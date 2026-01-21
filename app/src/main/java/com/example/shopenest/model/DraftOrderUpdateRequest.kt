package com.example.shopenest.model

import com.google.gson.annotations.SerializedName


data class DraftOrderUpdateRequest(

    @SerializedName("draft_order")
    val draftOrder: DraftOrderUpdateBody

)


data class DraftOrderUpdateBody(
    val id: Long,
    @SerializedName("applied_discount")
    val appliedDiscount: AppliedDiscount

)
