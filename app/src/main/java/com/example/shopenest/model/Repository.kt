package com.example.shopenest.model

import com.example.shopenest.db.LocalSource
import com.example.shopenest.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

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

    override suspend fun getProductsForBrands(id: Long): ShoppingProducts {

        return remoteSource.getProductsForBrands(id)
    }

    override suspend fun getProductsDetails(id: Long): ProductResponse {
        return remoteSource.getProductsDetails(id)
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