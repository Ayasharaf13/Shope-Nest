package com.example.shopenest.model

data class Customer(
   val  email: String,
   val id:Long = 0L // <- Include this to get the generated ID
)
