package com.example.shopenest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull


@Dao
interface ShoppingDao {

    // Insert a single product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @NotNull
    suspend fun saveProduct(product: Product):Long

    // Retrieve all saved products as a list
    @Query("SELECT * FROM Product_Table")
  fun getAllSavedProducts(): Flow<List<Product>>

}