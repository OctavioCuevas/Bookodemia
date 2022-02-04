package com.mx.crystalcloud.bookodemia.model

import kotlinx.serialization.Serializable

@Serializable
data class Observacion(
    val fecha: String,
    val comentario: String
)