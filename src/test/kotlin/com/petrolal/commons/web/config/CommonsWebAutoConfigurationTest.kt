package com.petrolal.commons.web.config

import com.petrolal.commons.web.filter.CorrelationIdFilter
import com.petrolal.commons.web.test.TestApplication
import io.swagger.v3.oas.models.OpenAPI
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [TestApplication::class])
@ActiveProfiles("test")
class CommonsWebAutoConfigurationTest {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Test
    fun `should load all commons web auto-configurations and beans`() {
        assertThat(applicationContext.containsBean("correlationIdFilter")).isTrue()
        assertThat(applicationContext.getBean(CorrelationIdFilter::class.java)).isNotNull

        assertThat(applicationContext.containsBean("customOpenAPI")).isTrue()
        assertThat(applicationContext.getBean(OpenAPI::class.java)).isNotNull

        assertThat(applicationContext.containsBean("commonsFlywayConfigurationCustomizer")).isTrue()
        val customizer = applicationContext.getBean("commonsFlywayConfigurationCustomizer")
        assertThat(customizer).isNotNull

        assertThat(applicationContext.getBean(Flyway::class.java)).isNotNull
    }
}
