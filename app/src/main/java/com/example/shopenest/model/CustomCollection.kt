package com.example.shopenest.model


data class CustomCollection(

    val id: Long,
    val handle: String,
    val title: String,
    val updatedAt: String,
    val bodyHtml: String? = "",
    val publishedAt: String,
    val sortOrder: String,
    val image: ImageBrand?,


    )




