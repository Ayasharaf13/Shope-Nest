package com.example.shopenest.homescreen.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


class HomeViewModel(private val repo: RepositoryInterface) : ViewModel() {


    private val _draftOrder = MutableStateFlow<Response<ResponseDraftOrderForRequestCreate>?>(null)

    // Expose as a read-only StateFlo
    val draftOrder: StateFlow<Response<ResponseDraftOrderForRequestCreate>?> get() = _draftOrder


    private val _updateDiscount = MutableStateFlow<AppliedDiscount?>(null)
    val appliedDiscount = _updateDiscount


    suspend fun saveDraftOrder(header: DraftOrderHeaderEntity, items: List<LineItem>) {
        repo.saveDraftOrderWithItems(header, items)
    }


    fun saveDraftOrder(draft: DraftOrderHeaderEntity) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repo.saveDraftOrderHeader(draft)
            }
        } catch (e: Exception) {
            Log.i("ErrorSavingDrafOrder", e.message.toString())

        }
    }


    fun saveLineItem(itemsDraftOrder: List<LineItem>) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repo.saveLineItems(itemsDraftOrder)
            }
        } catch (e: Exception) {
            Log.i("ErrorSavingLineItems", e.message.toString())

        }
    }


    fun deleteDraft(draftId: Long, customerId: Long) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repo.deleteDraftOrder(draftId, customerId)
            }
        } catch (e: Exception) {
            Log.i("ErrorSavingLineItems", e.message.toString())

        }
    }


    fun mapToDraftOrderHeaderEntity(api: DraftOrder, customerId: Long): DraftOrderHeaderEntity {
        return DraftOrderHeaderEntity(
            draftOrderId = api.idDraftOrder,

            email = api.email,
            taxes_included = api.taxes_included,
            currency = api.currency,

            created_at = api.created_at,
            updated_at = api.updated_at,
            tax_exempt = api.tax_exempt,
            completed_at = api.completed_at,
            name = api.name,
            allow_discount_codes_in_checkout = api.allow_discount_codes_in_checkout,
            status = api.status,

            customerId = customerId, // (from SharedPreferences)
            totalPrice = api.total_price,
            subtotalPrice = api.subtotal_price,

            createdAt = api.created_at,     // duplicated fields?
            updatedAt = api.updated_at,

            shipping_address = api.shipping_address,
            billing_address = api.billing_address,
            invoice_url = api.invoice_url,
            applied_discount = api.applied_discount ?: AppliedDiscount(
                title = "",
                value = "0",
                value_type = "percentage",
                amount = "0"
            ),

            //applied_discount = api.applied_discount,
            order_id = api.order_id,

            tags = api.tags,
            total_price = api.total_price,
            subtotal_price = api.subtotal_price,
            total_tax = api.total_tax,

            default_address = api.default_address,

            api_client_id = api.admin_graphql_api_id,


            )


    }


    fun updateDiscount(draftOrderId: Long, body: DraftOrderUpdateRequest) {

        viewModelScope.launch {
            val response = repo.updateDraftOrder(draftOrderId, body)

            if (response.isSuccessful) {
                _updateDiscount.value = response.body()?.draft_order?.applied_discount
                Log.i("SHOPIFY", "Discount applied successfully")
            } else {
                Log.e("SHOPIFY", "Failed: ${response.errorBody()?.string()}")
            }

        }
    }


    private var cou = 0

    // Replace MutableLiveData with MutableStateFlow
    private val _product: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())

    // Expose as a read-only StateFlow
    val product: StateFlow<List<Product>> get() = _product


    // Replace MutableLiveData with MutableStateFlow
    private val _inventory: MutableStateFlow<Int?> = MutableStateFlow(null)

    // Expose as a read-only StateFlow
    val inventory: StateFlow<Int?> get() = _inventory


    private val inventoryMap = mutableMapOf<Long, Int>()

    private val _inventoryFlow = MutableLiveData<MutableMap<Long, Int>>()
    val inventoryFlow: LiveData<MutableMap<Long, Int>> = _inventoryFlow

    private val _inventoryFlowDB = MutableLiveData<Int>()
    val inventoryFlowDB: LiveData<Int> = _inventoryFlowDB




    fun getInventory(productId: Long): Int {

        Log.i("newIncreasegetttt", inventoryMap[productId].toString())
        return inventoryMap[productId] ?: 0
    }

    fun saveInventory(productId: Long, count: Int) {

        inventoryMap[productId] = count
        // emit update
        Log.i("newIncreaseSave", inventoryMap[productId].toString())
    }


    fun increaseInventory(productId: Long) {
        //  val newValue = getInventory(productId) + 1

        val newValue = (inventoryMap[productId] ?: 0) + 1  //getInventory(productId) + 1
        inventoryMap[productId] = newValue
        _inventoryFlow.value = inventoryMap
        Log.i("newIncrease", newValue.toString())
        Log.i("newIncreaseInve", inventoryMap[productId].toString())
        Log.i("newIncreaseIdddd", productId.toString())

        saveInventory(productId, newValue)

    }

    fun decreaseInventory(productId: Long) {
        val newValue = ((inventoryMap[productId]
            ?: 0) - 1).coerceAtLeast(0)//(getInventory(productId) - 1).coerceAtLeast(0)

        saveInventory(productId, newValue)
    }


    fun increaseItem(lineItemId: Long, customerId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.increaseQuantity(lineItemId, customerId)

        }
    }

    fun decreaseItem(lineItemId: Long, customerId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.decreaseQuantity(lineItemId, customerId)
        }
    }


    private val _discount: MutableStateFlow<String?> = MutableStateFlow("")

    // Expose as a read-only StateFlow
    val discount: StateFlow<String?> get() = _discount


    private val _filterProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())


    val filterProducts: StateFlow<List<Product>> get() = _filterProducts


    private val _allProducts: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())


    val allProducts: StateFlow<List<Product>> get() = _allProducts


    // Replace MutableLiveData with MutableStateFlow
    private val _productDetails: MutableStateFlow<ProductResponse> =
        MutableStateFlow(ProductResponse(Product()))

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


    fun getDiscount() {

        viewModelScope.launch(Dispatchers.IO) {


            try {
                var code = repo.getDiscount().discount_codes.get(0)?.code
                _discount.value = code


            } catch (e: Exception) {
                Log.i("ErrorRetrieveDiscountCode", e.message.toString())
            }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun getBrands() {

        var brands: List<SmartCollection>
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


        Log.i("filtterbrandsQuerrry", query)
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


    fun getProductsBrand(vendor: String) {

        viewModelScope.launch(Dispatchers.IO) {

            _product.value = repo.getProductsForBrands(vendor).products
        }

    }


    fun getAvailableProducts(inventoryItemId: Long) {

        viewModelScope.launch(Dispatchers.IO) {

            _inventory.value =
                repo.getAvailableProducts(inventoryItemId).inventory_levels.firstOrNull()?.available


        }

    }


    suspend fun getProductDetails(id: Long): ProductResponse {

        viewModelScope.launch(Dispatchers.IO) {

            _productDetails.value = repo.getProductsDetails(id)

        }
        return repo.getProductsDetails(id)

    }


    suspend fun createDraftOrder(request: DraftOrderRequest)
            : Response<ResponseDraftOrderForRequestCreate> {
        return repo.createCartOrder(request)
    }


}