package com.example.shopenest.favouritescreen.viewmodel

import com.example.shopenest.model.FakeRepo
import com.example.shopenest.model.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FaViewModelTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var fakeRepository: FakeRepo
    private lateinit var viewModel: FavViewModel


    @Before
    fun setUp() {
        fakeRepository = FakeRepo()
        viewModel = FavViewModel(fakeRepository,
            ioDispatcher = mainDispatcherRule.testDispatcher

            )

    }

    @Test
    fun getAllProducts_validCustomerId_updatesProductsStateFlow() = runTest {
        // Given: تجهيز قائمة منتجات وهمية داخل الـ FakeRepository
        val expectedProducts = listOf(
            Product(id = 101L, title = "Running Shoes", customerId = 1L),
            Product(id = 102L, title = "Sports Watch", customerId = 1L)
        )
        fakeRepository.setFavProducts(expectedProducts)

        // When: استدعاء الدالة لجلب المنتجات
        viewModel.getAllProducts(customerId = 1L)

        // Then: التحقق من تحديث قيمة الـ StateFlow بالبيانات المتوقعة
        val actualProducts = viewModel.products?.value

        assertThat(actualProducts, IsEqual(expectedProducts))
        assertThat(actualProducts?.size, IsEqual(2))
    }

    @Test
    fun saveProduct_success_clearsErrorMessage() = runTest {
        // Given
        val product = Product(id = 101L, title = "Laptop")
        fakeRepository.shouldReturnError = false

        // When
        viewModel.saveProduct(product)

        // Then
        assertThat(viewModel.errorMessage.value, IsEqual(null))
    }


    @Test
    fun deleteProduct_success_errorMessageIsNull() = runTest {
        // 1. Given: تجهيز منتج وحفظه أولاً في الـ FakeRepository
        val productId = 101L
        val customerId = 1L

        // 2. When: حذف المنتج
        viewModel.deleteProduct(productId, customerId)

        // 3. Then: التأكد من أن الـ errorMessage قيمتها null
        assertThat(viewModel.errorMessage.value, IsEqual(null))
    }



}