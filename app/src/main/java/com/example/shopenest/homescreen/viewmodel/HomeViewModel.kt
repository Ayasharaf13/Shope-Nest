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
import retrofit2.HttpException
import retrofit2.Response


class HomeViewModel (private val repo: RepositoryInterface) :ViewModel(){



    private val _draftOrder = MutableStateFlow<Response<ResponseDraftOrderForRequestCreate>?>(null)

    // Expose as a read-only StateFlo
    val draftOrder: StateFlow<Response<ResponseDraftOrderForRequestCreate>?> get() = _draftOrder



    // var countter =0

    // This map will store inventory values for each product id
    val inventoryMap = mutableMapOf<Long, Int>()



    fun saveInventory(productId: Long, count: Int) {
        inventoryMap[productId] = count
    }

    fun getInventory(productId: Long?): Int? {
        return inventoryMap[productId] ?:0
    }

    fun increaseInventory(productId: Long) {
        val current = inventoryMap[productId] ?: 0
        var currentIncrease = current +1
        inventoryMap[productId] = currentIncrease //current + 1
        saveInventory(productId,currentIncrease)
    }

    fun decreaseInventory(productId: Long) {
        val current = inventoryMap[productId] ?: 0
   //  if (current > 0)
            var currentDecrease = current-1
            inventoryMap[productId] = currentDecrease //current - 1
            saveInventory(productId,currentDecrease)
    }

    // Replace MutableLiveData with MutableStateFlow
    private val _product: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())

    // Expose as a read-only StateFlow
    val product: StateFlow<List<Product>> get() = _product


    // Replace MutableLiveData with MutableStateFlow
    private val _inventory: MutableStateFlow<Int?> = MutableStateFlow(null)

    // Expose as a read-only StateFlow
    val inventory: StateFlow<Int?> get() = _inventory


    private val _discount: MutableStateFlow<String?> = MutableStateFlow("")

    // Expose as a read-only StateFlow
    val discount: StateFlow<String?> get() = _discount




    private val _filterProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())


    val filterProducts: StateFlow<List<Product>> get() = _filterProducts


    private val _allProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())


    val allProducts: StateFlow<List<Product>> get() = _allProducts


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


   fun getDiscount(){

        viewModelScope.launch(Dispatchers.IO) {


            try {
             var code =   repo.getDiscount().discount_codes.get(0)?.code
                _discount.value = code


            }catch (e:Exception){
                Log.i("ErrorRetrieveDiscountCode",e.message.toString())
            }
        }
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



    fun getProductKids ( ) {

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


        fun getProductsBrand(vendor:String) {

            viewModelScope.launch(Dispatchers.IO) {

                _product.value = repo.getProductsForBrands(vendor).products
            }

    }


    fun getAvailableProducts(inventoryItemId:Long) {

        viewModelScope.launch(Dispatchers.IO) {

            _inventory.value = repo.getAvailableProducts(inventoryItemId).inventory_levels.firstOrNull()?.available



        }

    }



    fun getProductDetails(id:Long) {

        viewModelScope.launch(Dispatchers.IO) {

           _productDetails.value = repo.getProductsDetails(id)
        }


    }






    fun createDraftOrder(draftOrderRequest: DraftOrderRequest) {

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = repo.createCartOrder(draftOrderRequest)
                _draftOrder.value = response
                 Log.i("createDraftOrder::: ",response.body()?.draft_order?.total_price.toString())
                Log.i("createDraftOrderId::: ",response.body()?.draft_order?.id.toString())

            } catch (e: HttpException) {

                Log.e(
                    "CreateDraftOrder",
                    "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}"
                )
            }
        }
    }



    /*
    <com.paypal.checkout.paymentbutton.PaymentButtonContainer
    android:id="@+id/payment_button_container"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:paypal_button_color="silver"
    app:paypal_button_label="pay"
    app:paypal_button_shape="rectangle"
    app:paypal_button_size="large"
    app:paypal_button_enabled="true" />
     */




}