package com.example.shopenest.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(
    tableName = "Product_Table",
    primaryKeys = ["id", "customerId"],
    indices = [Index("customerId")]
)

data class Product(

    val id: Long = 0L,
    var customerId: Long = 0L,     // from SharedPreferences
    val title: String = "",
    val body_html: String = "",
    val vendor: String = "",
    val productType: String = "",
    val createdAt: String = "",
    val handle: String = "",
    val updatedAt: String = "",
    val publishedAt: String = "",
    val publishedScope: String = "",
    val tags: String = "",
    val status: String = "",
    val adminGraphqlApiId: String = "",
    val image: ImageProduct = ImageProduct(),
    val variants: List<Variant> = emptyList(),
    val images: List<ImageProduct> = emptyList()


) : Serializable
