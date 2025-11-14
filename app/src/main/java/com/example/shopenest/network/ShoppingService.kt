package com.example.shopenest.network

import com.example.shopenest.model.*
import com.example.shopenest.utilities.Constants
import retrofit2.Response
import retrofit2.http.*



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
  suspend fun createCustomer (@Body customer: CustomerRequest): Response<CustomerResponse>


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("customers/search.json")
  suspend fun getCustomerByEmail(@Query("query") query: String): Response<Customers>


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("customers/count.json")
  suspend fun getCountCustomer():CountCustomer


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @DELETE("customers/{id}.json")
  suspend fun deleteCustomer ( @Path("id") customerId: Long):Response<Unit>


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("inventory_levels.json")
  suspend fun getAvailableProducts(@Query("inventory_item_ids") inventoryItemId: Long): ResponseInventory


  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("price_rules/1407947047202/discount_codes.json")
  suspend fun getDiscount(): ResponseDiscount


  @Headers(
    "X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}"
    ,"Content-Type:application/json")
  @POST("draft_orders.json")
  suspend fun createCartOrder(@Body cartOrder: DraftOrderRequest): Response<ResponseDraftOrderForRequestCreate>



  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("draft_orders.json")
  suspend fun getDraftOrders():Response<ResponseDraftOrderForRetrieve>



  @Headers("X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @DELETE("draft_orders/{id}.json")
  suspend fun deleteDraftOrderById ( @Path("id") draftOrderId: Long):Response<Unit>



  @Headers( "X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @PUT("draft_orders/{draft_order_id}/complete.json")
  suspend fun completeDraftOrder(
    @Path("draft_order_id") draftOrderId: Long,
    @Query("payment_pending") paymentPending: Boolean = true
  ): Response<ResponseDraftOrderForRequestCreate>



  @Headers( "X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @POST("customers/{customer_id}/addresses.json")
  suspend fun createCustomerAddress(
    @Path("customer_id") customerId: Long,
    @Body request: CreateCustomerAddressRequest
  ): CustomerAddressResponse



  @Headers( "X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("customers/{customer_id}/addresses.json")
  suspend fun getCustomerAddresses(
    @Path("customer_id") customerId: Long
  ): CustomerAddressesResponse


  @Headers( "X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @GET("customers/{customer_id}.json")
  suspend fun getCustomerById(
    @Path("customer_id") customerId: Long
  ): Response<CustomerResponse>



  @Headers( "X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @PUT("customers/{customer_id}.json")
  suspend fun updateCustomer(
    @Path("customer_id") customerId: Long,
    @Body body: CustomerRequest
  ):Response<CustomerResponse>



  @Headers( "X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
  @PUT("customers/{customer_id}/addresses/{address_id}/default.json")
  suspend fun setDefaultAddress(
    @Path("customer_id") customerId: Long,
    @Path("address_id") addressId: Long):CustomerAddressResponse


}