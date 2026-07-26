package com.petrolal.commons.web.hateoas

import org.springframework.hateoas.RepresentationModel

/**
 * Base class for all DTOs requiring HATEOAS hypermedia links (_links).
 */
abstract class BaseRepresentationModel<T : BaseRepresentationModel<T>> : RepresentationModel<T>()