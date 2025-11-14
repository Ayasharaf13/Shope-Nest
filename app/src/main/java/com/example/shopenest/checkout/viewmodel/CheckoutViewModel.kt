package com.example.shopenest.checkout.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopenest.model.CustomerResponse
import com.example.shopenest.model.RepositoryInterface
import com.example.shopenest.model.ResponseDraftOrderForRequestCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class CheckoutViewModel  (private val repo: RepositoryInterface) : ViewModel()   {


    val needUpdateCustomer = MutableStateFlow(false)


    fun setUpdate(update:Boolean){
        needUpdateCustomer.value = update
    }


    private val _draftOrder = MutableStateFlow<Response<ResponseDraftOrderForRequestCreate>?>(null)

    // Expose as a read-only StateFlo
    val draftOrder: StateFlow<Response<ResponseDraftOrderForRequestCreate>?> get() = _draftOrder



    private val _customerResponse = MutableStateFlow<Response<CustomerResponse>?>(null)

    // Expose as a read-only StateFlo
    val customerResponse: StateFlow<Response<CustomerResponse>?> get() = _customerResponse




    fun completeDraftOrder(draftOrderId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _draftOrder.value = repo.completeDraftOrder(draftOrderId)
            } catch (e: Exception) {
                _draftOrder.value = null
            }
        }


    }

    fun getCustomerInfo (customerId:Long){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _customerResponse.value = repo.getCustomerById(customerId)
                val result = repo.getCustomerById(customerId)
                Log.i("resultIfo::",result.body()?.customer?.first_name.toString())
            } catch (e: Exception) {
                _customerResponse.value = null
            }
        }

    }





}