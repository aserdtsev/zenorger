package ru.serdtsev.zenorger.config

import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.DigestUtils
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.web.filter.GenericFilterBean
import ru.serdtsev.zenorger.common.ApiRequestContextHolder
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
        private val ignorableUriPrefixes = arrayOf("/webjars", "/swagger", "/api-doc")
        private const val requestIdHeaderName = "X-Request-Id"
        private const val organizerIdHeaderName = "X-Organizer-Id"
        private const val requestIdKey = "requestId"
    }

    @Bean
    fun appRequestContextFilter(): Filter = object : GenericFilterBean() {
        override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
            val httpServletRequest = request as HttpServletRequest
            ApiRequestContextHolder.requestId = httpServletRequest.getHeader(requestIdHeaderName) ?: run { generateRequestId() }
            ApiRequestContextHolder.organizerId = httpServletRequest.getHeader(organizerIdHeaderName)?.let { UUID.fromString(it) }

            chain.doFilter(request, response)

            val httpServletResponse = response as HttpServletResponse
            with (httpServletResponse) {
                setHeader(requestIdHeaderName, ApiRequestContextHolder.requestId)
                setHeader(organizerIdHeaderName, ApiRequestContextHolder.organizerId?.toString())
            }
        }

        private fun generateRequestId() = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().toByteArray()).substring(24)
    }

    @Bean
    fun mdcProviderFilter(): Filter = object : GenericFilterBean() {
        override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
            val httpServletRequest = request as HttpServletRequest
            val uri = httpServletRequest.requestURI

            val ignorableUri = isIgnorableUri(uri)
            if (!ignorableUri) {
                MDC.put(requestIdKey, ApiRequestContextHolder.requestId)
                log.info { ApiRequestContextHolder.apiRequestContext }

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
        setAfterMessagePrefix("REQUEST DATA : ")
    }

    private fun isIgnorableUri(uri: String): Boolean = Arrays.stream(ignorableUriPrefixes).anyMatch { uri.startsWith(it) }
}
