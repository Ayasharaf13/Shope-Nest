package com.example.shopenest.db

import com.example.shopenest.model.DraftOrderHeaderEntity
import com.example.shopenest.model.LineItem
import com.example.shopenest.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(

    val productsMapForCustomer: MutableMap<Long, MutableList<Product>> = mutableMapOf<Long, MutableList<Product>>(),
    val draftOrderList: MutableMap<Long, MutableList<DraftOrderHeaderEntity>> = mutableMapOf<Long, MutableList<DraftOrderHeaderEntity>>(),
    val lineItemList: MutableMap<Long, MutableList<LineItem>> = mutableMapOf<Long, MutableList<LineItem>>(),


): LocalSource {


    override suspend fun getAllFavProducts(customerId: Long): Flow<List<Product>> = flow {
// تحديث القيمة بالمنتجات الحالية لهذا المشتري
        val favProducts = productsMapForCustomer[customerId] ?: emptyList()
        emit(favProducts)
    }

    override suspend fun saveProduct(product: Product) {
        // 1. هات القائمة القديمة للمستخدم أو أنشئ قائمة جديدة لو أول مرة
        val list = productsMapForCustomer.getOrPut(product.customerId) { mutableListOf() }

        // 2. شيل المنتج لو كان موجود قبل كده عشان نحدثه، وسيب باقي المنتجات زي ما هي
        list.removeAll { it.id == product.id }

        // 3. ضيف المنتج الجديد/المُعدل
        list.add(product)
    }

    override suspend fun deleteById(productId: Long, customerId: Long) {
        // 1. البحث عن قائمة المنتجات الخاصة بالمستخدم
        val userProducts = productsMapForCustomer[customerId]

        // 2. حذف المنتج الذي يطابق الـ productId
        userProducts?.removeAll { product -> product.id == productId }

    }

    override suspend fun saveDraftOrderHeader(header: DraftOrderHeaderEntity) {

        val list = draftOrderList.getOrPut(header.customerId) { mutableListOf() }
        list.removeAll { it.draftOrderId == header.draftOrderId }
        list.add(header)
    }


    override suspend fun saveLineItems(items: List<LineItem>) {

        if (items.isEmpty()) return
        val customerId = items.first().customerId

        val list = lineItemList.getOrPut(customerId) { mutableListOf() }

        // تحديث أو إضافة المنتجات
        items.forEach { newItem ->
            list.removeAll { it.idLineItem == newItem.idLineItem }
            list.add(newItem)
        }

    }

    override fun getLineItems(customerId: Long): Flow<List<LineItem>> = flow {

        val lineItems = lineItemList[customerId] ?: emptyList()
        emit(lineItems)

    }

    override suspend fun deleteDraftOrder(draftOrderId: Long, customerId: Long) {

        val draftOrders = draftOrderList[customerId]
        draftOrders?.removeAll { draftOrders -> draftOrders.draftOrderId == draftOrderId }
    }

    override fun getDraftOrderHeader(customerId: Long): Flow<DraftOrderHeaderEntity?> = flow {
        // جلب أحدث كائن في القائمة بأمان، أو null إن كانت القائمة فارغة
        val draftOrderEntity = draftOrderList[customerId]?.lastOrNull()
        emit(draftOrderEntity)

    }

    override fun getDraftOrderWithItems(customerId: Long): Flow<Pair<DraftOrderHeaderEntity?, List<LineItem>>> {
        return combine(
            getDraftOrderHeader(customerId),
            getLineItems(customerId)
        ) { header, items ->
            Pair(header, items)
        }
    }

    override suspend fun saveDraftOrderWithItems(
        header: DraftOrderHeaderEntity,
        items: List<LineItem>
    ) {
        saveDraftOrderHeader(header)
        saveLineItems(items)
    }

    override suspend fun increaseQuantity(lineItemId: Long, customerId: Long) {
        // 1. جلب قائمة سلة المستخدم
        val userItems = lineItemList[customerId] ?: return

        // 2. البحث عن العنصر وتحديث كميته
        val itemIndex = userItems.indexOfFirst { it.idLineItem == lineItemId }
        if (itemIndex != -1) {
            val currentItem = userItems[itemIndex]

            // إنشاء نسخة جديدة بزيادة الكمية (سلوك الكائنات التي لا تتغير Immutable)
            userItems[itemIndex] = currentItem.copy(
                quantity = currentItem.quantity + 1
            )


        }
    }

    override suspend fun decreaseQuantity(lineItemId: Long, customerId: Long) {

        // 1. جلب قائمة سلة المستخدم
        val userItems = lineItemList[customerId] ?: return
        // 2. البحث عن العنصر وتحديث كميته
        val itemIndex = userItems.indexOfFirst { it.idLineItem == lineItemId }
        if (itemIndex != -1) {
            val currentItem = userItems[itemIndex]

            if(currentItem.quantity > 1) {
                // إنشاء نسخة جديدة بزيادة الكمية (سلوك الكائنات التي لا تتغير Immutable)
                userItems[itemIndex] = currentItem.copy(
                    quantity = currentItem.quantity - 1

                )
            }
        }
    }

}