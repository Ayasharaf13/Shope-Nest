package com.example.shopenest.model



data class
CustomerBody(

    val first_name: String = "",
    val email: String,
    val phone: String ="",
    val verified_email: Boolean = true,
    val addresses: List<AddressBody> = emptyList(),
    val send_email_welcome: Boolean = false


)



