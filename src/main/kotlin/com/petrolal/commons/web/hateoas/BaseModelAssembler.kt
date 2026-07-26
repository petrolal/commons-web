package com.petrolal.commons.web.hateoas

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler

/**
 * Generic Kotlin-friendly interface for mapping Domain Entities -> HATEOAS EntityModels.
 */
abstract class BaseModelAssembler<T : Any, D : Any> : RepresentationModelAssembler<T, EntityModel<D>> {

    abstract fun toResourceDto(entity: T): D

    override fun toModel(entity: T): EntityModel<D> {
        val dto = toResourceDto(entity)
        val model = EntityModel.of(dto)
        addLinks(entity, model)
        return model
    }

    /**
     * Override to append custom HATEOAS links.
     */
    open fun addLinks(entity: T, model: EntityModel<D>) {}
}