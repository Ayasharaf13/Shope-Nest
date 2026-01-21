package com.example.shopenest.utilities

import android.content.Context



class CustomerPref(context: Context) {


   private val   pref = context.getSharedPreferences(
    "MyPref",
    Context.MODE_PRIVATE
    )

  private  var getCustomerId = pref.getString("customer_id", null)


    fun getCustomerId(): String? {
        return getCustomerId
    }



}
