package com.petrolal.commons.web.config

import com.petrolal.commons.web.test.TestApplication
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [TestApplication::class])
@ActiveProfiles("test")
class FlywayAutoConfigurationTest {

    @Autowired
    private lateinit var flyway: Flyway

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `should run Flyway migrations successfully`() {
        val info = flyway.info()
        assertThat(info.applied()).isNotEmpty
        assertThat(info.applied()[0].script).isEqualTo("V1__create_test_entities.sql")

        val tableCount = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM information_schema.tables WHERE UPPER(table_name) = 'TEST_ENTITIES'",
            Int::class.java
        ) ?: 0
        assertThat(tableCount).isGreaterThan(0)
    }
}
