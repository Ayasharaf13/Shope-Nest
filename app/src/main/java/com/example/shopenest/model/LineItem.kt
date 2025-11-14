package com.example.shopenest.model


data class LineItem(
    val id: Long = 0L,
    val variant_id: Long = 0L,
    val product_id: Long? = null,
    val title: String? = null,
    val variant_title: String? = null,
    val sku: String? = null,
    val vendor: String? = null,
    val quantity: Int? = 0,
    val requires_shipping: Boolean = false,
    val taxable: Boolean = false,
    val gift_card: Boolean = false,
    val fulfillment_service: String? = null,
    val grams: Int? = 0,
    val tax_lines: List<TaxLine>? = emptyList(),
    val applied_discount: Any? = null,
    val name: String? = null,
    val properties: List<Property>? = emptyList(),
    val custom: Boolean = false,
    val price: String? = null,
    val admin_graphql_api_id: String? = null
)
