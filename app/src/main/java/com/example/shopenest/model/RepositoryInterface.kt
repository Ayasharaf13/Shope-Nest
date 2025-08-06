package com.example.shopenest.model

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query


interface RepositoryInterface {


suspend fun getBrands():Brands

suspend fun getAllFavProducts(): Flow<List<Product>>

suspend fun saveProduct(product: Product)

    suspend fun getCategory():Categories
    suspend fun getProductsForSectionKidsCategory():ShoppingProducts
    suspend fun getProductsForSectionWomenCategory():ShoppingProducts
    suspend fun getProductsForSectionMenCategory():ShoppingProducts
    suspend fun getProductsForBrands(vendor:String):ShoppingProducts
    suspend fun getProductsDetails(id:Long):ProductResponse
    suspend fun createCustomer ( customer: ResponseCustomer): Response<ResponseCustomer>
    suspend fun getCustomerByEmail(@Query("query") email: String): Response<Customers>
    suspend fun getCountCustomer():CountCustomer
    suspend fun deleteCustomer ( @Path("id") customerId: Long):Response<Unit>




}