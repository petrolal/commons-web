package com.petrolal.commons.web.exception

import java.time.Instant

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String?,
    val timestamp: Instant = Instant.now(),
    val details: List<String>? = null
)