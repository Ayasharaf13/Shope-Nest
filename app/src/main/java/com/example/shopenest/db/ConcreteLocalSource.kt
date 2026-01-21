package com.example.shopenest.db

import android.annotation.SuppressLint
import android.content.Context

import com.example.shopenest.model.CustomerAddress
import com.example.shopenest.model.DraftOrderHeaderEntity
import com.example.shopenest.model.LineItem

import com.example.shopenest.model.Product
import com.example.shopenest.utilities.CustomerPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


class ConcreteLocalSource : LocalSource {

    val context: Context
    var shoppingDao: ShoppingDao
    //  var addressDao:AddressDao

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var localsource: ConcreteLocalSource? = null

        fun getInstance(con: Context): ConcreteLocalSource {
            if (localsource == null) {
                localsource = ConcreteLocalSource(con)
            }
            return localsource as ConcreteLocalSource
        }

    }

    private constructor (con: Context) {

        this.context = con
        val db: AppDataBase = AppDataBase.getInstance(context.applicationContext)
        shoppingDao = db.getProdDao()
        //  addressDao = db.getAddressdDao()


    }

    override suspend fun getAllFavProducts(customerId: Long): Flow<List<Product>> = flow {

        emitAll(shoppingDao.getFavProducts(customerId))
    }

    override suspend fun saveProduct(product: Product) {
        var id = CustomerPref(context).getCustomerId()
        id?.let { product.customerId = it.toLong() }

        shoppingDao.saveProduct(product)
    }

    override suspend fun deleteById(productId: Long, customerId: Long) {
        shoppingDao.deleteById(productId, customerId)

    }

    override suspend fun saveDraftOrderHeader(header: DraftOrderHeaderEntity) {
        shoppingDao.saveDraftOrderHeader(header)
    }

    override suspend fun saveLineItems(items: List<LineItem>) {
        shoppingDao.saveLineItems(items)
    }

    override fun getLineItems(customerId: Long): Flow<List<LineItem>> = flow {

        emitAll(shoppingDao.getLineItems(customerId))
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long, customerId: Long) {
        shoppingDao.deleteDraftOrder(draftOrderId, customerId)
    }

    override fun getDraftOrderHeader(

        customerId: Long
    ): Flow<DraftOrderHeaderEntity?> = flow {
        emitAll(shoppingDao.getDraftOrderHeader(customerId))
    }

    override fun getDraftOrderWithItems(
        customerId: Long
    ): Flow<Pair<DraftOrderHeaderEntity?, List<LineItem>>> {

        return combine(
            shoppingDao.getDraftOrderHeader(customerId),
            shoppingDao.getLineItems(customerId)
        ) { header, items ->
            header to items
        }
    }

    /* override fun getDraftOrderWithItems(customerId: Long): Flow<Pair<DraftOrderHeaderEntity?, List<LineItem>>>  = flow {

         emitAll(shoppingDao.getDraftOrderWithItems(customerId))
     }

     */

    override suspend fun saveDraftOrderWithItems(
        header: DraftOrderHeaderEntity,
        items: List<LineItem>
    ) {
        shoppingDao.saveDraftOrderWithItems(header, items)
    }

    override suspend fun increaseQuantity(lineItemId: Long, customerId: Long) {

        shoppingDao.increaseQuantity(lineItemId, customerId)

    }

    override suspend fun decreaseQuantity(lineItemId: Long, customerId: Long) {

        shoppingDao.decreaseQuantity(lineItemId, customerId)

    }


}