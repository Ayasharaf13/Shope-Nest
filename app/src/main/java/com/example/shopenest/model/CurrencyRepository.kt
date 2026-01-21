package com.example.shopenest.model

import android.content.Context
import com.example.shopenest.db.LocalSource
import com.example.shopenest.network.RemoteSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CurrencyRepository(context: Context) : RepInterfaceSetting {

    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    // STATEFLOW for real-time updates
    private val _currencyFlow = MutableStateFlow(getSavedCurrency())
    val currencyFlow: StateFlow<String> = _currencyFlow

    // Load saved currency
    override fun getSavedCurrency(): String {
        return prefs.getString("currency", "USD") ?: "USD"
    }

    // Save + Emit update
    override fun saveCurrency(currency: String) {
        // Save to SharedPreferences
        prefs.edit().putString("currency", currency).apply()

        // Notify UI instantly
        _currencyFlow.value = currency
    }


    companion object {
        private var instance: CurrencyRepository? = null
        fun getInstance(context: Context): CurrencyRepository {
            return instance ?: synchronized(this) {

                val temp = CurrencyRepository(context)
                instance = temp
                temp
            }

        }
    }

}
