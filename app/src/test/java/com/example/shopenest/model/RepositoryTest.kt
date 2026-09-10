package com.example.shopenest.model

import com.example.shopenest.db.FakeLocalDataSource
import com.example.shopenest.network.FakeRemoteDataSource
import com.example.shopenest.network.createDummyCustomerAddress
import com.example.shopenest.network.createDummyDraftOrder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.math.exp


class RepositoryTest () {

    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var remoteDataSource: FakeRemoteDataSource
    private lateinit var repo: Repository

    @Before
    fun setUp() {
        //Given
        // إعادة تهيئة الـ Fake قبل كل اختبار لضمان عزلة البيانات
        localDataSource = FakeLocalDataSource()
        remoteDataSource = FakeRemoteDataSource()
        repo = Repository(remoteDataSource, localDataSource)

    }

    @Test
    fun getAllFavProducts_validCustomerId_returnsListOfProducts() = runTest {

        val customerId = 1L
        val expectedProducts = listOf(
            createDummyProduct(id = 101L, title = "Nike Shoes"),
            createDummyProduct(id = 102L, title = "Adidas Shirt")
        )

        // نضع المنتجات داخل الخريطة الوهمية الخاصة بـ FakeLocalDataSource
        localDataSource.productsMapForCustomer[customerId] = expectedProducts.toMutableList()

        // نستخدم first() لاستخراج أول قائمة يتم إرسالها عبر الـ Flow
        val actualProducts = repo.getAllFavProducts(customerId).first()

        // 3. Then (التحقق من صحة النتيجة)
        // نتأكد أن القائمة القادمة من الـ Repository تطابق القائمة المتوقعة
        assertThat(actualProducts, IsEqual(expectedProducts))
    }


    @Test
    fun saveProduct_validProduct_savesProductInLocalSource() = runTest {
        // 1. Given (تجهيز منتج للاختبار)
        val productTest = Product(id = 1, customerId = 100, title = "Nike")

        // 2. When (استدعاء دالة الـ Repo لحفظ المنتج)
        repo.saveProduct(productTest)

        // 3. Then (التحقق من أن المنتج أُضيف بنجاح داخل الـ Fake)
        val savedProducts = localDataSource.productsMapForCustomer[productTest.customerId]

        // التأكد من أن القائمة ليست null وأنها تحتوي على عنصر واحد فقط
        assertThat(savedProducts?.size, IsEqual(1))

        // التأكد من أن المنتج المخزّن هو نفسه المنتج الذي أرسلناه للـ Repo
        assertThat(savedProducts?.first(), IsEqual(productTest))
    }




    @Test
    fun deleteById_validProductIdAndCustomerId_deletesProductFromLocalSource() = runTest {
        // 1. Given (تجهيز البيئة بوجود منتج مخزن مسبقاً)
        val customerId = 3L
        val productId = 2L
        val productTest = Product(id = productId, customerId = customerId, title = "Mx")

        // إضافة المنتج للـ Fake ليكون هناك شيء قابل للحذف
        localDataSource.productsMapForCustomer[customerId] = mutableListOf(productTest)

        // 2. When (تنفيذ عملية الحذف من الـ Repo)
        repo.deleteById(productId, customerId)

        // 3. Then (التحقق من خلو القائمة من هذا المنتج)
        val savedProducts = localDataSource.productsMapForCustomer[customerId]

        // التحقق من أن حجم القائمة أصبح 0 فعلياً بعد الحذف
        assertThat(savedProducts?.size, IsEqual(0))
    }


     @Test
     fun  saveDraftOrderHeader_validDraftOrderHeader_saveDraftOrderInLocal () =runTest {

         val draftOrderHeader  = createDummyDraftOrderHeader (1L,2L)

         repo.saveDraftOrderHeader(draftOrderHeader)

         val savedDraft = localDataSource.draftOrderList[2]

         assertThat(savedDraft?.size, IsEqual(1))

     }


