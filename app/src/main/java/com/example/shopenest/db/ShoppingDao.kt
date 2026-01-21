package com.example.shopenest.db

import androidx.room.*
import com.example.shopenest.model.DraftOrderHeaderEntity
import com.example.shopenest.model.LineItem
import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.jetbrains.annotations.NotNull


@Dao
interface ShoppingDao {


    // Insert a single product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @NotNull
    suspend fun saveProduct(product: Product): Long

    @Query("DELETE FROM Product_Table WHERE id = :productId AND customerId = :customerId")
    suspend fun deleteById(productId: Long, customerId: Long)

    @Query("SELECT * FROM Product_Table WHERE customerId = :customerId")
    fun getFavProducts(customerId: Long): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @NotNull
    suspend fun saveDraftOrderHeader(header: DraftOrderHeaderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @NotNull
    suspend fun saveLineItems(items: List<LineItem>)


    // @Query("SELECT * FROM draft_order_line_items WHERE draftOrder_id = :id AND customerId = :customerId")
    // fun getLineItems(id: Long, customerId: Long): Flow<List<LineItem>>

    @Query("SELECT * FROM draft_order_line_items WHERE customerId = :customerId")
    fun getLineItems(customerId: Long): Flow<List<LineItem>>


    @Query("SELECT * FROM draft_order_header WHERE customerId = :customerId")

    fun getDraftOrderHeader(

        customerId: Long
    ): Flow<DraftOrderHeaderEntity?>

    // Combine header + items
    fun getDraftOrderWithItems(customerId: Long): Flow<Pair<DraftOrderHeaderEntity?, List<LineItem>>> {
        return combine(
            getDraftOrderHeader(customerId),
            getLineItems(customerId)
        ) { header, items -> header to items }
    }


    @Transaction
    suspend fun saveDraftOrderWithItems(header: DraftOrderHeaderEntity, items: List<LineItem>) {
        saveDraftOrderHeader(header)   // Save parent first
        saveLineItems(items)           // Then children
    }

    /* @Query("SELECT * FROM draft_order_header WHERE draftOrder_id = :draftOrderId AND customerId = :customerId")

     fun getDraftOrderHeader(
         draftOrderId: Long,
         customerId: Long
     ): Flow<DraftOrderHeaderEntity?>

     */


    // when delete draft removes all its line items.Because onDelete = ForeignKey.CASCADE
    @Query(
        """
    DELETE FROM draft_order_header 
    WHERE draftOrder_id = :draftOrderId AND customerId = :customerId
"""
    )
    suspend fun deleteDraftOrder(draftOrderId: Long, customerId: Long)


    // no need to delete line because I use auto delete which is mean when delete parent then delete children
// remove line item not lead to remove parent (draftorder)
    @Query(
        """
    DELETE FROM draft_order_line_items 
    WHERE id_lineItem = :lineItemId AND customerId = :customerId
"""
    )
    suspend fun deleteLineItem(lineItemId: Long, customerId: Long)


    @Query(
        """
        UPDATE draft_order_line_items
        SET quantity = quantity + 1
        WHERE id_lineItem = :lineItemId
        AND customerId = :customerId
    """
    )
    suspend fun increaseQuantity(lineItemId: Long, customerId: Long)


    @Query(
        """
        UPDATE draft_order_line_items
        SET quantity = quantity - 1
        WHERE id_lineItem = :lineItemId
        AND customerId = :customerId
        AND quantity > 1
    """
    )
    suspend fun decreaseQuantity(lineItemId: Long, customerId: Long)


}
