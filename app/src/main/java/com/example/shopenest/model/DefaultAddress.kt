package com.example.shopenest.model



data class DefaultAddress(

 val id: Long,
val first_name: String,
val customer_id:Long,
val phone: String,
val address1: String,
val city: String,
val zip: String,
val country: String,
val default: Boolean = false

)


