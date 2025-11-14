package com.example.shopenest.db


/*
import androidx.room.*

import com.example.shopenest.model.CustomerAddress

import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull


@Dao
interface AddressDao {


 /////////error mydefaultAddress
    @Query("UPDATE addresses SET is_default = 0")
    suspend fun clearDefault()

    @Query("UPDATE addresses SET is_default= 1 WHERE id = :addressId")
    suspend fun setDefault(addressId: Long)

    @Query("SELECT * FROM addresses WHERE is_default= 1 LIMIT 1")
    suspend fun getDefaultAddress(): CustomerAddress?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @NotNull
    suspend fun insert(address:CustomerAddress)

    @Query("SELECT * FROM addresses")
    fun getAllAddresses(): Flow<List<CustomerAddress>>

    @Query("SELECT * FROM addresses WHERE id = :addressId")
    suspend fun getAddressByIdOnce(addressId: Int): CustomerAddress?

    @Delete
    suspend fun delete(address: CustomerAddress)


}

 */


