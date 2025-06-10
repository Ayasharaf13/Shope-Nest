package com.example.shopenest.network

import com.example.shopenest.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path


interface RemoteSource {

    suspend fun getBrands():Brands
    suspend fun getCategory(): Categories
    suspend fun getProductsForSectionKidsCategory(): ShoppingProducts
    suspend fun getProductsForSectionWomenCategory(): ShoppingProducts
    suspend fun getProductsForSectionMenCategory(): ShoppingProducts
    suspend fun getProductsForBrands(id:Long): ShoppingProducts
    suspend fun getProductsDetails( id: Long): ProductResponse


}