    @Test
    fun  saveLineItems_validLineItems_savedLineItemsInLocal() = runTest {

        val items = LineItem(2L, customerId = 5L)

        val listLineItems : List<LineItem> = listOf(items)

        repo.saveLineItems(listLineItems)

        val savedItems = localDataSource.lineItemList[5]

        assertThat(savedItems?.size, IsEqual(1))


    }


    @Test
    fun getLineItems_validCustomerId_returnListOfLineItems () = runTest {

        val items = LineItem(7L, customerId = 3L)

        val expectedLineItems : List<LineItem> = listOf(items)

       localDataSource.lineItemList[3] = expectedLineItems.toMutableList()

      val actualItems =  repo.getLineItems(3).first()

        assertThat(actualItems, IsEqual(expectedLineItems))



    }

    @Test
    fun deleteDraftOrder_validDraftOrderIdAndCustomerId () = runTest {

        val draftOrderId = 3L
        val customerId = 4L

        val draftOrderHeader  = createDummyDraftOrderHeader (draftOrderId,customerId)

        localDataSource.draftOrderList[customerId] = mutableListOf(draftOrderHeader)

        repo.deleteDraftOrder(draftOrderId,customerId)

          val savedDraft = localDataSource.draftOrderList[customerId]

        assertThat(savedDraft?.size, IsEqual(0))

    }

    @Test
    fun getDraftOrderHeader_validCustomerId_returnListOfDraftOrders()= runTest {

        val draftOrderHeaderExpected  = createDummyDraftOrderHeader (3L,2L)

        val listOfOrder = mutableListOf(draftOrderHeaderExpected)

        localDataSource.draftOrderList[2] = listOfOrder

       val  actualDraftOrder = repo.getDraftOrderHeader(2).first()

        assertThat(actualDraftOrder, IsEqual(draftOrderHeaderExpected))


    }


    @Test
    fun getDraftOrderWithItems_existingHeaderAndItems_returnsCombinedPair() = runTest {
        // 1. Given (تجهيز بيانات الـ Header والـ Items في الـ Fake)
        val customerId = 100L
        val expectedHeader = createDummyDraftOrderHeader(draftOrderId = 1L, customerId = customerId)
        val expectedItems = listOf(
            LineItem(idLineItem = 10L, customerId = customerId),
            LineItem(idLineItem = 11L, customerId = customerId)
        )

        // وضع البيانات داخل الـ FakeLocalDataSource
        localDataSource.draftOrderList[customerId] = mutableListOf(expectedHeader)
        localDataSource.lineItemList[customerId] = expectedItems.toMutableList()

        // 2. When (استدعاء الدالة واستخراج القائمة الأولى عبر first())
        val actualPair = repo.getDraftOrderWithItems(customerId).first()

        // 3. Then (الفحص والتأكد من صحة الدمج)
        // نتحقق من أن الـ Header المطابق رجع في first
        assertThat(actualPair.first, IsEqual(expectedHeader))

        // نتحقق من أن القائمة المطابقة رجعت في second
        assertThat(actualPair.second, IsEqual(expectedItems))
    }


    @Test
    fun saveDraftOrderWithItems_validHeaderAndItems_savesBothInLocalSource() = runTest {
        // 1. Given (تجهيز الـ Header والـ Items)
        val customerId = 100L
        val dummyHeader = createDummyDraftOrderHeader(draftOrderId = 10L, customerId = customerId)
        val dummyItems = listOf(
            LineItem(idLineItem = 1L, customerId = customerId),
            LineItem(idLineItem = 2L, customerId = customerId)
        )

        // 2. When (استدعاء الدالة المركبة من الـ Repository)
        repo.saveDraftOrderWithItems(dummyHeader, dummyItems)

        // 3. Then (التحقق من التخزين في كلا المكانين داخل الـ Fake)
        val savedHeaderList = localDataSource.draftOrderList[customerId]
        val savedItemsList = localDataSource.lineItemList[customerId]

        // فحص تخزين الـ Header
        assertThat(savedHeaderList?.size, IsEqual(1))
        assertThat(savedHeaderList?.first(), IsEqual(dummyHeader))

        // فحص تخزين الـ LineItems
        assertThat(savedItemsList?.size, IsEqual(2))
        assertThat(savedItemsList, IsEqual(dummyItems))
    }


