package com.petrolal.commons.web.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.hateoas.config.EnableHypermediaSupport

@AutoConfiguration
@ConditionalOnClass(EnableHypermediaSupport::class)
@EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL])
class HateoasAutoConfiguration