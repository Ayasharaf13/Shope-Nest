package com.example.shopenest.db

import android.annotation.SuppressLint
import android.content.Context

import com.example.shopenest.model.CustomerAddress

import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


class ConcreteLocalSource :LocalSource{

    val context:Context
    var shoppingDao:ShoppingDao
  //  var addressDao:AddressDao

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
      //  addressDao = db.getAddressdDao()


    }

    override suspend fun getAllFavProducts(): Flow<List<Product>> = flow {

        emitAll(shoppingDao.getAllSavedProducts())
    }

    override suspend fun saveProduct(product: Product) {

        shoppingDao.saveProduct(product)
    }



  /*  override suspend fun saveAddress(address: CustomerAddress) {
      addressDao.insert(address)
    }

    override suspend fun getAllAddresses(): Flow<List<CustomerAddress>> = flow {
        emitAll(addressDao.getAllAddresses())
    }

    override suspend fun deleteAddress(address: CustomerAddress) {

        addressDao.delete(address)
    }

    override suspend fun getAddressByIdOnce(addressId: Int): CustomerAddress? {

        return addressDao.getAddressByIdOnce(addressId)

    }

    override suspend fun clearDefault() {
      addressDao.clearDefault()
    }

    override suspend fun setDefault(addressId: Long) {
      addressDao.setDefault(addressId)
    }


    override suspend fun getDefaultAddress(): CustomerAddress? {

    return  addressDao.getDefaultAddress()

    }*/




}