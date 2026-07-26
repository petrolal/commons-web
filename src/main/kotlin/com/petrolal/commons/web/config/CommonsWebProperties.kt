package com.petrolal.commons.web.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "commons.web")
data class CommonsWebProperties(
    var correlationHeaderName: String = "X-Correlation-ID",
    var swagger: SwaggerProperties = SwaggerProperties(),
    var jpa: JpaProperties = JpaProperties(),
    var flyway: FlywayProperties = FlywayProperties()
) {
    data class SwaggerProperties(
        var enabled: Boolean = true,
        var title: String = "API Documentation",
        var description: String = "Base API specifications and endpoints",
        var version: String = "1.0.0"
    )

    data class JpaProperties(
        var enableAuditing: Boolean = true
    )

    data class FlywayProperties(
        var enabled: Boolean = true,
        var locations: List<String> = listOf("classpath:db/migration")
    )
}