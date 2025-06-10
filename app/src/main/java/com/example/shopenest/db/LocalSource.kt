package com.example.shopenest.db

import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.Flow


interface LocalSource {

suspend fun getAllFavProducts():Flow<List<Product>>

suspend fun saveProduct (product:Product)

}