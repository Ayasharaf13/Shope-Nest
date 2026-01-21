package com.example.shopenest.model

import com.google.gson.annotations.SerializedName

data class DraftOrderRequestBody(


    //  val customer: Customer? = null
    @SerializedName("line_items")
    val line_items: List<ItemsOrder>,
    @SerializedName("applied_discount")
    val applied_discount: AppliedDiscount = AppliedDiscount(),
    @SerializedName("customer")
    val customer: CustomerRef? = null,
    @SerializedName("use_customer_default_address")
    val use_customer_default_address: Boolean = true

)
