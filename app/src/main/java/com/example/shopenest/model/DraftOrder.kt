package com.example.shopenest.model

import com.google.gson.annotations.SerializedName



// DraftOrder
data class DraftOrder(
    val id: Long,
    val note: Any?,
    val email: Any?,
    val taxes_included: Boolean,
    val currency: String?,
    val invoice_sent_at: Any?,
    val created_at: String?,   // use String if ISO 8601; convert later to Date if needed
    val updated_at: String?,
    val tax_exempt: Boolean,
    val completed_at: String?,
    val name: String?,
    @SerializedName("allow_discount_codes_in_checkout?")
    val allow_discount_codes_in_checkout: Boolean?,
    @SerializedName("b2b?")
    val b2b: Boolean?,
    val status: String?,
    val line_items: List<LineItem>?,
    val api_client_id: Any?,
    val shipping_address: AddressBody,
    val billing_address: AddressBody?,
    val invoice_url: String?,
    val created_on_api_version_handle: Any?,
    val applied_discount: AppliedDiscount?,
    val order_id: Long?,
    val shipping_line: Any?,
    val tax_lines: List<TaxLine>?,
    val tags: String?,
    val note_attributes: List<Any>?,
    val total_price: String?,
    val subtotal_price: String?,
    val total_tax: String?,
    val payment_terms: Any?,
    val customer: CustomerData,
    val default_address:CustomerAddress,
    val admin_graphql_api_id: String?
)






