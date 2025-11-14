package com.example.shopenest.cartscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopenest.model.RepositoryInterface

class CartViewModelFactory  (private val repo: RepositoryInterface): ViewModelProvider.Factory  {


        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                CartViewModel(repo) as T

            } else {
                throw IllegalArgumentException("View Model is not found")
            }
        }

    }
