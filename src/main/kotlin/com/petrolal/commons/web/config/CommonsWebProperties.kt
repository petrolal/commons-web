package com.petrolal.commons.web.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "commons.web")
data class CommonsWebProperties(
    var correlationHeaderName: String = "X-Correlation-ID",
    var swagger: SwaggerProperties = SwaggerProperties()
) {
    data class SwaggerProperties(
        var enabled: Boolean = true,
        var title: String = "API Documentation",
        var description: String = "Base API specifications and endpoints",
        var version: String = "1.0.0"
    )
}