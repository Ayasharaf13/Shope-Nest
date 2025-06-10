package com.example.shopenest.homescreen.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopenest.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel (private val repo: RepositoryInterface) :ViewModel(){

    // Replace MutableLiveData with MutableStateFlow
    private val _product: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())

    // Expose as a read-only StateFlow
    val product: StateFlow<List<Product>> get() = _product


    // Replace MutableLiveData with MutableStateFlow
    private val _productDetails: MutableStateFlow<ProductResponse> = MutableStateFlow(ProductResponse(Product()))

    // Expose as a read-only StateFlow
    val productDetails: StateFlow<ProductResponse> get() = _productDetails




    // Replace MutableLiveData with MutableStateFlow
    private val _brand: MutableStateFlow<List<SmartCollection>> = MutableStateFlow(emptyList())

    // Expose as a read-only StateFlow
    val brand: StateFlow<List<SmartCollection>> get() = _brand


init {
    Log.i("HomeViewModel", "Init called")
    getBrands()
}
    fun getAllFavProduct(){

      viewModelScope.launch(Dispatchers.IO) {
          try {

              repo.getAllFavProducts().collect {
                  _product.value = it
              }
          }catch (e:Exception){
              Log.i("ErrorRetrieveProd",e.message.toString())
          }
      }
    }


    fun saveProduct (product: Product) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repo.saveProduct(product)
            }
        } catch (e: Exception) {
            Log.i("ErrorSaving",e.message.toString())

        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun getBrands() {

        var brands:List<SmartCollection>
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("getBrands first", "Updated StateFlow with")


            brands = repo.getBrands().smart_collections
            Log.i("getBrands sec", "Updated StateFlow with Beforeeee ${brands.size}")
          //  withContext(Dispatchers.Main) {
                _brand.value = brands


                Log.i("getBrands th", "Updated StateFlow with ${brands.size} items")
         //   }


            // Log all brands in the list
            if (brands.isNotEmpty()) {
                brands.forEach { brand ->
                    Log.i("successBrands", "Brand: $brand")

                }

            }



        }


    }
    fun filterBrands(query: String) {
        val filtered = if (query.isBlank()) {

            _brand.value

        } else {

            _brand.value.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }

        _brand.value = filtered


        Log.i("filtterbrandsQuerrry",query)
    }

    fun getProductKids () {

        viewModelScope.launch(Dispatchers.IO) {


            _product.value = repo.getProductsForSectionKidsCategory().products
        }
    }

        fun getProductWomen() {

            viewModelScope.launch(Dispatchers.IO) {


                _product.value = repo.getProductsForSectionWomenCategory().products
            }


        }



    fun getProductMen() {

        viewModelScope.launch(Dispatchers.IO) {


            _product.value = repo.getProductsForSectionMenCategory().products
        }


    }


        fun getProductsBrand(id:Long) {

            viewModelScope.launch(Dispatchers.IO) {


                _product.value = repo.getProductsForBrands(id).products
            }


    }

    fun getProductDetails(id:Long) {

        viewModelScope.launch(Dispatchers.IO) {

           _productDetails.value = repo.getProductsDetails(id)
        }


    }



}