    @Test
    fun increaseQuantity_validLineItemId_increasesQuantityByOne() = runTest {
        // 1. Given (تحديث السلة بمنتج كميته الأولية 1)
        val customerId = 5L
        val lineItemId = 10L
        val initialItem = LineItem(idLineItem = lineItemId, customerId = customerId, quantity = 1)

        localDataSource.lineItemList[customerId] = mutableListOf(initialItem)

        // 2. When (استدعاء دالة زيادة الكمية)
        repo.increaseQuantity(lineItemId = lineItemId, customerId = customerId)

        // 3. Then (الفحص والتأكد أن الكمية أصبحت 2)
        val updatedItems = localDataSource.lineItemList[customerId]
        val updatedItem = updatedItems?.firstOrNull { it.idLineItem == lineItemId }

        assertThat(updatedItem?.quantity, IsEqual(2))
    }


    @Test
    fun decreaseQuantity_validLineItemId_decreasesQuantityByOne() = runTest {
        // 1. Given (تحديث السلة بمنتج كميته الأولية 1)
        val customerId = 5L
        val lineItemId = 10L
        val initialItem = LineItem(idLineItem = lineItemId, customerId = customerId, quantity = 2)

        localDataSource.lineItemList[customerId] = mutableListOf(initialItem)

        // 2. When (استدعاء دالة زيادة الكمية)
        repo.decreaseQuantity(lineItemId = lineItemId, customerId = customerId)

        // 3. Then (الفحص والتأكد أن الكمية أصبحت 2)
        val updatedItems = localDataSource.lineItemList[customerId]
        val updatedItem = updatedItems?.firstOrNull { it.idLineItem == lineItemId }

        assertThat(updatedItem?.quantity, IsEqual(1))
    }

    // Test Remote :


    @Test
    fun getBrands_success_returnsBrandsFromRemote() = runTest {
        // 1. Given: تجهيز الاستجابة المتوقعة داخل الـ FakeRemote
        val expectedBrands = Brands(
            smart_collections = listOf(
                SmartCollection(id = 1L, title = "Nike", image = createDummyImageBrand()),
                SmartCollection(id = 2L, title = "Adidas",image = createDummyImageBrand())
            )
        )
        remoteDataSource.brandsResponse = expectedBrands

        // 2. When: استدعاء دالة الـ Repo
        val actualBrands = repo.getBrands()

        // 3. Then: التأكد من تطابق البيانات المرجعة مع بيانات الـ Remote
        assertThat(actualBrands, IsEqual(expectedBrands))
        assertThat(actualBrands.smart_collections.size, IsEqual(2))
    }



    @Test
    fun getCategories_success_returnsCategoriesFromRemote() = runTest {
        // 1. Given: تجهيز الاستجابة المتوقعة داخل الـ FakeRemote
        val expectedCategories = Categories(
            custom_collections = listOf(
                createDummyCustomCollection(id = 2L, title = "ADIDAS"),
                createDummyCustomCollection(id = 3L , title = "BIM")
            )
        )
        remoteDataSource.categoriesResponse =  expectedCategories

        // 2. When: استدعاء دالة الـ Repo
        val actualCategories = repo.getCategory()

        // 3. Then: التأكد من تطابق البيانات المرجعة مع بيانات الـ Remote
        assertThat(actualCategories, IsEqual(expectedCategories))
        assertThat(actualCategories.custom_collections.size, IsEqual(2))
    }


