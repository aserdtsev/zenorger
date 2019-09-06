package ru.serdtsev.zenorger.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.util.DigestUtils
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.web.filter.GenericFilterBean
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
import ru.serdtsev.zenorger.common.HttpErrorResponse
import ru.serdtsev.zenorger.common.ZenorgerException
import ru.serdtsev.zenorger.organizer.OrganizerService
import java.time.Instant
import java.util.*
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class RequestConfig {
    companion object {
        private val log = KotlinLogging.logger {}
        private val ignorableUriPrefixes = listOf("/webjars", "/swagger", "/v2/api-doc")
        private val organizerIdNotNeedUriPrefixes = listOf("/user/signUp").plus(ignorableUriPrefixes)
        private const val requestIdHeaderName = "X-Request-Id"
        private const val organizerIdHeaderName = "X-Organizer-Id"
        private const val requestIdKey = "requestId"
    }

    @Bean
    fun appRequestContextFilter(): Filter = object : GenericFilterBean() {
        override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
            val httpServletRequest = request as HttpServletRequest
            val uri = httpServletRequest.requestURI
            val ignorableUri = isUriContain(uri, ignorableUriPrefixes)
            val organizerIdNotNeedUri = isUriContain(uri, organizerIdNotNeedUriPrefixes)

            val httpServletResponse = response as HttpServletResponse
            try {
                if (!ignorableUri)
                    ApiRequestContextHolder.requestId = httpServletRequest.getHeader(requestIdHeaderName) ?: generateRequestId()

                if (!organizerIdNotNeedUri)
                    ApiRequestContextHolder.organizerId = httpServletRequest.getHeader(organizerIdHeaderName)
                            ?.let { UUID.fromString(it) }

                chain.doFilter(request, response)

                if (!ignorableUri)
                    httpServletResponse.setHeader(requestIdHeaderName, ApiRequestContextHolder.requestId)

                if (!organizerIdNotNeedUri)
                    httpServletResponse.setHeader(organizerIdHeaderName, ApiRequestContextHolder.organizerId?.toString())
            } catch (ex: ZenorgerException) {
                setErrorInResponse(httpServletResponse, ex, uri)
            }
        }

        private fun generateRequestId() = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().toByteArray()).substring(24)

        private fun setErrorInResponse(httpServletResponse: HttpServletResponse, ex: ZenorgerException, uri: String?) {
            val errorResponse = HttpErrorResponse(Instant.now(), ex.httpStatus.value(), ex.error, ex.message, uri)
            val mapper = ObjectMapper().apply {
                registerModule(JavaTimeModule())
                setSerializationInclusion(JsonInclude.Include.NON_NULL)
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                enable(SerializationFeature.INDENT_OUTPUT)
            }
            with(httpServletResponse) {
                status = errorResponse.status
                val body = mapper.writeValueAsString(errorResponse)
                writer.write(body)
            }
        }
    }

    @Bean
    fun mdcProviderFilter(): Filter = object : GenericFilterBean() {
        override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
            val httpServletRequest = request as HttpServletRequest
            val uri = httpServletRequest.requestURI

            val ignorableUri = isUriContain(uri, ignorableUriPrefixes)
            if (!ignorableUri) {
                MDC.put(requestIdKey, ApiRequestContextHolder.requestId)
                log.debug { ApiRequestContextHolder.apiRequestContext }

                // Labels for access.log
                with (httpServletRequest) {
                    setAttribute(requestIdKey, ApiRequestContextHolder.requestId)
                }
            }

            chain.doFilter(request, response)

            if (!ignorableUri) {
                MDC.clear()
            }
        }
    }

    @Bean
    fun logFilter(): CommonsRequestLoggingFilter = CommonsRequestLoggingFilter().apply {
        setIncludeQueryString(true)
        setIncludePayload(true)
        setMaxPayloadLength(10000)
        setIncludeHeaders(true)
    }

    @Bean
    fun checkRequestFilter(organizerService: OrganizerService): Filter = object : GenericFilterBean() {
        override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
            val httpServletRequest = request as HttpServletRequest
            val uri = httpServletRequest.requestURI
            val organizerIdNotNeedUri = isUriContain(uri, organizerIdNotNeedUriPrefixes)

            val httpServletResponse = response as HttpServletResponse
            try {
                if (!organizerIdNotNeedUri && ApiRequestContextHolder.organizerId == null)
                    throw ZenorgerException(HttpStatus.BAD_REQUEST, "Header 'X-Organizer-Id' is not defined")
                // Проверим существование органайзера.
                organizerService.getOrganizer()

                chain.doFilter(request, response)
            } catch (ex: ZenorgerException) {
                setErrorInResponse(httpServletResponse, ex, uri)
            }
        }

        private fun setErrorInResponse(httpServletResponse: HttpServletResponse, ex: ZenorgerException, uri: String?) {
            val errorResponse = HttpErrorResponse(Instant.now(), ex.httpStatus.value(), ex.error, ex.message, uri)
            val mapper = ObjectMapper().apply {
                registerModule(JavaTimeModule())
                setSerializationInclusion(JsonInclude.Include.NON_NULL)
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                enable(SerializationFeature.INDENT_OUTPUT)
            }
            with(httpServletResponse) {
                status = errorResponse.status
                val body = mapper.writeValueAsString(errorResponse)
                writer.write(body)
            }
        }
    }

    private fun isUriContain(uri: String, list: List<String>): Boolean = list.any { uri.startsWith(it) }
}
