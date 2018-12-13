package ru.serdtsev.zenorger

import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.DigestUtils
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Configuration
class MdcConfig {
    private val log = KotlinLogging.logger {}
    private val mdcHeaderPrefix = "x-mdc-"
    private val oid = "oid"
    private val rci = "rci"
    private val requestChannel = "request-channel"
    private val instanceId = "instance-id"
    private val ignorableUriPrefixes = arrayOf("/webjars", "/swagger", "/api-doc")

    @Bean
    fun mdcProviderFilter(): Filter {
        return object : GenericFilterBean() {
            @Throws(IOException::class, ServletException::class)
            override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
                val httpServletRequest = request as HttpServletRequest
                val uri = httpServletRequest.requestURI
                val oid = httpServletRequest.getHeader(mdcHeaderPrefix + oid)
                val rci = httpServletRequest.getHeader(mdcHeaderPrefix + rci) ?: run { generateRci() }
                val requestChannel = httpServletRequest.getHeader(mdcHeaderPrefix + requestChannel)
                val instanceId = httpServletRequest.getHeader(mdcHeaderPrefix + instanceId)

                val ignorableUri = isIgnorableUri(uri)
                if (!ignorableUri) {
                    // Organizer Id
                    MDC.put(this@MdcConfig.oid, oid)
                    // Request Context Id
                    MDC.put(this@MdcConfig.rci, rci)
                    MDC.put(this@MdcConfig.requestChannel, requestChannel)
                    MDC.put(this@MdcConfig.instanceId, instanceId)
                    log.trace {
                        "Saved MDC: REQ $uri: rci = $rci; requestChannel = $requestChannel; oid = $oid; instanceId = $instanceId"
                    }

                    // Labels for access.log
                    with (httpServletRequest) {
                        setAttribute(this@MdcConfig.oid, oid)
                        setAttribute(this@MdcConfig.rci, rci)
                        setAttribute(this@MdcConfig.requestChannel, requestChannel)
                        setAttribute(this@MdcConfig.instanceId, instanceId)
                    }
                }

                chain.doFilter(request, response)

                if (!ignorableUri) {
                    log.trace("Clear MDC")
                    MDC.clear()
                }
            }
        }
    }

    private fun isIgnorableUri(uri: String): Boolean = Arrays.stream(ignorableUriPrefixes).anyMatch { uri.startsWith(it) }

    private fun generateRci() = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().toByteArray()).substring(26)
}