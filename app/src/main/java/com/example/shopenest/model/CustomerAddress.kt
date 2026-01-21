package com.example.shopenest.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CustomerAddress(

    val id: Long,
    val customer_id: Long,
    val first_name: String?,
    val address1: String?,
    val city: String?,
    val country: String?,
    val zip: String?,
    val phone: String?,
    @SerializedName("default")        // JSON key from API
    @ColumnInfo(name = "is_default")  // column name in Room DB
    val isDefault: Boolean = true    // safe Kotlin property name

) : Parcelable






