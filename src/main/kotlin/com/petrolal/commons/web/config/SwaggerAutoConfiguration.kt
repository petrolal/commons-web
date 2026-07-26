package com.petrolal.commons.web.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.HeaderParameter
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnClass(OpenAPI::class)
@ConditionalOnProperty(name = ["commons.web.swagger.enabled"], havingValue = "true", matchIfMissing = true)
class SwaggerAutoConfiguration(
    private val properties: CommonsWebProperties
) {

    @Bean
    @ConditionalOnMissingBean
    fun customOpenAPI(): OpenAPI {
        val swaggerProps = properties.swagger

        val correlationIdHeader = HeaderParameter()
            .name(properties.correlationHeaderName)
            .description("Correlation ID for tracing requests")
            .required(false)
            .schema(StringSchema())

        return OpenAPI()
            .info(
                Info()
                    .title(swaggerProps.title)
                    .description(swaggerProps.description)
                    .version(swaggerProps.version)
            )
            .components(
                Components().addParameters(properties.correlationHeaderName, correlationIdHeader)
            )
    }
}