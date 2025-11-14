package com.example.shopenest.cartscreen.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.shopenest.model.RepositoryInterface
import com.example.shopenest.model.ResponseDraftOrderForRequestCreate
import com.example.shopenest.model.ResponseDraftOrderForRetrieve
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class CartViewModel  (private val repo: RepositoryInterface) : ViewModel()   {


    private val _draftOrder = MutableStateFlow<Response<ResponseDraftOrderForRetrieve>?>(null)

    // Expose as a read-only StateFlo
    val draftOrder: StateFlow<Response<ResponseDraftOrderForRetrieve>?> get() = _draftOrder


    private val _deleteDraftOrder = MutableStateFlow<Result<Boolean>?>(null)
    val deleteDraftOrder: StateFlow<Result<Boolean>?> get() = _deleteDraftOrder


    fun getDraftOrder ( ) {

        viewModelScope.launch(Dispatchers.IO) {


            _draftOrder.value = repo.getDraftOrders()

        }
    }


    fun deleteDraftOrder(idDraftOrder: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.deleteDraftOrderById(idDraftOrder)
                if (response.isSuccessful) {
                    _deleteDraftOrder.value = Result.success(true)  // ✅ success
                } else {
                    _deleteDraftOrder.value = Result.failure(Exception("Delete failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                _deleteDraftOrder.value = Result.failure(e)  // ✅ network or parsing error
            }
        }
    }




}