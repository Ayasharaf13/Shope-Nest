package com.example.shopenest.cartscreen.view

import android.annotation.SuppressLint
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

     class CartAdapter (val productQuantity:Int,private var totalPriceAfterDiscount: String?): ListAdapter<Product, CartAdapter.ViewHolder>(CartDiffUtil()) {


        inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

            var imageProduct: ImageView = itemview.findViewById(R.id.imgeCartItem)

            var nameProduct : TextView = itemview.findViewById(R.id.tvCartItemProductName)

            var priceProduct : TextView = itemview.findViewById(R.id.tvCartItemProductPrice)

            var productQuantity : TextView = itemview.findViewById(R.id.textDisplaycountDetailsCardItem)




        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.cartitem, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder:ViewHolder, position: Int) {
            //https://www.imgonline.com.ua/examples/rays-of-light-in-the-sky.jpg
//https://cdn.shopify.com/s/files/1/0792/3626/8322/products/7883dc186e15bf29dad696e1e989e914.jpg?v=1689453539
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
            holder.productQuantity.text = productQuantity.toString()
            holder.nameProduct.text = product.title

            val variant = product.variants.firstOrNull()
          //  holder.priceProduct.text = variant?.price?: "N/A"

            holder.priceProduct.text = totalPriceAfterDiscount.toString()

            // val product = apiResponse.products.firstOrNull()
            Log.d("Product", "Variants: ${product?.variants}")  // Check if it’s null or populated

            //  Log.i("product id:::  " , product.id.toString())



        }


         @SuppressLint("NotifyDataSetChanged")
         fun updateTotalPrice(newTotalPrice: String?) {
             totalPriceAfterDiscount = newTotalPrice
             notifyDataSetChanged() // refresh all items

         }



        class CartDiffUtil() : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }


        }

    }




