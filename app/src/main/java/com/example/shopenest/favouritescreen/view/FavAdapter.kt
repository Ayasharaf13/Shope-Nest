package com.example.shopenest.favouritescreen.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopenest.R
import com.example.shopenest.model.Product

import com.google.android.material.dialog.MaterialAlertDialogBuilder


class FavAdapter  (val cxt:Context ,val context:View, val navFrom :String ,
                   private val onRemoveClick: (Long) -> Unit,
                   private val onItemClick: (Long) -> Unit): ListAdapter<Product, FavAdapter.ViewHolder>(CategoryDiffUtil()) {


        inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

            var imageProduct: ImageView = itemview.findViewById(R.id.productImage)

            var nameProduct :TextView = itemview.findViewById(R.id.productTitle)

          //  var addFav :ImageView = itemview.findViewById(R.id.imageFav)

            var btnDelete:ImageView = itemview.findViewById(R.id.btnRemove)



        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.favitem, parent, false)
            return ViewHolder(view)
        }




        override fun onBindViewHolder(holder:ViewHolder, position: Int) {

            var product = getItem(position)
            val imageUrl = product.image?.src
            if (imageUrl != null ) {
                Glide.with(holder.imageProduct.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background) // Optional placeholder
                    .into(holder.imageProduct)

            }else{


            }


            var proId =  product.id

            holder.nameProduct.text = product.title


            holder.btnDelete.setOnClickListener {
               var dialog = MaterialAlertDialogBuilder(cxt)
                    .setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete this product?")
                    .setPositiveButton("Keep") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNegativeButton("Delete") { dialog, _ ->
                        onRemoveClick(proId)
                        // يفضل إغلاق الديالوج بعد تنفيذ الحذف أيضاً
                        dialog.dismiss()
                    }
                    .show()

                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(
                    android.graphics.Color.RED)

                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(
                    android.graphics.Color.GRAY)

            }



            Log.d("Product", "Variants: ${product?.variants}")  // Check if it’s null or populated


            holder.itemView.setOnClickListener {

                onItemClick(proId)

            }

        }



        class CategoryDiffUtil() : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }


        }



}










