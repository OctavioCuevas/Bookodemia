package com.mx.crystalcloud.bookodemia.model

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @Serializable
    val data: List<Item>
)