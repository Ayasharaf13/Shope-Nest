package com.example.shopenest.model

import com.example.shopenest.db.LocalSource
import com.example.shopenest.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class Repository(remoteSource: RemoteSource, localSource: LocalSource) :RepositoryInterface {

    val remoteSource:RemoteSource = remoteSource
    val localSource:LocalSource =localSource

    override suspend fun getBrands(): Brands {
      return  remoteSource.getBrands()
    }

    override suspend fun getAllFavProducts(): Flow<List<Product>> = flow {

        emitAll(localSource.getAllFavProducts())
    }

    override suspend fun saveProduct(product: Product) {
           localSource.saveProduct(product)
    }

    override suspend fun getCategory(): Categories {

        return remoteSource.getCategory()
    }

    override suspend fun getProductsForSectionKidsCategory(): ShoppingProducts {
        return remoteSource.getProductsForSectionKidsCategory()
    }

    override suspend fun getProductsForSectionWomenCategory(): ShoppingProducts {
       return remoteSource.getProductsForSectionWomenCategory()
    }

    override suspend fun getProductsForSectionMenCategory(): ShoppingProducts {

        return remoteSource.getProductsForSectionMenCategory()
    }

    override suspend fun getProductsForBrands(vendor: String): ShoppingProducts {

        return remoteSource.getProductsForBrands(vendor)
    }

    override suspend fun getProductsDetails(id: Long): ProductResponse {
        return remoteSource.getProductsDetails(id)
    }

    override suspend fun createCustomer(customer: CustomerRequest): Response<CustomerResponse> {

        return remoteSource.createCustomer(customer)
    }

    override suspend fun getCustomerByEmail(email: String): Response<Customers> {
        return remoteSource.getCustomerByEmail(email)
    }

    /*  override suspend fun createCustomer(customer: ResponseCustomerForBody): Response<ResponseCustomer> {
          return remoteSource.createCustomer(customer)
      }

     */


  /*  override suspend fun getCustomerByEmail(email: String): Response<Customers> {
        return remoteSource.getCustomerByEmail(email)
    }

   */

    override suspend fun getCountCustomer(): CountCustomer {
        return remoteSource.getCountCustomer()
    }

    override suspend fun deleteCustomer(customerId: Long): Response<Unit> {
        return remoteSource.deleteCustomer(customerId)
    }


    override suspend fun getAvailableProducts(inventoryItemId: Long): ResponseInventory {
        return remoteSource.getAvailableProducts(inventoryItemId)

    }



    override suspend fun getDiscount(): ResponseDiscount {
        return remoteSource.getDiscount()
    }

    override suspend fun createCartOrder(cartOrder: DraftOrderRequest): Response<ResponseDraftOrderForRequestCreate> {

        return remoteSource.createCartOrder(cartOrder)
    }



    override suspend fun getDraftOrders(): Response<ResponseDraftOrderForRetrieve> {

        return remoteSource.getDraftOrders()
    }


    override suspend fun deleteDraftOrderById(draftOrderId: Long): Response<Unit> {
         return remoteSource.deleteDraftOrderById(draftOrderId)
    }


    override suspend fun getCustomerById(customerId: Long): Response<CustomerResponse> {

        return remoteSource.getCustomerById(customerId)
    }

    override suspend fun updateCustomer(
        customerId: Long,
        body: CustomerRequest
    ): Response<CustomerResponse> {

        return remoteSource.updateCustomer(customerId,body)
    }

    override suspend fun createCustomerAddress(
        customerId: Long,
        request: CreateCustomerAddressRequest
    ): CustomerAddressResponse {

        return remoteSource.createCustomerAddress(customerId,request)
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): CustomerAddressResponse {

        return remoteSource.setDefaultAddress(customerId,addressId)
    }

    override suspend fun getCustomerAddresses(customerId: Long): CustomerAddressesResponse {

       return remoteSource.getCustomerAddresses(customerId)
    }

    /*   override suspend fun setDefaultAddress(
           customerId: Long,
           addressId: Long
       ): CustomerAddressResponse {

           return remoteSource.setDefaultAddress(customerId,addressId)
       }


       override suspend fun saveAddress(address: CustomerAddress) {

           localSource.saveAddress(address)
       }

       override suspend fun getAllAddresses(): Flow<List<CustomerAddress>> {

           return localSource.getAllAddresses()
       }

       override suspend fun deleteAddress(address: CustomerAddress) {
          localSource.deleteAddress(address)

       }

       override suspend fun getAddressByIdOnce(addressId: Int): CustomerAddress? {

          return localSource.getAddressByIdOnce(addressId)
       }

       override suspend fun clearDefault() {
          localSource.clearDefault()
       }

       override suspend fun setDefault(addressId: Long) {
         localSource.setDefault(addressId)
       }

       override suspend fun getDefaultAddress(): CustomerAddress? {

           return localSource.getDefaultAddress()
       }*/

    override suspend fun completeDraftOrder(
        draftOrderId: Long,
        paymentPending: Boolean
    ): Response<ResponseDraftOrderForRequestCreate> {
        return remoteSource.completeDraftOrder(draftOrderId,paymentPending)
    }


    companion object{
        private var instance :Repository? = null
        fun getInstance(remoteSource: RemoteSource,localSource: LocalSource):Repository{
            return  instance?: synchronized(this){

                val temp = Repository(remoteSource, localSource)
                instance =temp
                temp
            }

        }
    }
}