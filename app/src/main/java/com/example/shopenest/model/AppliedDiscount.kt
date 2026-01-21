package com.example.shopenest.model

import com.google.gson.annotations.SerializedName


data class AppliedDiscount(


    @SerializedName("description")
    val description: String? = "",
    @SerializedName("value")
    val value: String? = "",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("amount")
    val amount: String? = "",
    @SerializedName("value_type")
    val value_type: String? = ""


)


