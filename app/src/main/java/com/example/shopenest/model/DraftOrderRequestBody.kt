package com.example.shopenest.model

data class DraftOrderRequestBody(



  //  val customer: Customer? = null

    val line_items: List<LineItem>,
    val applied_discount: AppliedDiscount,
    val customer: CustomerRef? = null,
    val use_customer_default_address: Boolean = true

)
