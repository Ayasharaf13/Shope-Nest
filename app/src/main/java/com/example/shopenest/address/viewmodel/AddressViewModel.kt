package com.example.shopenest.address.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopenest.model.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response


class AddressViewModel(private val repo: RepositoryInterface) : ViewModel() {


    private val _updateCustomer = MutableSharedFlow<Response<CustomerResponse>>()
    val updateCustomer = _updateCustomer.asSharedFlow()


    private val _createCustomerAddress = MutableSharedFlow<CustomerAddressResponse>()
    val createCustomerAddress = _createCustomerAddress.asSharedFlow()


    private val _address = MutableStateFlow<List<CustomerAddress>?>(null)

    // Expose as a read-only StateFlo
    val address: StateFlow<List<CustomerAddress>?> get() = _address


    private val _addressID = MutableStateFlow<CustomerAddress?>(null)

    // Expose as a read-only StateFlo
    val addressID: StateFlow<CustomerAddress?> get() = _addressID


    init {


    }


    fun getAddresses(customerId: Long) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val res = repo.getCustomerAddresses(customerId)
                _address.value = res.addresses
            } catch (e: HttpException) {
                Log.i(
                    "GetAddressesForCustomer",
                    "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}"
                )
            }
        }

    }


    fun updateCustomer(customerId: Long, body: CustomerRequest) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.updateCustomer(customerId, body)

                // ✅ Update StateFlow with result
                _updateCustomer.emit(response)

                val name = response.body()?.customer?.first_name

                Log.i("name::customerupdate", name.toString())

            } catch (e: HttpException) {
                Log.e(
                    "updateCustomer",
                    "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}"
                )
                //  _updateCustomer.value = null
            } catch (e: Exception) {
                Log.e("updateCustomer", "Unexpected error: ${e.message}")
                // _updateCustomer.value = null
            }
        }

    }


    fun createCustomerAddress(customerId: Long, body: CreateCustomerAddressRequest) {


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.createCustomerAddress(customerId, body)

                // ✅ Update StateFlow with result
                val name = response.customer_address.first_name
                val phone = response.customer_address.phone
                _createCustomerAddress.emit(response)
                Log.e("createCustomerAddressCustomer", "Success: $name $phone ")
            } catch (e: HttpException) {
                Log.e(
                    "createCustomerAddressCustomer",
                    "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}"
                )

            } catch (e: Exception) {
                Log.e("createCustomerAddressCustomer", "Unexpected error: ${e.message}")

            }
        }


    }


    fun setDefaultAddress(customerId: Long, addressId: Long) {
        viewModelScope.launch(Dispatchers.IO) {

            try {

                val response = repo.setDefaultAddress(customerId, addressId)

                // ✅ Success: update StateFlow
                _address.value = listOf(response.customer_address)

                Log.d("DefaultAddress", "Default set to: ${response.customer_address.address1}")

            } catch (e: HttpException) {
                Log.e("DefaultAddress", "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")

            } catch (e: Exception) {
                Log.e("DefaultAddress", "Unexpected: ${e.message}")

            }


        }


    }


}