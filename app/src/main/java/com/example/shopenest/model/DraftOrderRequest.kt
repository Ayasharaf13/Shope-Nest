package com.example.shopenest.model

import com.google.gson.annotations.SerializedName


data class  DraftOrderRequest (
    @SerializedName("draft_order")
    var draft_order: DraftOrderRequestBody

)