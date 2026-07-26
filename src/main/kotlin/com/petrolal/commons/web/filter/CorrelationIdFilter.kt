package com.petrolal.commons.web.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

class CorrelationIdFilter(
    private val headerName: String = "X-Correlation-ID"
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val correlationId = request.getHeader(headerName)?.takeIf { it.isNotBlank() }
            ?: UUID.randomUUID().toString()

        MDC.put("correlationId", correlationId)
        response.setHeader(headerName, correlationId)

        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.remove("correlationId")
        }
    }
}