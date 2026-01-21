package com.example.shopenest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "draft_order_header",
    primaryKeys = ["draftOrder_id", "customerId"],
    indices = [Index("customerId")]
)
data class
DraftOrderHeaderEntity(

    @ColumnInfo(name = "draftOrder_id")
    @SerializedName("id")
    val draftOrderId: Long,

    val email: Any?,
    val taxes_included: Boolean,
    val currency: String?,

    val created_at: String?,   // use String if ISO 8601; convert later to Date if needed
    val updated_at: String?,
    val tax_exempt: Boolean,
    val completed_at: String?,
    val name: String?,
    @SerializedName("allow_discount_codes_in_checkout?")
    val allow_discount_codes_in_checkout: Boolean?,

    val status: String?,
    val api_client_id: Any?,
    val customerId: Long,     // from SharedPreferences
    val totalPrice: String?,
    val subtotalPrice: String?,


    val createdAt: String?,
    val updatedAt: String?,

    val shipping_address: AddressBody,
    val billing_address: AddressBody?,
    val invoice_url: String?,

    val applied_discount: AppliedDiscount? = null,
    val order_id: Long?,

    val tags: String?,
    val total_price: String?,
    val subtotal_price: String?,
    val total_tax: String?,


    val default_address: CustomerAddress?

)



