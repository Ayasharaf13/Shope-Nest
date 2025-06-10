package com.example.shopenest.utilities

import androidx.room.TypeConverter
import com.example.shopenest.model.ImageProduct
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {



    @TypeConverter
    fun fromImageProduct(imageProduct: ImageProduct): String {
        return Gson().toJson(imageProduct)
    }

    @TypeConverter
    fun toImageProduct(json: String): ImageProduct {
        val type = object : TypeToken<ImageProduct>() {}.type
        return Gson().fromJson(json, type)
    }


    @TypeConverter
    fun fromImageProductList(images: List<ImageProduct>): String {
        return Gson().toJson(images)
    }

    @TypeConverter
    fun toImageProductList(json: String): List<ImageProduct> {
        val type = object : TypeToken<List<ImageProduct>>() {}.type
        return Gson().fromJson(json, type)
    }


}