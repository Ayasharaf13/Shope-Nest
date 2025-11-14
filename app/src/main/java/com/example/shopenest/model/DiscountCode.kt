package com.example.shopenest.model

data class DiscountCode(
    val id: Long,
    val price_rule_id: Long,
    val code: String,
    val usage_count: Int,
    val created_at: String,  // Or use Date if you set up a Date adapter with Gson/Moshi/Kotlinx
    val updated_at: String
)
