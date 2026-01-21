package com.example.shopenest.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopenest.model.CurrencyRepository
import com.example.shopenest.model.RepInterfaceSetting


    class SettingViewModelFactory (private val repo: CurrencyRepository): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
                SettingViewModel(repo) as T

            } else {
                throw IllegalArgumentException("View Model is not found")
            }
        }

}