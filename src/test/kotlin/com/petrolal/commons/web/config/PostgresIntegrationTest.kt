package com.petrolal.commons.web.config

import com.petrolal.commons.web.test.TestApplication
import com.petrolal.commons.web.test.TestEntity
import com.petrolal.commons.web.test.TestEntityRepository
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(classes = [TestApplication::class])
@Testcontainers(disabledWithoutDocker = true)
class PostgresIntegrationTest {

    companion object {
        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:16-alpine").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpass")
        }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            if (isDockerAvailable()) {
                registry.add("spring.datasource.url", postgres::getJdbcUrl)
                registry.add("spring.datasource.username", postgres::getUsername)
                registry.add("spring.datasource.password", postgres::getPassword)
                registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
                registry.add("spring.jpa.database-platform") { "org.hibernate.dialect.PostgreSQLDialect" }
            } else {
                registry.add("spring.datasource.url") { "jdbc:h2:mem:testdb_pg;MODE=PostgreSQL;DB_CLOSE_DELAY=-1" }
                registry.add("spring.datasource.username") { "sa" }
                registry.add("spring.datasource.password") { "" }
                registry.add("spring.datasource.driver-class-name") { "org.h2.Driver" }
                registry.add("spring.jpa.database-platform") { "org.hibernate.dialect.H2Dialect" }
            }
            registry.add("commons.web.flyway.enabled") { "true" }
            registry.add("commons.web.jpa.enable-auditing") { "true" }
        }

        private fun isDockerAvailable(): Boolean {
            return try {
                DockerClientFactory.instance().isDockerAvailable
            } catch (e: Exception) {
                false
            }
        }
    }

    @Autowired
    private lateinit var flyway: Flyway

    @Autowired
    private lateinit var repository: TestEntityRepository

    @Test
    fun `should migrate DB with Flyway and perform JPA operations`() {
        val info = flyway.info()
        assertThat(info.applied()).isNotEmpty

        val entity = repository.save(TestEntity(name = "Postgres Test"))
        assertThat(entity.id).isNotNull()
        assertThat(entity.createdAt).isNotNull()

        val found = repository.findById(entity.id!!).orElse(null)
        assertThat(found).isNotNull
        assertThat(found.name).isEqualTo("Postgres Test")
    }
}
