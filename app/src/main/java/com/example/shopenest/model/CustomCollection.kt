package com.example.shopenest.model




data class CustomCollection (

    val id: Long,
    val handle: String,
    val title: String,
    val updatedAt: String,
    val bodyHtml: String? = "",
    val publishedAt: String,
    val sortOrder: String,
  //  val templateSuffix: Any?,
    //val publishedScope: String,
    //val adminGraphqlApiId: String,
    val image: ImageBrand?,


        )




