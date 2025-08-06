package com.example.shopenest.model

data class Variant(

val id: Long =0L,
val product_id: Long = 0L,
val title: String = "",
val price: String= ""
/*
val position: Int,
val inventory_policy: String,
//val compare_at_price: Any?, // Use Any? for nullable/unknown type
val option1: String,
val option2: String,
//val option3: Any?,           // nullable
val created_at: String,      // Or use java.util.Date if you parse it
val updated_at: String,      // Or Date
val taxable: Boolean,
//val barcode: Any?,
val fulfillment_service: String,
val grams: Int,
val inventory_management: String,
val requires_shipping: Boolean,
val sku: String,
val weight: Double,
val weight_unit: String,
val inventory_item_id: Long,
val inventory_quantity: Int,
val old_inventory_quantity: Int,
val admin_graphql_api_id: String,
//val image_id: Any?
*/

)
