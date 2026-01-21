package com.example.shopenest.db


import com.example.shopenest.model.DraftOrderHeaderEntity
import com.example.shopenest.model.LineItem
import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.Flow


interface LocalSource {

    suspend fun getAllFavProducts(customerId: Long): Flow<List<Product>>
    suspend fun saveProduct(product: Product)
    suspend fun deleteById(productId: Long, customerId: Long)
    suspend fun saveDraftOrderHeader(header: DraftOrderHeaderEntity)
    suspend fun saveLineItems(items: List<LineItem>)
    fun getLineItems(customerId: Long): Flow<List<LineItem>>
    suspend fun deleteDraftOrder(draftOrderId: Long, customerId: Long)


    fun getDraftOrderHeader(

        customerId: Long
    ): Flow<DraftOrderHeaderEntity?>


    fun getDraftOrderWithItems(customerId: Long): Flow<Pair<DraftOrderHeaderEntity?, List<LineItem>>>
    suspend fun saveDraftOrderWithItems(header: DraftOrderHeaderEntity, items: List<LineItem>)

    suspend fun increaseQuantity(lineItemId: Long, customerId: Long)

    suspend fun decreaseQuantity(lineItemId: Long, customerId: Long)


}