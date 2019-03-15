package ru.serdtsev.zenorger

import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.DigestUtils
import org.springframework.web.filter.GenericFilterBean
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class MdcConfig(val requestContext: RequestContext) {
    private val log = KotlinLogging.logger {}
    private val mdcHeaderPrefix = "X-MDC-"
    private val organizerIdSuffix = "Organizer-Id"
    private val requestIdSuffix = "Request-Id"
    private val requestChannelSuffix = "Request-Channel"
    private val instanceIdSuffix = "Instance-Id"
    private val ignorableUriPrefixes = arrayOf("/webjars", "/swagger", "/api-doc")

    @Bean
    fun mdcProviderFilter(): Filter {
        return object : GenericFilterBean() {
            override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
                val httpServletRequest = request as HttpServletRequest
                val uri = httpServletRequest.requestURI
                val requestId = httpServletRequest.getHeader(mdcHeaderPrefix + requestIdSuffix) ?: run { generateRequestId() }
                val organizerId = httpServletRequest.getHeader(mdcHeaderPrefix + organizerIdSuffix)
                val requestChannel = httpServletRequest.getHeader(mdcHeaderPrefix + requestChannelSuffix)
                val instanceId = httpServletRequest.getHeader(mdcHeaderPrefix + instanceIdSuffix)

                val ignorableUri = isIgnorableUri(uri)
                if (!ignorableUri) {
                    // Organizer Id
                    requestContext.organizerId = organizerId?.let { UUID.fromString(it) }
                    MDC.put(this@MdcConfig.organizerIdSuffix, organizerId)
                    // Request Context Id
                    requestContext.requestId = requestId
                    MDC.put(this@MdcConfig.requestIdSuffix, requestId)
                    MDC.put(this@MdcConfig.requestChannelSuffix, requestChannel)
                    MDC.put(this@MdcConfig.instanceIdSuffix, instanceId)
                    log.trace {
                        "Saved MDC: REQ $uri: requestIdSuffix = $requestId; requestChannelSuffix = $requestChannel; organizerIdSuffix = $organizerId; instanceIdSuffix = $instanceId"
                    }

                    // Labels for access.log
                    with (httpServletRequest) {
                        setAttribute(this@MdcConfig.organizerIdSuffix, organizerId)
                        setAttribute(this@MdcConfig.requestIdSuffix, requestId)
                        setAttribute(this@MdcConfig.requestChannelSuffix, requestChannel)
                        setAttribute(this@MdcConfig.instanceIdSuffix, instanceId)
                    }
                }

                chain.doFilter(request, response)

                val httpServletResponse = response as HttpServletResponse
                httpServletResponse.setHeader(mdcHeaderPrefix + requestIdSuffix, MDC.get(requestId))
                httpServletResponse.setHeader(mdcHeaderPrefix + organizerIdSuffix, MDC.get(organizerIdSuffix))

                if (!ignorableUri) {
                    log.trace("Clear MDC")
                    MDC.clear()
                }
            }
        }
    }

    private fun isIgnorableUri(uri: String): Boolean = Arrays.stream(ignorableUriPrefixes).anyMatch { uri.startsWith(it) }

    private fun generateRequestId() = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().toByteArray()).substring(26)
}