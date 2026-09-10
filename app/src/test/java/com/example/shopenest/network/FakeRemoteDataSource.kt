package com.example.shopenest.network

import com.example.shopenest.model.Brands
import com.example.shopenest.model.Categories
import com.example.shopenest.model.CountCustomer
import com.example.shopenest.model.CreateCustomerAddressRequest
import com.example.shopenest.model.CustomerAddressResponse
import com.example.shopenest.model.CustomerAddressesResponse
import com.example.shopenest.model.CustomerRequest
import com.example.shopenest.model.CustomerResponse
import com.example.shopenest.model.Customers
import com.example.shopenest.model.DraftOrderRequest
import com.example.shopenest.model.DraftOrderUpdateRequest
import com.example.shopenest.model.Product
import com.example.shopenest.model.ProductResponse
import com.example.shopenest.model.ResponseDiscount
import com.example.shopenest.model.ResponseDraftOrderForRequestCreate
import com.example.shopenest.model.ResponseDraftOrderForRetrieve
import com.example.shopenest.model.ResponseInventory
import com.example.shopenest.model.ShoppingProducts

import retrofit2.Response



class FakeRemoteDataSource(

    var brandsResponse: Brands = Brands(smart_collections = emptyList()),
    var categoriesResponse: Categories = Categories(custom_collections = emptyList()),
    var kidsProductsResponse: ShoppingProducts = ShoppingProducts(products = emptyList()),
    var womenProductsResponse: ShoppingProducts = ShoppingProducts(products = emptyList()),
    var menProductsResponse: ShoppingProducts = ShoppingProducts(products = emptyList()),
    var productsForBrandResponse: ShoppingProducts = ShoppingProducts(products = emptyList()),
    var productDetailsResponse: ProductResponse = ProductResponse(product = Product()),
    // 2. تغليف البيانات في CustomerResponse و Response.success()
    var createCustomerResponse: Response<CustomerResponse> = Response.success(
        CustomerResponse(customer = mockCustomerData)
    ),

    var getCustomersByMailResponse: Response<Customers> = Response.success(
        Customers (customers = mutableListOf())
    ),

    var countCustomerResponse: CountCustomer = CountCustomer(count = 0),

    var deleteCustomerResponse: Response<Unit> = Response.success(Unit),

    var getInventoryResponse : ResponseInventory=
       ResponseInventory( inventory_levels = emptyList()),

    var discountResponse : ResponseDiscount=
        ResponseDiscount( discount_codes = emptyList()),

    var draftOrderForCreateResponse: Response<ResponseDraftOrderForRequestCreate> = Response.success(

        ResponseDraftOrderForRequestCreate(draft_order = createDummyDraftOrder())
    ),

    var deleteDraftOrderResponse: Response<Unit> = Response.success(Unit),

    var getCustomerByIdResponse: Response<CustomerResponse> = Response.success(
        CustomerResponse(customer = mockCustomerData)
    ),

    var updateCustomerResponse: Response<CustomerResponse> = Response.success(
        CustomerResponse(customer = mockCustomerData)
    ),

    var setDefaultCustomerAddressResponse: CustomerAddressResponse = CustomerAddressResponse ( customer_address = createDummyCustomerAddress()),
    var createCustomerAddressResponse: CustomerAddressResponse = CustomerAddressResponse ( customer_address = createDummyCustomerAddress()),
    var getCustomerAddressesResponse: CustomerAddressesResponse = CustomerAddressesResponse ( addresses = emptyList()),
//ResponseDraftOrderForRequestCreate


    var completeDraftOrderForRequestCreate: Response<ResponseDraftOrderForRequestCreate> = Response.success(
        ResponseDraftOrderForRequestCreate(draft_order = createDummyDraftOrder())
    ),

    var UpdateDraftOrderForRequestCreate: Response<ResponseDraftOrderForRequestCreate> = Response.success(
        ResponseDraftOrderForRequestCreate(draft_order = createDummyDraftOrder())
    ),


    ): RemoteSource {
    // 1. تجهيز كائن CustomerData وهمي بالقيم المطلوبة


    // متغير لمحاكاة أخطاء الشبكة (Error Case)
    var shouldReturnError = false

    override suspend fun getBrands(): Brands {

        if (shouldReturnError) {
            throw Exception("Network Connection Error")
        }
        return brandsResponse

    }

    override suspend fun getCategory(): Categories {

        if (shouldReturnError) {
            throw Exception("Server Error")
        }
        return categoriesResponse
    }


    override suspend fun getProductsForSectionKidsCategory(): ShoppingProducts {
        if (shouldReturnError) {
            throw Exception("Server Error")
        }
        return kidsProductsResponse


    }

    override suspend fun getProductsForSectionWomenCategory(): ShoppingProducts {

        if (shouldReturnError) {
            throw Exception("Server Error")
        }
        return womenProductsResponse

    }

    override suspend fun getProductsForSectionMenCategory(): ShoppingProducts {

        if (shouldReturnError) {
            throw Exception("Server Error")
        }
        return menProductsResponse

    }

    override suspend fun getProductsForBrands(vendor: String): ShoppingProducts {

        if (shouldReturnError) {
            throw Exception("Network Error for brand: $vendor")
        }
        return productsForBrandResponse
    }


    override suspend fun getProductsDetails(id: Long): ProductResponse {

        if (shouldReturnError) {
            throw Exception("Server Error for product details id: $id")
        }
        return productDetailsResponse
    }


    override suspend fun createCustomer(customer: CustomerRequest): Response<CustomerResponse> {

        if (shouldReturnError) {

            throw Exception("Server Error for create Customer")
        }
       return createCustomerResponse
     }


    override suspend fun getCustomerByEmail(email: String): Response<Customers> {

        if (shouldReturnError) {
            throw Exception("Network Failure")
        }

        return getCustomersByMailResponse

    }

    override suspend fun getCountCustomer(): CountCustomer {

            if (shouldReturnError) {
                throw Exception("Network Failure")
            }
            return countCustomerResponse

    }


    override suspend fun deleteCustomer(customerId: Long): Response<Unit> {

        if (shouldReturnError) {
            throw Exception("Network Error for delete customer id: $customerId")
        }
        return deleteCustomerResponse
    }

    override suspend fun getAvailableProducts(inventoryItemId: Long): ResponseInventory {

        if (shouldReturnError) {
            throw Exception("Network Error for InventoryLevel")
        }
        return getInventoryResponse
    }

    override suspend fun getDiscount(): ResponseDiscount {

        if (shouldReturnError) {
            throw Exception("Network Error for discount")
        }
        return discountResponse
    }

    override suspend fun createCartOrder(cartOrder: DraftOrderRequest): Response<ResponseDraftOrderForRequestCreate> {

        if (shouldReturnError) {
            throw Exception("Network Error for create discount")
        }
        return draftOrderForCreateResponse
    }

   /* override suspend fun getDraftOrders(): Response<ResponseDraftOrderForRetrieve> {
        TODO("Not yet Used in app ")
    }*/

    override suspend fun deleteDraftOrderById(draftOrderId: Long): Response<Unit> {
        if (shouldReturnError) {
            throw Exception("Network Error for  delete draftOrderByID")
        }
        return deleteDraftOrderResponse
    }


    override suspend fun getCustomerById(customerId: Long): Response<CustomerResponse> {

        if (shouldReturnError) {
            throw Exception("Network Error for  delete draftOrderBy_Customer_Id")
        }
        return getCustomerByIdResponse
    }



    override suspend fun updateCustomer(
        customerId: Long,
        body: CustomerRequest
    ): Response<CustomerResponse> {

        if (shouldReturnError) {
            throw Exception("Server Error for update customer: $customerId")
        }
        return updateCustomerResponse
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): CustomerAddressResponse {

        if (shouldReturnError) {
            throw Exception("Server Error for setDefaultAddress customer: $customerId")
        }
        return setDefaultCustomerAddressResponse
    }

    override suspend fun createCustomerAddress(
        customerId: Long,
        request: CreateCustomerAddressRequest
    ): CustomerAddressResponse {

        if (shouldReturnError) {
            throw Exception("Server Error for create customer Address: $customerId")
        }
        return createCustomerAddressResponse
    }

    override suspend fun getCustomerAddresses(customerId: Long): CustomerAddressesResponse {

        if (shouldReturnError) {
            throw Exception("Server Error for get customer Address: $customerId")

        }
        return getCustomerAddressesResponse
    }

    override suspend fun completeDraftOrder(
        draftOrderId: Long,
        paymentPending: Boolean
    ): Response<ResponseDraftOrderForRequestCreate> {
        if (shouldReturnError) {
            throw Exception("Server Error for complete draft Order")
        }
        return completeDraftOrderForRequestCreate

    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        body: DraftOrderUpdateRequest
    ): Response<ResponseDraftOrderForRequestCreate> {
        if (shouldReturnError) {
            throw Exception("Server Error for update draft Order")
        }
        return UpdateDraftOrderForRequestCreate

    }
}