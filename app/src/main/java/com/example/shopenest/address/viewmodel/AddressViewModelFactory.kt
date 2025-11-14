package com.example.shopenest.address.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopenest.model.RepositoryInterface


class AddressViewModelFactory (private val repo: RepositoryInterface): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
           AddressViewModel(repo) as T

        } else {
            throw IllegalArgumentException("View Model is not found")
        }
    }

}


