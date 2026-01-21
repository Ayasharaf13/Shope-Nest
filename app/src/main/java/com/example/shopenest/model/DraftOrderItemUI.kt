package com.example.shopenest.model



data class DraftOrderItemUI(
    val productId:Long,
    val lineItemId: Long,
    val draftOrderId:Long,
    val title: String,
    val image: String,
    val quantity: Int,
    val price: Double
)