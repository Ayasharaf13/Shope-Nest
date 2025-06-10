package com.example.shopenest.db

import android.annotation.SuppressLint
import android.content.Context
import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


class ConcreteLocalSource :LocalSource{

    val context:Context
    var shoppingDao:ShoppingDao

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var localsource: ConcreteLocalSource? = null

        fun  getInstance(con: Context):ConcreteLocalSource{
            if(localsource == null){
                localsource = ConcreteLocalSource(con)
            }
            return localsource as ConcreteLocalSource
        }

    }

    private constructor (con: Context) {

        this.context = con
        val db: AppDataBase = AppDataBase.getInstance(context.applicationContext)
        shoppingDao = db.getProdDao()


    }
    override suspend fun getAllFavProducts(): Flow<List<Product>> = flow {

        emitAll(shoppingDao.getAllSavedProducts())
    }

    override suspend fun saveProduct(product: Product) {

        shoppingDao.saveProduct(product)
    }



}