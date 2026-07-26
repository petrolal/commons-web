package com.petrolal.commons.web.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@AutoConfiguration
@ConditionalOnClass(JpaRepository::class)
@ConditionalOnProperty(name = ["commons.web.jpa.enable-auditing"], havingValue = "true", matchIfMissing = true)
@EnableJpaAuditing
class JpaAutoConfiguration
