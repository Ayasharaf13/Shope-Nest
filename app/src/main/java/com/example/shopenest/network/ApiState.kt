package com.example.shopenest.network

import com.example.shopenest.model.Brands


sealed class ApiState {

    class Success (val data: Brands):ApiState()
    class Failure (val msg:Throwable) : ApiState()
    object Loading :ApiState()


}
