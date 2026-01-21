package com.example.shopenest.search.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopenest.model.Product
import com.example.shopenest.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repo: RepositoryInterface) : ViewModel() {


    var titleBrand: String? = null

    private val _filterProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())


    val filterProducts: StateFlow<List<Product>> get() = _filterProducts


    // Replace MutableLiveData with MutableStateFlow
    private val _product: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())

    // Expose as a read-only StateFlow
    val product: StateFlow<List<Product>> get() = _product


    private val _allProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())


    val allProducts: StateFlow<List<Product>> get() = _allProducts


    fun filterProductsBasedPrice(rangePrice1: Float, rangePrice2: Float, title: String) {
        viewModelScope.launch(Dispatchers.IO) {


            var pro2 = repo.getProductsForBrands(title).products


            val all = pro2
            Log.i("prouctfffiltAll:  ", all.toString())
            Log.i("prouctfffiltAllTttt :  ", title)

            val filtered = all.filter {

                it.variants.firstOrNull()?.price?.toFloatOrNull()?.let { price ->
                    price in rangePrice1..rangePrice2

                } ?: false


            }

            Log.i("prouctfffilter:  ", filtered.toString())
            _allProducts.value = all
            _filterProducts.value = filtered

        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun getProductsBrand(vendor: String) {

        viewModelScope.launch(Dispatchers.IO) {

            val res = repo.getProductsForBrands(vendor).products
            Log.d("DEBUG", "Fetched products: $res")
            _product.value = res

        }

    }

    fun searchAllProducts(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //  _filterProducts.value = (emptyList()) // ✅ Clear previous result

            val women = repo.getProductsForSectionWomenCategory().products
            val kids = repo.getProductsForSectionKidsCategory().products
            val men = repo.getProductsForSectionMenCategory().products

            val all = women + kids + men

            val filtered = all.filter {
                it.title.trim().contains(query, ignoreCase = true)
            }


            _allProducts.value = all
            _filterProducts.value = filtered

        }
    }


    fun getProductKids() {

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


}

