package com.example.shopenest.network

import com.example.shopenest.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//admin/api/2024-10/

object RetrofitClient {
    private const val BASE_URL = "https://itp-sv-and7.myshopify.com/admin/api/2024-10/"

    val retrofitt: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}


class ShoppingClient:RemoteSource{


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
    override suspend fun getBrands(): Brands   {

        return apiService.getBrands()
    }

    override suspend fun getCategory(): Categories {

        return apiService.getCategory()
    }

    override suspend fun getProductsForSectionKidsCategory(): ShoppingProducts {
        return apiService.getProductsForSectionKidsCategory()
    }

    override suspend fun getProductsForSectionWomenCategory(): ShoppingProducts {
    return  apiService.getProductsForSectionWomenCategory()
    }

    override suspend fun getProductsForSectionMenCategory(): ShoppingProducts {

        return apiService.getProductsForSectionManCategory()
    }

    override suspend fun getProductsForBrands(id:Long): ShoppingProducts {

        return apiService.getProductsForBrands(id)
    }

    override suspend fun getProductsDetails(id: Long): ProductResponse {
        return apiService.getProductsDetails(id)
    }


}