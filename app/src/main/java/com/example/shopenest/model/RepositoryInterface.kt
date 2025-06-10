package com.example.shopenest.model

import kotlinx.coroutines.flow.Flow


interface RepositoryInterface {


suspend fun getBrands():Brands

suspend fun getAllFavProducts(): Flow<List<Product>>

suspend fun saveProduct(product: Product)

    suspend fun getCategory():Categories
    suspend fun getProductsForSectionKidsCategory():ShoppingProducts
    suspend fun getProductsForSectionWomenCategory():ShoppingProducts
    suspend fun getProductsForSectionMenCategory():ShoppingProducts
    suspend fun getProductsForBrands(id:Long):ShoppingProducts
    suspend fun getProductsDetails(id:Long):ProductResponse


}