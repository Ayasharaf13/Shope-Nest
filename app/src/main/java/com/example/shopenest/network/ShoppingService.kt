package com.example.shopenest.network

import com.example.shopenest.model.*
import com.example.shopenest.utilities.Constants
import retrofit2.Response
import retrofit2.http.*

//get products related to brands and category
//https://itp-sv-and7.myshopify.com/admin/api/2024-10/collections/451595567394/products.json

//get category
//https://itp-sv-and7.myshopify.com/admin/api/2024-10/custom_collections.json

// https://itp-sv-and7.myshopify.com/admin/api/2024-10/collections/451595567394/products.json // id:smartcollection

//get prodect details
//https://itp-sv-and7.myshopify.com/admin/api/2024-10/products/8393073230114.json

interface ShoppingService {
// response return object inside into list brands (smartcollection)
  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
      @GET("smart_collections.json")
     suspend fun getBrands():Brands



    @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
    @GET("custom_collections.json")
    suspend fun getCategory():Categories


    @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
    @GET("collections/451595993378/products.json")
    suspend fun getProductsForSectionKidsCategory():ShoppingProducts

    @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
    @GET("collections/451595960610/products.json")
    suspend fun getProductsForSectionWomenCategory():ShoppingProducts


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("collections/451595927842/products.json")
  suspend fun getProductsForSectionManCategory():ShoppingProducts


  /*@Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("collections/{id}/products.json")
  suspend fun getProductsForBrands(@Path("id") id: Long): ShoppingProducts

   */


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("products/{id}.json")
  suspend fun getProductsDetails(@Path("id") id: Long): ProductResponse



  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("products.json")
  suspend fun getProductsForBrands(@Query("vendor") vendor: String): ShoppingProducts


  @Headers(
    "X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}"
    ,"Content-Type:application/json")
  @POST("customers.json")
  suspend fun createCustomer (@Body customer: ResponseCustomer): Response<ResponseCustomer>


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("customers/search.json")
  suspend fun getCustomerByEmail(@Query("query") query: String): Response<Customers>


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("customers/count.json")
  suspend fun getCountCustomer():CountCustomer


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @DELETE("customers/{id}.json")
  suspend fun deleteCustomer ( @Path("id") customerId: Long):Response<Unit>



}