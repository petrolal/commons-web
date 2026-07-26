package com.petrolal.commons.web.config

import com.petrolal.commons.web.test.TestApplication
import com.petrolal.commons.web.test.TestEntity
import com.petrolal.commons.web.test.TestEntityRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [TestApplication::class])
@ActiveProfiles("test")
class JpaAutoConfigurationTest {

    @Autowired
    private lateinit var repository: TestEntityRepository

    @Test
    fun `should persist entity and set audit timestamps automatically`() {
        val entity = TestEntity(name = "Integration Test Entity")
        assertThat(entity.createdAt).isNull()
        assertThat(entity.updatedAt).isNull()

        val saved = repository.save(entity)

        assertThat(saved.id).isNotNull()
        assertThat(saved.name).isEqualTo("Integration Test Entity")
        assertThat(saved.createdAt).isNotNull()
        assertThat(saved.updatedAt).isNotNull()

        val retrieved = repository.findById(saved.id!!).orElse(null)
        assertThat(retrieved).isNotNull
        assertThat(retrieved.name).isEqualTo("Integration Test Entity")
    }

    @Test
    fun `should enforce entity equality based on id`() {
        val entity1 = TestEntity(name = "Entity 1").apply { id = 100L }
        val entity2 = TestEntity(name = "Entity 2").apply { id = 100L }
        val entity3 = TestEntity(name = "Entity 3").apply { id = 200L }

        assertThat(entity1).isEqualTo(entity2)
        assertThat(entity1).isNotEqualTo(entity3)
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode())
    }
}
