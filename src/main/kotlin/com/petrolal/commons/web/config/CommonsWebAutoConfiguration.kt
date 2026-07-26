package com.petrolal.commons.web.config

import com.petrolal.commons.web.exception.GlobalExceptionHandler
import com.petrolal.commons.web.filter.CorrelationIdFilter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@AutoConfiguration
@EnableConfigurationProperties(CommonsWebProperties::class)
@Import(SwaggerAutoConfiguration::class)
class CommonsWebAutoConfiguration(
    private val properties: CommonsWebProperties
) {

    @Bean
    @ConditionalOnMissingBean
    fun correlationIdFilter(): CorrelationIdFilter =
        CorrelationIdFilter(properties.correlationHeaderName)

    @Bean
    @ConditionalOnMissingBean
    fun globalExceptionHandler(): GlobalExceptionHandler =
        GlobalExceptionHandler()
}