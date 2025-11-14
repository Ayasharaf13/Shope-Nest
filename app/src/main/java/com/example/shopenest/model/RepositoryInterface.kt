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
    suspend fun createCustomer (@Body customer: CustomerRequest): Response<CustomerResponse>
    suspend fun getCustomerByEmail(@Query("query") email: String): Response<Customers>
    suspend fun getCountCustomer():CountCustomer
    suspend fun deleteCustomer ( @Path("id") customerId: Long):Response<Unit>
    suspend fun getAvailableProducts(@Query("inventory_item_ids") inventoryItemId: Long): ResponseInventory
    suspend fun getDiscount(): ResponseDiscount
    suspend fun createCartOrder(@Body cartOrder: DraftOrderRequest): Response<ResponseDraftOrderForRequestCreate>
    suspend fun getDraftOrders():Response<ResponseDraftOrderForRetrieve>
    suspend fun deleteDraftOrderById ( @Path("id") draftOrderId: Long):Response<Unit>

    suspend fun getCustomerById( @Path("customer_id") customerId: Long): Response<CustomerResponse>
    suspend fun updateCustomer( @Path("customer_id") customerId: Long,  @Body body: CustomerRequest):Response<CustomerResponse>

    suspend fun createCustomerAddress(  @Path("customer_id") customerId: Long, @Body request: CreateCustomerAddressRequest
    ): CustomerAddressResponse




    /*  suspend fun setDefaultAddress(  @Path("customer_id") customerId: Long,  @Path("address_id") addressId: Long):CustomerAddressResponse


      suspend fun saveAddress(address: CustomerAddress)
      suspend fun getAllAddresses(): Flow<List<CustomerAddress>>
      suspend fun deleteAddress(address: CustomerAddress)
      suspend fun getAddressByIdOnce(addressId: Int): CustomerAddress?



      suspend fun clearDefault()
      suspend fun setDefault(addressId: Long)
      suspend fun getDefaultAddress(): CustomerAddress?

  */
    suspend fun setDefaultAddress(
        @Path("customer_id") customerId: Long,
        @Path("address_id") addressId: Long):CustomerAddressResponse


    suspend fun getCustomerAddresses(
        @Path("customer_id") customerId: Long
    ): CustomerAddressesResponse

    suspend fun completeDraftOrder(
        @Path("draft_order_id") draftOrderId: Long,
        @Query("payment_pending") paymentPending: Boolean = false
    ): Response<ResponseDraftOrderForRequestCreate>


}