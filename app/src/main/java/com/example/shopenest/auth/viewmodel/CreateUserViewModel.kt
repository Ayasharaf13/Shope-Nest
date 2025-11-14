package com.example.shopenest.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopenest.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class CreateUserViewModel  (private val repo: RepositoryInterface) : ViewModel() {




    private val _customer = MutableStateFlow<Response<CustomerResponse>?>(null)

    // Expose as a read-only StateFlo
    val customer: StateFlow<Response<CustomerResponse>?> get() = _customer






   private val _searchCustomer = MutableStateFlow<Response<Customers>?>(null)
    val searchCustomer: StateFlow<Response<Customers>?> get() = _searchCustomer



    private val _countCustomer = MutableStateFlow<CountCustomer?>(null)
    val countCustomer: StateFlow<CountCustomer?> get() = _countCustomer

    private val _deleteCustomer = MutableStateFlow<Unit?>(null)
    val deleteCustomer: StateFlow<Unit?> get() = _deleteCustomer



    fun getCountCustomer (){
        viewModelScope.launch  (Dispatchers.IO){

            try{
                val res = repo.getCountCustomer()
                _countCustomer.value = res
            }catch (e:HttpException){
                Log.i ("countCustmer","HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
            }
        }
    }



    fun deleteCustomer(customerId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = repo.deleteCustomer(customerId)

                if (res.isSuccessful) {
                    // Handle success
                    val res = repo.deleteCustomer(customerId)
                    _deleteCustomer.value = res.body()

                } else {
                    Log.e("deleteCustomer", "Failed: ${res.code()} - ${res.errorBody()?.string()}")
                }
            } catch (e: HttpException) {
                Log.e("deleteCustomer", "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
            } catch (e: Exception) {
                Log.e("deleteCustomer", "Unexpected error: ${e.localizedMessage}")
            }
        }
    }


       fun searchCustomerByEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getCustomerByEmail(email)
                _searchCustomer.value = response
            } catch (e: HttpException) {
                Log.e("SearchCustomer", "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
            } catch (e: Exception) {
                Log.e("SearchCustomer", "Error: ${e.message}")
            }
        }
    }



        fun createCustomer(customer:  CustomerRequest) {

            viewModelScope.launch(Dispatchers.IO) {
                try {

                    val response = repo.createCustomer(customer)
                    _customer.value = response

                } catch (e: HttpException) {//catch (e:Exception){
                    // Log.i("ErrorCreateCustomer",e.message.toString())
                    Log.e(
                        "CreateCustomer",
                        "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}"
                    )
                }
            }
        }




}

