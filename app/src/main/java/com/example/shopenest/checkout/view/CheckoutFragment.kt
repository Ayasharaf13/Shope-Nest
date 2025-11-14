package com.example.shopenest.checkout.view

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopenest.R
import com.example.shopenest.address.view.DisplaySavedAddressFragmentArgs
import com.example.shopenest.address.viewmodel.AddressViewModel
import com.example.shopenest.address.viewmodel.AddressViewModelFactory
import com.example.shopenest.auth.view.WelcomeFragmentDirections
import com.example.shopenest.checkout.viewmodel.CheckoutFactory
import com.example.shopenest.checkout.viewmodel.CheckoutViewModel
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.CustomerData
import com.example.shopenest.model.CustomerResponse

import com.example.shopenest.model.DraftOrder
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.google.android.material.snackbar.Snackbar
import com.paypal.checkout.PayPalCheckout
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
import com.paypal.checkout.paymentbutton.PaymentButton
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CheckoutFragment : Fragment() {

    lateinit var payCash: RadioButton
    lateinit var payPaypal: RadioButton
    lateinit var placeOrderButton: Button
    lateinit var paypalContainer: PaymentButtonContainer
    lateinit var addressCheckout: TextView
    lateinit var changeAddress: TextView
    lateinit var addressFactory: AddressViewModelFactory
    lateinit var addressViewModel: AddressViewModel
    lateinit var checkoutViewModel: CheckoutViewModel
    lateinit var checkoutFactory: CheckoutFactory
    private var isCashSelected = false
   var customer : CustomerData?  = null
    lateinit var pref: SharedPreferences
    lateinit var  paymentButtonContainer:PaymentButtonContainer
   // private val sharedViewModel: CheckoutViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
        addressFactory = AddressViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )

        addressViewModel =
            ViewModelProvider(this, addressFactory).get(AddressViewModel::class.java)


          checkoutFactory = CheckoutFactory(
              Repository.getInstance(
                  ShoppingClient.getInstance(),
                  ConcreteLocalSource.getInstance(requireContext())
              )
          )


        checkoutViewModel = ViewModelProvider(requireActivity(), checkoutFactory).get(CheckoutViewModel::class.java)



        pref = requireContext().
        getSharedPreferences("MyPref", Context.MODE_PRIVATE) // 0 - for private mode


        val customerId = pref.getString("customer_id", null)


        customerId?.toLongOrNull()?.let { id ->
            checkoutViewModel.getCustomerInfo(id)
            addressViewModel.getAddresses(id)
            Log.e("CustomerCgeckoutSuccusess", "customer_id is null or invalid $id")
        } ?: run {
            Log.e("CustomerCgeckout", "customer_id is null or invalid")
        }





    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentButtonContainer = view.findViewById(R.id.payment_button_container)
        payCash = view.findViewById(R.id.payCash)
        payPaypal = view.findViewById(R.id.payPaypal)
        placeOrderButton = view.findViewById(R.id.placceOrderbutton)
        paypalContainer = view.findViewById(R.id.payment_button_container)
        addressCheckout = view.findViewById(R.id.addressCheckout)
        changeAddress = view.findViewById(R.id.changeAddress)

       // addressViewModel.getDefaultAddress()
     //   val args: CheckoutFragmentArgs by navArgs()
      //  val addressID = args.idAddress



        /*lifecycleScope.launchWhenStarted {
            addressViewModel.defaultAddress.collect { default ->
                if (default != null) {
                    addressCheckout.text = "${default.address1}\n${default.phone}"
                } else {
                    addressCheckout.text = "No default address selected"
                }
            }
        }

         */



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.address.collectLatest { list ->
                    val default = list?.firstOrNull { it.isDefault == true }

                    if (default != null) {
                        addressCheckout.text = "${default.address1}\n${default.phone}"
                    } else {
                        addressCheckout.text = "No default address selected yet"
                    }
                }
            }
        }








        // addressCheckout.text = addressID

        /* if (addressID != null) {


         // var fulladdress =  "${addressSetDefault.street}, ${addressSetDefault.city}," +
                //  " ${addressSetDefault.zip} ,${addressSetDefault.country} \n\n${addressSetDefault.phone} "

            addressCheckout.text = fulladdress

        }

        */
        ////////////////////
       viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.addressID.collect { address ->
                    // This block is called every time the data changes
                    if (address != null ) {

                         var fulladdress =  "${address.address1}, ${address.city}," +
                          " ${address.zip} ,${address.country} \n\n${address.phone} "
                        addressCheckout.text = fulladdress



                    } else {
                        // Handle empty state
                        //   recyclerAddress.visibility = View.GONE
                        Log.i("NoAddress", "NoAddressToDisplay")

                    }
                }
            }
        }



         changeAddress.setOnClickListener {

             val action = CheckoutFragmentDirections.
             actionCheckoutFragmentToDisplaySavedAddressFragment("checkout")
             findNavController().navigate(action)

         }




  /*  if(paypalContainer.isPressed){

        Toast.makeText(requireContext(),"payByale",Toast.LENGTH_SHORT).show()

    }

        paypalContainer.setOnClickListener {
            Toast.makeText(requireContext(),"payByale",Toast.LENGTH_SHORT).show()

        }

       */



        fun showAddAddressSnackbar() {
           // checkoutViewModel.needUpdateCustomer.value = true
            checkoutViewModel.setUpdate(true)

            Snackbar.make(view, "You need a shipping address", Snackbar.LENGTH_LONG)
                .setAction("Add Address") {

                    findNavController().navigate(R.id.action_checkoutFragment_to_adressFragment)

                }.show()
        }



        fun checkProfileComplete (cashSelect:Boolean) {
            lifecycleScope.launchWhenStarted {
                checkoutViewModel.customerResponse.collect { response ->
                    if (response != null && response.isSuccessful) {
                        //customer.firstName I check is isprofilecompleted by this fieled only
                        customer = response.body()?.customer
                        if (customer?.first_name.isNullOrBlank() && cashSelect) {

                            //nave to add address and update customer

                            showAddAddressSnackbar()

                            Log.i("isselectInside", isCashSelected.toString())
                            Log.i("isselectInside", customer?.first_name.toString())
                        } else {
                            Log.i("isselectInsidelll", isCashSelected.toString())
                            // payment pay success


                        }
                        Log.i(
                            "CustomerInfo",
                            "Name: ${customer?.first_name} ${customer?.first_name}"
                        )
                        Log.i("CustomerInfo", "Email: ${customer?.email}")
                        Log.i("CustomerInfo", "Phone: ${customer?.phone}")

                        // ✅ You can show data in UI:
                        /* tvName.text = customer?.first_name
                    tvEmail.text = customer?.email
                    tvPhone.text = customer?.phone

                    */

                    } else {
                        Log.e("CustomerInfo", "Failed: ${response?.code()}")
                    }
                }
            }
        }

        fun showPaymentSuccessDialog() {

            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_payment)
            dialog.setCancelable(false) // User can't close by tapping outside

            val continueBtn = dialog.findViewById<Button>(R.id.btnContinue)

            continueBtn.setOnClickListener {
                dialog.dismiss()
                // ✅ Navigate or close fragment
                findNavController().navigate(R.id.action_checkoutFragment_to_homeFragment)
            }

            dialog.show()
        }


        placeOrderButton.setOnClickListener {

            if(isCashSelected && customer?.first_name.isNullOrBlank()  ){

                // snack to add & update
                checkProfileComplete(isCashSelected)



            }else {
                showPaymentSuccessDialog()
            }
        }




        // When Cash is selected
             payCash.setOnCheckedChangeListener { _, isChecked ->
                 if (isChecked) {
                    isCashSelected = isChecked
                     placeOrderButton.visibility = View.VISIBLE
                     paypalContainer.visibility = View.GONE
                        checkProfileComplete(isCashSelected)
                 }
             }

        Log.i("isselect",isCashSelected.toString())

// When PayPal is selected
        payPaypal.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                placeOrderButton.visibility = View.GONE
               paypalContainer.visibility = View.VISIBLE
            }
        }





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
                                Amount(currencyCode = CurrencyCode.USD, value = "5.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                Toast.makeText(requireContext(),"paymentApproval",Toast.LENGTH_SHORT).show()
                Log.i("My TAg", "OrderId: ${approval.data.orderId}")
                checkoutViewModel.completeDraftOrder( 1214597595426)
                showPaymentSuccessDialog()
            }, onCancel =
            OnCancel {
                Log.i("My TAg", "Byer Cancel order")
                Toast.makeText(requireContext(),"paymentCancel",Toast.LENGTH_SHORT).show()

            }, onError =
            OnError {errorInfo ->
                Toast.makeText(requireContext(),"paymentError",Toast.LENGTH_SHORT).show()
                Log.i("My TAg", "Error : $errorInfo")

            }



        )




    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckoutFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}