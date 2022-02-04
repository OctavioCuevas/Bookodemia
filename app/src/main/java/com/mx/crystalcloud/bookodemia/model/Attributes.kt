package com.mx.crystalcloud.bookodemia.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attributes (
    val title: String = "",
    val name: String = "",
    val slug: String,
    val content: String = "",
    @SerialName("created-at")
    val createdAt: String = "",
    @SerialName("updated-at")
    val updatedAt: String = ""
)