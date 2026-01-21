package com.example.shopenest.cartscreen.viewmodel


import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopenest.model.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response


class CartViewModel(private val repo: RepositoryInterface) : ViewModel() {


    private val _draftOrder = MutableStateFlow<Response<ResponseDraftOrderForRetrieve>?>(null)

    // Expose as a read-only StateFlo
    val draftOrder: StateFlow<Response<ResponseDraftOrderForRetrieve>?> get() = _draftOrder


    private val _lineItems = MutableStateFlow<List<LineItem>>(emptyList())

    // Expose as a read-only StateFlo
    val lineItems: StateFlow<List<LineItem>> get() = _lineItems


    private val _deleteDraftOrder = MutableStateFlow<Result<Boolean>?>(null)
    val deleteDraftOrder: StateFlow<Result<Boolean>?> get() = _deleteDraftOrder


    fun getDraftOrder() {

        viewModelScope.launch(Dispatchers.IO) {


            _draftOrder.value = repo.getDraftOrders()

        }
    }

    private val _customerId = MutableStateFlow<Long?>(null)

    fun loadDraftOrder(customerId: Long) {
        _customerId.value = customerId
    }

    val draftOrderWithItems: StateFlow<Pair<DraftOrderHeaderEntity?, List<LineItem>>> =
        _customerId
            .filterNotNull()
            .flatMapLatest { id ->
                repo.getDraftOrderWithItems(id)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null to emptyList()
            )

    val lineItemsOnly: StateFlow<List<LineItem>> =
        _customerId
            .filterNotNull()
            .flatMapLatest { id ->
                repo.getLineItems(id)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun setCustomerId(id: Long) {
        _customerId.value = id
    }


    @SuppressLint("SuspiciousIndentation")
    fun deleteDraftOrder(idDraftOrder: Long, customerId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.deleteDraftOrderById(idDraftOrder)

                repo.deleteDraftOrder(idDraftOrder, customerId)

                _deleteDraftOrder.value = Result.success(true)  // ✅ success

                Log.i("draftorderSucc::", "success")

                _deleteDraftOrder.value =
                    Result.failure(Exception("Delete failed: ${response.code()}"))
                Log.i("draftorderfail_1::", "fail")

            } catch (e: Exception) {
                _deleteDraftOrder.value = Result.failure(e)  // ✅ network or parsing error
                Log.i("draftorderfail_2::", "fail")
            }
        }
    }


}