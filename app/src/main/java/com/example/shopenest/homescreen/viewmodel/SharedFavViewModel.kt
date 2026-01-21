package com.example.shopenest.homescreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class SharedFavViewModel : ViewModel() {


    private val _selectedProduct = MutableStateFlow<Product?>(null)


    val selectedProduct = _selectedProduct.asStateFlow()


    fun passProductToFav(product: Product) {
        _selectedProduct.value = product
    }

    // دالة تصفير الحالة (Clear State)
    fun clearProduct() {
        _selectedProduct.value = null
    }
}