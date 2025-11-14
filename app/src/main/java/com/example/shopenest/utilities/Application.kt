package com.example.shopenest.utilities

import android.app.Application
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

class App : Application() {

    val cliendID = "AZC1TsaB5pK9HEc-TfxV6YHZy7stl4G3D_tXP88ErRF25c7Z9G7aDdoJliH-rPIYuHN9byuvEQvmXkwL"
    val returnURL = "com.example.shopenest://paypalpay"


    override fun onCreate() {
        super.onCreate()

        val config = CheckoutConfig(
            application = this,
            clientId = cliendID,
            environment = Environment.SANDBOX,
            returnUrl = returnURL,
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true,
                showWebCheckout = false
            )
        )
        PayPalCheckout.setConfig(config)
    }




}