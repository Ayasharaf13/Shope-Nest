package com.example.shopenest.model



data class InventoryLevel(

    val inventory_item_id: Long,
    val location_id: Long,
    val available: Int,
    val updated_at: String, // ISO 8601 datetime as String
    val admin_graphql_api_id: String

)
