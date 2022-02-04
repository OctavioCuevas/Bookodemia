package com.mx.crystalcloud.bookodemia.model

import kotlinx.serialization.Serializable

@Serializable
data class Links(
    val self: String,
    val related: String = ""
)