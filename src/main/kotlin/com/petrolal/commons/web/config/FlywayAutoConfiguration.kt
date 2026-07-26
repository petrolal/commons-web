package com.petrolal.commons.web.config

import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnClass(Flyway::class)
@ConditionalOnProperty(name = ["commons.web.flyway.enabled"], havingValue = "true", matchIfMissing = true)
class FlywayAutoConfiguration(
    private val properties: CommonsWebProperties
) {

    @Bean
    fun commonsFlywayConfigurationCustomizer(): FlywayConfigurationCustomizer {
        return FlywayConfigurationCustomizer { configuration ->
            configuration.baselineOnMigrate(true)
            if (properties.flyway.locations.isNotEmpty()) {
                configuration.locations(*properties.flyway.locations.toTypedArray())
            }
        }
    }
}
