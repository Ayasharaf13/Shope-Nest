package com.example.shopenest.checkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopenest.model.RepositoryInterface


class CheckoutFactory   (private val repo: RepositoryInterface): ViewModelProvider.Factory  {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
               CheckoutViewModel(repo) as T

            } else {
                throw IllegalArgumentException("View Model is not found")
            }
        }

    }


