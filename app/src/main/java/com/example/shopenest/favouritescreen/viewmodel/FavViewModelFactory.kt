package com.example.shopenest.favouritescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopenest.cartscreen.viewmodel.CartViewModel
import com.example.shopenest.model.RepositoryInterface


class FavViewModelFactory  (private val repo: RepositoryInterface): ViewModelProvider.Factory  {


        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
                FavViewModel(repo) as T

            } else {
                throw IllegalArgumentException("View Model is not found")
            }
        }

    }









