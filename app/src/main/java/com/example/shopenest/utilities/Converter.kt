package com.example.shopenest.utilities

import androidx.room.TypeConverter
import com.example.shopenest.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {


   private val gson = Gson()
    @TypeConverter
    fun fromImageProduct(imageProduct: ImageProduct): String {
        return gson.toJson(imageProduct)
    }

    @TypeConverter
    fun toImageProduct(json: String): ImageProduct {
        val type = object : TypeToken<ImageProduct>() {}.type
        return gson.fromJson(json, type)
    }


    @TypeConverter
    fun fromImageProductList(images: List<ImageProduct>): String {
        return gson.toJson(images)
    }

    @TypeConverter
    fun toImageProductList(json: String): List<ImageProduct> {
        val type = object : TypeToken<List<ImageProduct>>() {}.type
        return gson.fromJson(json, type)
    }


    @TypeConverter
    fun fromVariantList(value: List<Variant>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toVariantList(value: String): List<Variant> {
        val type = object : TypeToken<List<Variant>>() {}.type
        return gson.fromJson(value, type)
    }




        @TypeConverter
        fun fromAny(value: Any?): String? {
            return Gson().toJson(value)
        }

        @TypeConverter
        fun toAny(value: String?): Any? {
            return Gson().fromJson(value, Any::class.java)
        }



    // --------------------- AddressBody (shipping / billing) ----------------------
    @TypeConverter
    fun fromAddressBody(value: AddressBody?): String? =
        value?.let { gson.toJson(it) }

    @TypeConverter
    fun toAddressBody(value: String?): AddressBody? {
        if (value.isNullOrEmpty()) return null
        val type = object : TypeToken<AddressBody>() {}.type
        return gson.fromJson(value, type)
    }



    // --------------------- CustomerAddress (default_address) ----------------------
    @TypeConverter
    fun fromCustomerAddress(value: CustomerAddress?): String? =
        value?.let { gson.toJson(it) }

    @TypeConverter
    fun toCustomerAddress(value: String?): CustomerAddress? {
        if (value.isNullOrEmpty()) return null
        val type = object : TypeToken<CustomerAddress>() {}.type
        return gson.fromJson(value, type)
    }

    // --------------------- AppliedDiscount ----------------------
    @TypeConverter
    fun fromAppliedDiscount(value: AppliedDiscount?): String? =
        value?.let { gson.toJson(it) }

    @TypeConverter
    fun toAppliedDiscount(value: String?): AppliedDiscount? {
        if (value.isNullOrEmpty()) return null
        val type = object : TypeToken<AppliedDiscount>() {}.type
        return gson.fromJson(value, type)
    }


    @TypeConverter
    fun fromPropertyList(list: List<Property>?): String? {
        return list?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toPropertyList(json: String?): List<Property>? {
        if (json.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<Property>>() {}.type
        return gson.fromJson(json, type)
    }









}