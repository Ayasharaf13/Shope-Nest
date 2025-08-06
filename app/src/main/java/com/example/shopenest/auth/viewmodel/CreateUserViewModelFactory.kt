package com.example.shopenest.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.model.RepositoryInterface


class CreateUserViewModelFactory(private val repo: RepositoryInterface): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CreateUserViewModel::class.java)) {
               CreateUserViewModel(repo) as T

            } else {
                throw IllegalArgumentException("View Model is not found")
            }
        }

    }