    @Test
    fun getProductsForSectionKidsCategory_success_returnsKidsProducts() = runTest {
        // 1. Given: تجهيز قائمة منتجات وهمية وتعيينها لـ kidsProductsResponse في الـ Fake
        val expectedProduct = Product(id = 10L, title = "Kids T-Shirt")
        val expectedResponse = ShoppingProducts(products = listOf(expectedProduct))

        remoteDataSource.kidsProductsResponse = expectedResponse

        // 2. When: استدعاء الدالة من الـ Repository
        val actualResponse = repo.getProductsForSectionKidsCategory()

        // 3. Then: التحقق من نجاح المخرجات وتطابقها
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.products.size, IsEqual(1))
        assertThat(actualResponse.products.first().title, IsEqual("Kids T-Shirt"))
    }


    @Test
    fun getProductsForSectionWomenCategory_success_returnsWomenProducts() = runTest {
        // 1. Given: تجهيز قائمة منتجات وهمية وتعيينها لـ kidsProductsResponse في الـ Fake
        val expectedProduct = Product(id = 10L, title = "Women T-Shirt")
        val expectedResponse = ShoppingProducts(products = listOf(expectedProduct))

        remoteDataSource.womenProductsResponse= expectedResponse

        // 2. When: استدعاء الدالة من الـ Repository
        val actualResponse = repo.getProductsForSectionWomenCategory()

        // 3. Then: التحقق من نجاح المخرجات وتطابقها
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.products.size, IsEqual(1))
        assertThat(actualResponse.products.first().title, IsEqual("Women T-Shirt"))
    }


    @Test
    fun getProductsForSectionMenCategory_success_returnsMenProducts() = runTest {
        // 1. Given: تجهيز قائمة منتجات وهمية وتعيينها لـ kidsProductsResponse في الـ Fake
        val expectedProduct = Product(id = 10L, title = "Men T-Shirt")
        val expectedResponse = ShoppingProducts(products = listOf(expectedProduct))

        remoteDataSource.menProductsResponse = expectedResponse

        // 2. When: استدعاء الدالة من الـ Repository
        val actualResponse = repo.getProductsForSectionMenCategory()

        // 3. Then: التحقق من نجاح المخرجات وتطابقها
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.products.size, IsEqual(1))
        assertThat(actualResponse.products.first().title, IsEqual("Men T-Shirt"))
    }



    @Test
    fun getProductsForBrands_validVendor_returnsBrandProducts() = runTest {
        // 1. Given: تجهيز منتجات خاصة بماركة معينة (مثل Nike)
        val expectedProduct = Product(id = 101L, title = "Nike Air Max", vendor = "Nike")
        val expectedResponse = ShoppingProducts(products = listOf(expectedProduct))

        remoteDataSource.productsForBrandResponse = expectedResponse

        // 2. When: استدعاء الدالة وتمرير اسم الماركة
        val actualResponse = repo.getProductsForBrands("Nike")

        // 3. Then: التأكد من تطابق النتيجة
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.products.first().vendor, IsEqual("Nike"))
    }


    @Test
    fun getProductsDetails_validId_returnsProductResponse() = runTest {
        // 1. Given: تجهيز كائن تفاصيل منتج وهمي باستخدام Dummy Object
        val productId = 505L
        val expectedProduct = Product(id = productId, title = "Nike Running Shoes")
        val expectedResponse = ProductResponse(product = expectedProduct)

        remoteDataSource.productDetailsResponse = expectedResponse

        // 2. When: استدعاء الدالة برقم الـ ID
        val actualResponse = repo.getProductsDetails(productId)

        // 3. Then: التأكد من تطابق التفاصيل المرجعة
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.product.id, IsEqual(productId))
        assertThat(actualResponse.product.title, IsEqual("Nike Running Shoes"))
    }

    @Test
    fun createCustomer_validCustomerRequest_returnsSuccessResponse() = runTest {
             // Given: تجهيز طلب عميل وهمي واستجابة نجاح متوقعة
         var  dummyRequest = CustomerRequest (customer = CustomerBody(first_name = "Amr", email = "amr567@yahoo.com"))
         val expectedCustomerData = createDummyCustomerData(email = "newuser@example.com")
         val expectedResponse = Response.success(CustomerResponse(customer = expectedCustomerData))

          remoteDataSource.createCustomerResponse = expectedResponse

        // 2. When: استدعاء الدالة
        val actualResponse = repo.createCustomer(dummyRequest)

        // 3. Then: التأكد من نجاح الاستجابة وتطابق البيانات
        assertThat(actualResponse.body()?.customer?.email, IsEqual("newuser@example.com"))
        assertThat(actualResponse.code(), IsEqual(200))


    }


    @Test
    fun getCustomerByEmail_existingEmail_returnsCustomerList() = runTest {
        // 1. Given: تجهيز عميل وهمي وضعه داخل قائمة الاستجابة للـ Fake
        val email = "ali@example.com"
        val dummyCustomer = createDummyCustomerData(email = email)
        val expectedResponse = Response.success(Customers(customers = mutableListOf(dummyCustomer)))

        remoteDataSource.getCustomersByMailResponse = expectedResponse

        // 2. When: استدعاء الدالة
        val actualResponse = repo.getCustomerByEmail(email)

        // 3. Then: التأكد من نجاح الاستجابة وتطابق البريد الإلكتروني
        assertThat(actualResponse.body()?.customers?.size, IsEqual(1))
        assertThat(actualResponse.body()?.customers?.first()?.email, IsEqual(email))
    }


    @Test
    fun getCountCustomer_success_returnsCorrectCount() = runTest {

        var countCustomer = CountCustomer(count = 3)

        remoteDataSource.countCustomerResponse = countCustomer

       var actual =  repo.getCountCustomer()

        assertThat(actual.count, IsEqual(3))

    }

    @Test
    fun deleteCustomer_validCustomerId_returnsSuccessResponse() = runTest {
        // 1. Given: تجهيز استجابة نجاح (Response.success مع Unit)
        val customerId = 100L
        val expectedResponse = Response.success(Unit)
        remoteDataSource.deleteCustomerResponse = expectedResponse

        // 2. When: استدعاء الدالة بحذف العميل
        val actualResponse = repo.deleteCustomer(customerId)

        // 3. Then: التأكد من نجاح الاستجابة
        assertThat(actualResponse.code(), IsEqual(200))
    }


    @Test
    fun getAvailableProducts_validInventoryItemId_returnsInventoryResponse() = runTest {
        // 1. Given: تجهيز كائن مخزون وهمي وتعيينه للـ Fake
        val itemId = 1001L
        val dummyInventory = createDummyInventoryLevel(inventoryItemId = itemId, available = 15)
        val expectedResponse = ResponseInventory(inventory_levels = listOf(dummyInventory))

        remoteDataSource.getInventoryResponse = expectedResponse

        // 2. When: استدعاء الدالة من الـ Repository
        val actualResponse = repo.getAvailableProducts(itemId)

        // 3. Then: التحقق من تطابق البيانات والمرجعيات
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.inventory_levels.size, IsEqual(1))
        assertThat(actualResponse.inventory_levels.first().available, IsEqual(15))

    }


    @Test
    fun getDiscount_success_returnsDiscountResponse() = runTest {
        // 1. Given: تجهيز كود خصم وهمي وتعيين الاستجابة في الـ Fake
        val dummyDiscount = createDummyDiscountCode(code = "SUMMER2026")
        val expectedResponse = ResponseDiscount(discount_codes = listOf(dummyDiscount))

        remoteDataSource.discountResponse = expectedResponse

        // 2. When: استدعاء الدالة من الـ Repository
        val actualResponse = repo.getDiscount()

        // 3. Then: التحقق من تطابق الاستجابة والكود المرجع
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.discount_codes.size, IsEqual(1))
        assertThat(actualResponse.discount_codes.first().code, IsEqual("SUMMER2026"))
    }

    @Test
    fun createCartOrder_validRequest_returnsDraftOrderResponse() = runTest {
        // 1. Given: تجهيز طلب سلة وهمي واستجابة نجاح متوقعة
        val requestBody = DraftOrderRequestBody(emptyList()) // 1. إنشاء الـ Body
        val dummyRequest = DraftOrderRequest(draft_order = requestBody) // 2. تغليفه داخل الـ Request

        val dummyDraftOrderResponse = ResponseDraftOrderForRequestCreate(
            draft_order = createDummyDraftOrder(idDraftOrder = 999L)
        )

        val expectedResponse = Response.success(dummyDraftOrderResponse)

        remoteDataSource.draftOrderForCreateResponse = expectedResponse

        // 2. When: استدعاء الدالة من الـ Repository
        val actualResponse = repo.createCartOrder(dummyRequest)

        // 3. Then: التأكد من نجاح الاستجابة وتطابق بيانات الـ Draft Order
        assertThat(actualResponse.body()?.draft_order?.idDraftOrder, IsEqual(999L))
        assertThat(actualResponse.code(), IsEqual(200))

    }

    @Test
    fun deleteDraftOrderById_validId_returnsSuccessResponse() = runTest {
        // 1. Given: تجهيز استجابة نجاح (Response.success مع Unit)
        val draftOrderId = 888L
        val expectedResponse = Response.success(Unit)
        remoteDataSource.deleteDraftOrderResponse = expectedResponse

        // 2. When: استدعاء الدالة برقم طلب المسودة
        val actualResponse = repo.deleteDraftOrderById(draftOrderId)

        // 3. Then: التأكد من نجاح الاستجابة وكود HTTP 200
        assertThat(actualResponse.code(), IsEqual(200))
    }

    @Test
    fun getCustomerById_validCustomerId_returnsCustomerResponse() = runTest {
        // 1. Given: تجهيز بيانات عميل وهمية واستجابة نجاح
        val customerId = 101L
        val dummyCustomer = createDummyCustomerData(id = customerId, firstName = "Ahmed")
        val expectedResponse = Response.success(CustomerResponse(customer = dummyCustomer))

        remoteDataSource.getCustomerByIdResponse = expectedResponse

        // 2. When: استدعاء الدالة بواسطة الـ ID
        val actualResponse = repo.getCustomerById(customerId)

        // 3. Then: التأكد من نجاح الاستجابة وتطابق بيانات العميل
        assertThat(actualResponse.body()?.customer?.id, IsEqual(customerId))
        assertThat(actualResponse.body()?.customer?.first_name, IsEqual("Ahmed"))
    }

    @Test
    fun updateCustomer_validData_returnsSuccessResponse() = runTest {
        // 1. Given: تجهيز كائن الطلب واستجابة النجاح المتوقعة
        val customerId = 101L
        val dummyRequest = CustomerRequest(customer = CustomerBody(first_name = "Omar", email = "omar123@yahoo.com"))
        val updatedCustomerData = createDummyCustomerData(id = customerId, firstName = "Omar")
        val expectedResponse = Response.success(CustomerResponse(customer = updatedCustomerData))

        remoteDataSource.updateCustomerResponse = expectedResponse

        // 2. When: استدعاء الدالة للتحديث
        val actualResponse = repo.updateCustomer(customerId, dummyRequest)

        // 3. Then: التأكد من نجاح الاستجابة وتطابق الحقل المعدل
        assertThat(actualResponse.body()?.customer?.first_name, IsEqual("Omar"))
        assertThat(actualResponse.code(), IsEqual(200))
    }



    @Test
    fun setDefaultAddress_validIds_returnsCustomerAddressResponse() = runTest {
        // 1. Given: تجهيز الاستجابة المتوقعة لتعيين العنوان الافتراضي
        val customerId = 101L
        val addressId = 505L
        val expectedResponse = CustomerAddressResponse(
            customer_address = createDummyCustomerAddress(id = addressId, customerId = customerId)
        )

        remoteDataSource.setDefaultCustomerAddressResponse = expectedResponse

        // 2. When: استدعاء الدالة من الـ Repository
        val actualResponse = repo.setDefaultAddress(customerId, addressId)

        // 3. Then: التأكد من تطابق البيانات والمرجعيات
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.customer_address.id, IsEqual(addressId))
    }

    @Test
    fun createCustomerAddress_validData_returnsCustomerAddressResponse() = runTest {
        // 1. Given: تجهيز كائن الطلب واستجابة النجاح المتوقعة
        val customerId = 101L
        val dummyAddressRequest = CreateCustomerAddressRequest (createDummyAddressBody(address1 = "123 Main St", city = "Cairo"))
        val expectedAddress = createDummyCustomerAddress(
            id = 505L,
            customerId = customerId,
            address1 = "123 Main St",
            city = "Cairo"
        )
        val expectedResponse = CustomerAddressResponse(customer_address = expectedAddress)

        remoteDataSource.createCustomerAddressResponse = expectedResponse

        // 2. When: استدعاء الدالة من الـ Repository
        val actualResponse = repo.createCustomerAddress(customerId, dummyAddressRequest)

        // 3. Then: التأكد من تطابق البيانات والمرجعيات
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.customer_address.id, IsEqual(505L))
        assertThat(actualResponse.customer_address.address1, IsEqual("123 Main St"))
    }
    @Test
    fun getCustomerAddresses_validCustomerId_returnsAddressesList() = runTest {
        // 1. Given: تجهيز قوائم عناوين وهمية للعميل
        val customerId = 101L
        val dummyAddress1: CustomerAddress =  createDummyCustomerAddress(id = 501L, customerId = customerId, address1 = "Cairo St")
        val dummyAddress2: CustomerAddress = createDummyCustomerAddress(id = 502L, customerId = customerId, address1 = "Alex St")

        val expectedResponse = CustomerAddressesResponse(
            addresses = listOf(dummyAddress1, dummyAddress2)
        )

        remoteDataSource.getCustomerAddressesResponse = expectedResponse

        // 2. When: استدعاء الدالة بواسطة ID العميل
        val actualResponse = repo.getCustomerAddresses(customerId)

        // 3. Then: التأكد من تطابق البيانات وعدد العناوين المرجعة
        assertThat(actualResponse, IsEqual(expectedResponse))
        assertThat(actualResponse.addresses.size, IsEqual(2))
        assertThat(actualResponse.addresses.first().address1, IsEqual("Cairo St"))
    }


    @Test
    fun completeDraftOrder_paymentPendingTrue_returnsSuccessResponse() = runTest {
        // 1. Given: تجهيز طلب مكتمل وهمي واستجابة نجاح
        val draftOrderId = 9001L
        val completedOrder = createDummyDraftOrder(
            idDraftOrder = draftOrderId,
            status = "completed",
            completedAt = "2026-09-09T10:00:00Z"
        )
        val expectedResponseBody = ResponseDraftOrderForRequestCreate(draft_order = completedOrder)
        val expectedResponse = Response.success(expectedResponseBody)

        remoteDataSource.completeDraftOrderForRequestCreate = expectedResponse

        // 2. When: استدعاء دالة الإكمال
        val actualResponse = repo.completeDraftOrder(draftOrderId, paymentPending = true)

        // 3. Then: التأكد من نجاح الاستجابة وتحديث حالة الطلب
        assertThat(actualResponse.body()?.draft_order?.status, IsEqual("completed"))
        assertThat(actualResponse.code(), IsEqual(200))
    }


    @Test
    fun updateDraftOrder_validData_returnsSuccessResponse() = runTest {
        // 1. Given: تجهيز كائن الطلب واستجابة النجاح المتوقعة
        val draftOrderId = 9001L
        val dummyUpdateBody = createDummyDraftOrderUpdateBody(id = draftOrderId)
        val dummyRequest = DraftOrderUpdateRequest(draftOrder = dummyUpdateBody)

        val updatedDraftOrder = createDummyDraftOrder(
            idDraftOrder = draftOrderId,
            appliedDiscount = dummyUpdateBody.appliedDiscount
        )

        val expectedResponseBody = ResponseDraftOrderForRequestCreate(draft_order = updatedDraftOrder)
        val expectedResponse = Response.success(expectedResponseBody)

        remoteDataSource.UpdateDraftOrderForRequestCreate = expectedResponse

        // 2. When: استدعاء دالة التحديث
        val actualResponse = repo.updateDraftOrder(draftOrderId, dummyRequest)

        // 3. Then: التأكد من نجاح الاستجابة وتطابق بيانات الخصم المطبق
        assertThat(actualResponse.body()?.draft_order?.idDraftOrder, IsEqual(draftOrderId))
        assertThat(
            actualResponse.body()?.draft_order?.applied_discount?.title,
            IsEqual(dummyUpdateBody.appliedDiscount.title)
        )

        assertThat(actualResponse.code(), IsEqual(200))
    }






}































