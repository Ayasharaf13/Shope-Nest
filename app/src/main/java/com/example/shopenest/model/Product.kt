package com.example.shopenest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Product_Table")
data class Product(

    @PrimaryKey val id: Long = 0L,
    val title: String ="",
    val body_html: String ="",
    val vendor: String ="",
    val productType: String ="",
    val createdAt: String = "",
    val handle: String ="",
    val updatedAt: String ="",
    val publishedAt: String = "",
  //  val templateSuffix: Any?,
    val publishedScope: String = "",
    val tags: String =  "",
    val status: String = "",
    val adminGraphqlApiId: String = "",
    val image:ImageProduct = ImageProduct(),
    val variants: List<Variant> = List(3) { Variant() },

    // val variants: List<Variant>  = List<Variant>(),
   // val options: List<Option>,
    val images: List<ImageProduct> = emptyList()




    )
