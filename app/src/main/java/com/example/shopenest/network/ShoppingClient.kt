package com.example.shopenest.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import com.example.shopenest.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import okhttp3.*
import okhttp3.internal.http2.Http2Connection
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



object RetrofitClient {


    val errorInterceptor = Interceptor { chain ->
        try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            Log.e("API_ERROR", "Request failed: ${chain.request().url}", e)
            throw e
        }
    }


    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        .addInterceptor(errorInterceptor)
        .connectTimeout(50, TimeUnit.SECONDS)
        .readTimeout(50, TimeUnit.SECONDS)
        .build()


    private const val BASE_URL = "https://itp-sv-and7.myshopify.com/admin/api/2024-10/"

    val retrofitt: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}


class ShoppingClient : RemoteSource {


    companion object {

        private var remoteSource: RemoteSource? = null

        fun getInstance(): ShoppingClient {
            if (remoteSource == null) {
                remoteSource = ShoppingClient()
            }
            return remoteSource as ShoppingClient
        }
    }


    val apiService: ShoppingService by lazy {
        RetrofitClient.retrofitt.create(ShoppingService::class.java)


    }

    override suspend fun getBrands(): Brands {

        return apiService.getBrands()
    }

    override suspend fun getCategory(): Categories {

        return apiService.getCategory()
    }

    override suspend fun getProductsForSectionKidsCategory(): ShoppingProducts {
        return apiService.getProductsForSectionKidsCategory()
    }

    override suspend fun getProductsForSectionWomenCategory(): ShoppingProducts {
        return apiService.getProductsForSectionWomenCategory()
    }

    override suspend fun getProductsForSectionMenCategory(): ShoppingProducts {

        return apiService.getProductsForSectionManCategory()
    }

    override suspend fun getProductsForBrands(vendor: String): ShoppingProducts {

        return apiService.getProductsForBrands(vendor)
    }

    override suspend fun getProductsDetails(id: Long): ProductResponse {
        return apiService.getProductsDetails(id)
    }

    override suspend fun createCustomer(customer: CustomerRequest): Response<CustomerResponse> {

        return apiService.createCustomer(customer)
    }

    override suspend fun getCustomerByEmail(email: String): Response<Customers> {
        return apiService.getCustomerByEmail(email)
    }


    override suspend fun getCountCustomer(): CountCustomer {
        return apiService.getCountCustomer()
    }

    override suspend fun deleteCustomer(customerId: Long): Response<Unit> {
        return apiService.deleteCustomer(customerId)
    }

    override suspend fun getAvailableProducts(inventoryItemId: Long): ResponseInventory {
        return apiService.getAvailableProducts(inventoryItemId)
    }

    override suspend fun getDiscount(): ResponseDiscount {

        return apiService.getDiscount()

    }

    override suspend fun createCartOrder(cartOrder: DraftOrderRequest): Response<ResponseDraftOrderForRequestCreate> {

        return apiService.createCartOrder(cartOrder)
    }

    override suspend fun getDraftOrders(): Response<ResponseDraftOrderForRetrieve> {

        return apiService.getDraftOrders()
    }


    override suspend fun deleteDraftOrderById(draftOrderId: Long): Response<Unit> {

        return apiService.deleteDraftOrderById(draftOrderId)
    }


    override suspend fun getCustomerById(customerId: Long): Response<CustomerResponse> {
        return apiService.getCustomerById(customerId)
    }

    override suspend fun updateCustomer(
        customerId: Long,
        body: CustomerRequest
    ): Response<CustomerResponse> {

        return apiService.updateCustomer(customerId, body)
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): CustomerAddressResponse {

        return apiService.setDefaultAddress(customerId, addressId)
    }

    override suspend fun createCustomerAddress(
        customerId: Long,
        request: CreateCustomerAddressRequest
    ): CustomerAddressResponse {

        return apiService.createCustomerAddress(customerId, request)
    }

    override suspend fun getCustomerAddresses(customerId: Long): CustomerAddressesResponse {

        return apiService.getCustomerAddresses(customerId)
    }


    override suspend fun completeDraftOrder(
        draftOrderId: Long,
        paymentPending: Boolean
    ): Response<ResponseDraftOrderForRequestCreate> {

        return apiService.completeDraftOrder(draftOrderId, paymentPending)
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        body: DraftOrderUpdateRequest
    ): Response<ResponseDraftOrderForRequestCreate> {

        return apiService.updateDraftOrder(draftOrderId, body)

    }


}