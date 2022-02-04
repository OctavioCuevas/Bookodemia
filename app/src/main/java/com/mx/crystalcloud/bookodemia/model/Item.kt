package com.mx.crystalcloud.bookodemia.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val type: String,
    val id: String,
    val attributes: Attributes,
    val relationships: Relationships,
    val links: Links
) : java.io.Serializable