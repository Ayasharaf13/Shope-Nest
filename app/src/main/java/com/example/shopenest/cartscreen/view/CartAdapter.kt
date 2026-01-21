package com.example.shopenest.cartscreen.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopenest.R
import com.example.shopenest.model.DraftOrderItemUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CartAdapter(
    val cxt: Context,
    val context: View,
    private val onDeleteClick: (draftOrderId: Long) -> Unit
) :

    ListAdapter<DraftOrderItemUI, CartAdapter.ViewHolder>(CartDiffUtil()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProduct: ImageView = itemView.findViewById(R.id.imgeCartItem)
        val nameProduct: TextView = itemView.findViewById(R.id.tvCartItemProductName)
        val priceProduct: TextView = itemView.findViewById(R.id.tvCartItemProductPrice)
        val productQuantity: TextView = itemView.findViewById(R.id.textDisplaycountDetailsCardItem)
        val deleteCart: ImageView = itemView.findViewById(R.id.imageDeleteCart)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cartitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)

        Glide.with(holder.imageProduct.context)
            .load(product.image)
            .into(holder.imageProduct)

        holder.nameProduct.text = product.title
        holder.productQuantity.text = product.quantity.toString()
        var total = product.quantity * product.price

        holder.priceProduct.text = total.toString() //product.price * quan
        var idProduct = product.productId


        holder.deleteCart.setOnClickListener {

            Log.i("deleteeeeeimage", product.draftOrderId.toString())


            var dialog = MaterialAlertDialogBuilder(cxt)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Keep") { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton("Delete") { dialog, _ ->
                    onDeleteClick(product.draftOrderId)
                    // يفضل إغلاق الديالوج بعد تنفيذ الحذف أيضاً
                    dialog.dismiss()
                }
                .show()

            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(
                android.graphics.Color.RED
            )

            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(
                android.graphics.Color.GRAY
            )
        }


        holder.itemView.setOnClickListener {

            val action: NavDirections =
                CartFragmentDirections.actionCartFragmentToDetailsCartFragment(
                    product.productId,
                    product.price.toString(), product.quantity.toString(), product.lineItemId
                )
            findNavController(context).navigate(action);

        }

    }

    class CartDiffUtil : DiffUtil.ItemCallback<DraftOrderItemUI>() {
        override fun areItemsTheSame(
            oldItem: DraftOrderItemUI,
            newItem: DraftOrderItemUI
        ): Boolean {
            return oldItem.productId == newItem.productId

        }

        override fun areContentsTheSame(
            oldItem: DraftOrderItemUI,
            newItem: DraftOrderItemUI
        ): Boolean {
            return oldItem == newItem
        }
    }
}





