package com.mx.crystalcloud.bookodemia.model

import kotlinx.serialization.Serializable

@Serializable
data class Relationships(
    val authors: Author,
    val categories: Categories,
    val books: Books
)