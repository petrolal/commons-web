package com.petrolal.commons.web.test

import com.petrolal.commons.web.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "test_entities")
class TestEntity(
    var name: String = ""
) : BaseEntity()
