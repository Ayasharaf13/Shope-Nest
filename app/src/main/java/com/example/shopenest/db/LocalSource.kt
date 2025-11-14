package com.example.shopenest.db



import com.example.shopenest.model.CustomerAddress

import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.Flow


interface LocalSource {

suspend fun getAllFavProducts():Flow<List<Product>>

suspend fun saveProduct (product:Product)
/*
    suspend fun saveAddress(address: CustomerAddress)
    suspend fun getAllAddresses(): Flow<List<CustomerAddress>>
    suspend fun deleteAddress(address: CustomerAddress)
    suspend fun getAddressByIdOnce(addressId: Int): CustomerAddress?


    suspend fun clearDefault()
    suspend fun setDefault(addressId: Long)
    suspend fun getDefaultAddress(): CustomerAddress?

 */






}