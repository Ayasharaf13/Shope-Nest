package com.example.shopenest.homescreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.shopenest.R


class GridProductAdapter( items:MutableList<Int>) : BaseAdapter() {


    var c: Context? = null
   var items:MutableList<Int> = items

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(pos: Int): Any {
        return items[pos]
    }

    override fun getItemId(p0: Int): Long {

        return 0
    }

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {

        // Declare a variable for the item view
        val itemView: View = view ?: LayoutInflater.from(parent?.context).inflate(R.layout.product_item, parent, false)

        // Find the ImageView inside the layout
        val imageView: ImageView = itemView.findViewById(R.id.imageProduct
        )

        // Set the image resource based on the position
        imageView.setImageResource(items[pos])

        // Return the properly initialized view
        return itemView






    }




}