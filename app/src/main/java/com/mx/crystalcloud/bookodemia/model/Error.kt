package com.mx.crystalcloud.bookodemia.model

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val status: String,
    val title: String,
    val detail: String
)