package com.example.shopenest.homescreen.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopenest.R
import com.example.shopenest.model.Product
import com.example.shopenest.search.view.SeeAllFragmentDirections


class GenericHomeAdapter(
    val context: View, private val listener: OnFavClickListener? = null,
    val onItemClick: (Long) -> Unit
) : ListAdapter<Product, GenericHomeAdapter.ViewHolder>(CategoryDiffUtil()) {

    var isFavorite = false

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var imageProduct: ImageView = itemview.findViewById(R.id.imageProduct)
        var nameProduct: TextView = itemview.findViewById(R.id.txtNameProduct)
        var addFav: ImageView = itemview.findViewById(R.id.imageFav)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var product = getItem(position)
        val imageUrl = product.image?.src
        if (imageUrl != null) {
            Glide.with(holder.imageProduct.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background) // Optional placeholder
                .into(holder.imageProduct)

        } else {


        }


        var proId = product.id

        holder.nameProduct.text = product.title




        Log.d("Product", "Variants: ${product?.variants}")  // Check if it’s null or populated


        holder.addFav.setOnClickListener {

            isFavorite = !isFavorite

            if (isFavorite) {

                holder.addFav.setImageResource(R.drawable.filledheartred) // أيقونة القلب الأحمر
                listener?.onFavClick(product)
            } else {

                holder.addFav.setImageResource(R.drawable.ic_baseline_favorite_border_24) // أيقونة القلب الفارغ
            }

        }

        holder.itemView.setOnClickListener {

            onItemClick(proId) // نرسل الـ id للفراجمنت فقط


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



