package com.example.shopenest.favouritescreen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopenest.model.CustomerRef
import com.example.shopenest.model.Product
import com.example.shopenest.model.RepositoryInterface
import com.example.shopenest.utilities.CustomerPref
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FavViewModel(private val repo: RepositoryInterface,  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    // Expose as a read-only StateFlo
    val products: StateFlow<List<Product>>? get() = _products

    // 💡 تعريف الـ StateFlow الخاص بالأخطاء
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun getAllProducts(customerId: Long) {
       // viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch(ioDispatcher) {
            repo.getAllFavProducts(customerId).collect { list ->
                _products.value = list
            }
        }
    }


    fun saveProduct(product: Product) {
        viewModelScope.launch(ioDispatcher) {

            try {
                repo.saveProduct(product)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }



        }
    }


    fun deleteProduct(productId: Long, customerId: Long) {

        viewModelScope.launch(ioDispatcher) {

            try {
                repo.deleteById(productId, customerId)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }

        }
    }

}