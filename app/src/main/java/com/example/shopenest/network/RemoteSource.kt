package com.example.shopenest.network

import com.example.shopenest.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface RemoteSource {

    suspend fun getBrands(): Brands
    suspend fun getCategory(): Categories
    suspend fun getProductsForSectionKidsCategory(): ShoppingProducts
    suspend fun getProductsForSectionWomenCategory(): ShoppingProducts
    suspend fun getProductsForSectionMenCategory(): ShoppingProducts
    suspend fun getProductsForBrands(vendor: String): ShoppingProducts
    suspend fun getProductsDetails(id: Long): ProductResponse

    suspend fun createCustomer(@Body customer: CustomerRequest): Response<CustomerResponse>
    suspend fun getCustomerByEmail(@Query("query") email: String): Response<Customers>
    suspend fun getCountCustomer(): CountCustomer
    suspend fun deleteCustomer(@Path("id") customerId: Long): Response<Unit>
    suspend fun getAvailableProducts(@Query("inventory_item_ids") inventoryItemId: Long): ResponseInventory
    suspend fun getDiscount(): ResponseDiscount
    suspend fun createCartOrder(@Body cartOrder: DraftOrderRequest): Response<ResponseDraftOrderForRequestCreate>
    suspend fun getDraftOrders(): Response<ResponseDraftOrderForRetrieve>
    suspend fun deleteDraftOrderById(@Path("id") draftOrderId: Long): Response<Unit>
    suspend fun getCustomerById(@Path("customer_id") customerId: Long): Response<CustomerResponse>

    suspend fun updateCustomer(
        @Path("customer_id") customerId: Long,
        @Body body: CustomerRequest
    ): Response<CustomerResponse>

    suspend fun setDefaultAddress(
        @Path("customer_id") customerId: Long,
        @Path("address_id") addressId: Long
    ): CustomerAddressResponse

    suspend fun createCustomerAddress(
        @Path("customer_id") customerId: Long,
        @Body request: CreateCustomerAddressRequest
    ): CustomerAddressResponse


    suspend fun getCustomerAddresses(
        @Path("customer_id") customerId: Long
    ): CustomerAddressesResponse

    suspend fun completeDraftOrder(
        @Path("draft_order_id") draftOrderId: Long,
        @Query("payment_pending") paymentPending: Boolean = true
    ): Response<ResponseDraftOrderForRequestCreate>


    suspend fun updateDraftOrder(
        @Path("draftOrder_id") draftOrderId: Long,
        @Body body: DraftOrderUpdateRequest
    ): Response<ResponseDraftOrderForRequestCreate>


}