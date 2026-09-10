package com.example.shopenest.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


class FakeRepo:RepositoryInterface {

    private var brandsData: Brands? = null
    private val favProductsList = mutableListOf<Product>()
    var shouldReturnError: Boolean = false



    // 💡 دالة مساعدة للتست فقط لتهيئة البيانات المبدئية
    fun setFavProducts(products: List<Product>) {
        favProductsList.clear()
        favProductsList.addAll(products)
    }

    override suspend fun getBrands(): Brands {

        if (shouldReturnError) {
            throw Exception("Failed to fetch brands from network")
        }
        // إرجاع البيانات المخزنة في الذاكرة أو كائن افتراضي فارغ
        return brandsData ?: Brands(smart_collections = emptyList())
    }


    override suspend fun getAllFavProducts(customerId: Long): Flow<List<Product>> = flow{

        if (shouldReturnError) {
            throw Exception("Database Error fetching favorites")
        }
        // تصفية المنتجات في الذاكرة أو إرجاع القائمة
        emit(favProductsList)

    }


    override suspend fun saveProduct(product: Product) {

        if (shouldReturnError) {
            throw Exception("Database Error saving product")
        }

        favProductsList.add(product)
    }

    override suspend fun getCategory(): Categories {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsForSectionKidsCategory(): ShoppingProducts {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsForSectionWomenCategory(): ShoppingProducts {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsForSectionMenCategory(): ShoppingProducts {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsForBrands(vendor: String): ShoppingProducts {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsDetails(id: Long): ProductResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomer(customer: CustomerRequest): Response<CustomerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerByEmail(email: String): Response<Customers> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountCustomer(): CountCustomer {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCustomer(customerId: Long): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAvailableProducts(inventoryItemId: Long): ResponseInventory {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscount(): ResponseDiscount {
        TODO("Not yet implemented")
    }

    override suspend fun createCartOrder(cartOrder: DraftOrderRequest): Response<ResponseDraftOrderForRequestCreate> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDraftOrderById(draftOrderId: Long): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerById(customerId: Long): Response<CustomerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCustomer(
        customerId: Long,
        body: CustomerRequest
    ): Response<CustomerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomerAddress(
        customerId: Long,
        request: CreateCustomerAddressRequest
    ): CustomerAddressResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(productId: Long, customerId: Long) {
        if (shouldReturnError) {
            throw Exception("Database Error deleting product")
        }

        // إزالة المنتج من القائمة بناءً على الـ productId
        favProductsList.removeAll { product -> product.id == productId }
    }

    override suspend fun saveDraftOrderHeader(header: DraftOrderHeaderEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun saveLineItems(items: List<LineItem>) {
        TODO("Not yet implemented")
    }

    override fun getLineItems(customerId: Long): Flow<List<LineItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long, customerId: Long) {
        TODO("Not yet implemented")
    }

    override fun getDraftOrderHeader(customerId: Long): Flow<DraftOrderHeaderEntity?> {
        TODO("Not yet implemented")
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): CustomerAddressResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerAddresses(customerId: Long): CustomerAddressesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun completeDraftOrder(
        draftOrderId: Long,
        paymentPending: Boolean
    ): Response<ResponseDraftOrderForRequestCreate> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        body: DraftOrderUpdateRequest
    ): Response<ResponseDraftOrderForRequestCreate> {
        TODO("Not yet implemented")
    }

    override fun getDraftOrderWithItems(customerId: Long): Flow<Pair<DraftOrderHeaderEntity?, List<LineItem>>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveDraftOrderWithItems(
        header: DraftOrderHeaderEntity,
        items: List<LineItem>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun increaseQuantity(lineItemId: Long, customerId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun decreaseQuantity(lineItemId: Long, customerId: Long) {
        TODO("Not yet implemented")
    }


}