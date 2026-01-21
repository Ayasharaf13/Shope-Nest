package com.example.shopenest.setting.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shopenest.model.CurrencyRepository
import com.example.shopenest.model.RepInterfaceSetting
import com.example.shopenest.model.RepositoryInterface


class SettingViewModel (private val repo: CurrencyRepository) : ViewModel() {


    val currencyFlow = repo.currencyFlow // UI listens

    fun updateCurrency(currency: String) {
        repo.saveCurrency(currency)
    }

}