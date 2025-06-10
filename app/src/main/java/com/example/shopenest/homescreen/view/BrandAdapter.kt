package com.example.shopenest.homescreen.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopenest.R
import com.example.shopenest.model.Brands
import com.example.shopenest.model.SmartCollection


class BrandAdapter (var context:View): ListAdapter<SmartCollection, BrandAdapter.ViewHolder>(BrandDiffUtil()) {


    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var imageBrand: ImageView = itemview.findViewById(R.id.imageViewBrand)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {

        val brand = getItem(position)
        Glide.with(holder.imageBrand.context)
            .load(brand.image.src)
            .placeholder(R.drawable.ic_launcher_background) // Optional placeholder
            .into(holder.imageBrand)
         var idBrand =    brand.id


           holder.imageBrand.setOnClickListener {
               Log.i("brandId: ",idBrand.toString())
               val  action : NavDirections =  HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(id =idBrand , isItFromTheBrand = true)
               Navigation.findNavController(context).navigate(action);


           }


    }

    class BrandDiffUtil() : DiffUtil.ItemCallback<SmartCollection>() {
        override fun areItemsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SmartCollection, newItem: SmartCollection): Boolean {
            return oldItem == newItem
        }


    }

}





