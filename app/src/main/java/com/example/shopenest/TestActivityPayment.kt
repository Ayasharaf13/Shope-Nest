package com.example.shopenest

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer

class TestActivityPayment : AppCompatActivity() {

    lateinit var paymentButtonContainer: PaymentButtonContainer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_payment)



        paymentButtonContainer = findViewById(R.id.payment_button_container)
        paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    OrderRequest(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = "10.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                Toast.makeText(this, "paymentApproval", Toast.LENGTH_SHORT).show()
                Log.i("My TAg", "OrderId: ${approval.data.orderId}")
            }, onCancel =
            OnCancel {
                Log.i("My TAg", "Byer Cancel order")
                Toast.makeText(this, "paymentCancel", Toast.LENGTH_SHORT).show()

            }, onError =
            OnError { errorInfo ->
                Toast.makeText(this, "paymentError", Toast.LENGTH_SHORT).show()
                Log.i("My TAg", "Error : $errorInfo")

            }


        )

    }
}