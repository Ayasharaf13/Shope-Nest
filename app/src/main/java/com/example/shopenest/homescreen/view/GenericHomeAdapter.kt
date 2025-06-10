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


class GenericHomeAdapter (val context:View, val navFrom :String): ListAdapter<Product, GenericHomeAdapter.ViewHolder>(CategoryDiffUtil()) {


    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var imageProduct: ImageView = itemview.findViewById(R.id.imageProduct)

        var nameProduct :TextView = itemview.findViewById(R.id.txtNameProduct)


        var addFav :ImageView = itemview.findViewById(R.id.imageFav)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
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
        holder.nameProduct.text = product.title
      //  Log.i("product id:::  " , product.id.toString())

        holder.itemView.setOnClickListener {
            if (navFrom == "Home") {
                val  action : NavDirections = HomeFragmentDirections.actionHomeFragmentToDetailsProductFragment(proId)//actionSeeAllFragmentToDetailsProductFragment(proId) // HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(id =idBrand , isItFromTheBrand = true)
                Navigation.findNavController(context).navigate(action);

            }else{
                val action: NavDirections =
                    SeeAllFragmentDirections.actionSeeAllFragmentToDetailsProductFragment(proId) // HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(id =idBrand , isItFromTheBrand = true)
                Navigation.findNavController(context).navigate(action);
            }
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



