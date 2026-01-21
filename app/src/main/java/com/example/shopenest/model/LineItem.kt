package com.example.shopenest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "draft_order_line_items",
    primaryKeys = ["id_lineItem"],
    foreignKeys = [
        ForeignKey(
            entity = DraftOrderHeaderEntity::class,
            parentColumns = ["draftOrder_id", "customerId"],
            childColumns = ["draftOrder_id", "customerId"],
            onUpdate = ForeignKey.CASCADE,// it works if change primary key which is not need to other system
            onDelete = ForeignKey.CASCADE,

            )
    ],
    indices = [Index("draftOrder_id"), Index("customerId")]
)
data class LineItem(
    @SerializedName("id")// primaryKey
    @ColumnInfo(name = "id_lineItem")
    val idLineItem: Long,

    // @SerializedName("id")
    @ColumnInfo(name = "draftOrder_id") //forgney
    var draftOrderId: Long = 0L,

    @SerializedName("variant_id")
    val variant_id: Long = 0L,
    val product_id: Long? = null,
    val title: String? = null,
    val variant_title: String? = null,
    val sku: String? = null,
    val vendor: String? = null,
    @SerializedName("quantity")
    val quantity: Int = 0,
    val requires_shipping: Boolean = false,
    val taxable: Boolean = false,
    val gift_card: Boolean = false,
    val fulfillment_service: String? = null,
    val grams: Int? = 0,
    val applied_discount: AppliedDiscount? = null, // = AppliedDiscount(), //Any? = null,
    val name: String? = null,

    val properties: List<Property>? = emptyList(),
    val custom: Boolean = false,
    val price: String? = null,

    var customerId: Long = 0L         // From SharedPreferences
)
