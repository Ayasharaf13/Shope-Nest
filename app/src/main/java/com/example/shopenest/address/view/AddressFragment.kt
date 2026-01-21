package com.example.shopenest.address.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopenest.R
import com.example.shopenest.address.viewmodel.AddressViewModel
import com.example.shopenest.address.viewmodel.AddressViewModelFactory
import com.example.shopenest.cartscreen.view.CartFragmentArgs
import com.example.shopenest.checkout.viewmodel.CheckoutFactory
import com.example.shopenest.checkout.viewmodel.CheckoutViewModel

import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.model.*

import com.example.shopenest.network.ShoppingClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AddressFragment : Fragment() {

    lateinit var buttonMap: Button
    lateinit var savebutton: Button
    lateinit var pref: SharedPreferences
    lateinit var editTextName: TextInputEditText
    lateinit var textInputLayoutName: TextInputLayout
    lateinit var textInputLayoutZipCode: TextInputLayout
    lateinit var textInputLayoutCity: TextInputLayout
    lateinit var textInputLayoutCountry: TextInputLayout
    lateinit var textInputLayoutAddress: TextInputLayout
    lateinit var textInputLayoutPhone: TextInputLayout
    lateinit var editTextZipCode: TextInputEditText
    lateinit var editTextCity: TextInputEditText
    lateinit var editTextCountry: TextInputEditText
    lateinit var editTextAddress: TextInputEditText
    lateinit var editTextPhone: TextInputEditText


    lateinit var addressViewModel: AddressViewModel
    lateinit var addressFactory: AddressViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adress, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonMap = view.findViewById(R.id.buttonMap)



        textInputLayoutName = view.findViewById(R.id.textInputFullName)
        textInputLayoutAddress = view.findViewById(R.id.textInputAddress)
        textInputLayoutZipCode = view.findViewById(R.id.textInputPinCode)
        textInputLayoutCountry = view.findViewById(R.id.textInputCountry)
        textInputLayoutCity = view.findViewById(R.id.textInputCity)
        textInputLayoutPhone = view.findViewById(R.id.textInputPhone)


        editTextName = view.findViewById(R.id.editTextFullName)
        editTextAddress = view.findViewById(R.id.editTextAddress)
        editTextZipCode = view.findViewById(R.id.editTextPinCode)
        editTextCity = view.findViewById(R.id.editTextCity)
        editTextCountry = view.findViewById(R.id.editTextCountry)
        editTextPhone = view.findViewById(R.id.editTextPhone)
        savebutton = view.findViewById(R.id.buttonSave)
        val toolbar =
            view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.myCustomToolbarAddress)


        addressFactory = AddressViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(), ConcreteLocalSource.getInstance(requireContext())
            )
        )
        addressViewModel = ViewModelProvider(this, addressFactory).get(AddressViewModel::class.java)


        var checkoutFactory = CheckoutFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(), ConcreteLocalSource.getInstance(requireContext())
            )
        )


        var checkoutViewModel =
            ViewModelProvider(requireActivity(), checkoutFactory).get(CheckoutViewModel::class.java)


        buttonMap.setOnClickListener {

            val action = AddressFragmentDirections.actionAdressFragmentToMapFragment()
            findNavController().navigate(action)

        }

        pref = requireContext().getSharedPreferences(
            "MyPref", Context.MODE_PRIVATE
        ) // 0 - for private mode

        val customerId = pref.getString("customer_id", null)

        val customerEmail = pref.getString("email", null)

        val args: AddressFragmentArgs by navArgs()
        val address = args.address
        var updateCustomer = false




        lifecycleScope.launch {
            checkoutViewModel.needUpdateCustomer.collect { shouldUpdate ->
                updateCustomer = shouldUpdate
            }
        }


        // Step 1: Split by comma

        Toast.makeText(requireContext(), "AllAddress : $address", Toast.LENGTH_SHORT).show()

        if (address != "undefine") {

            val parts = address.split(",")

            //3
            var sizeParts = parts.size - 3

            // From 0 → sizeParts - 1
            var i = 0
            var street = ""
            while (i <= sizeParts) {
                println("i??? = $i") // 0,1,2
                street += parts[i].trim() + " "   // add each part

                i++
            }


            val country = parts.last().trim()   // "Egypt"
            val cityAndZip = parts[parts.size - 2].trim() // "Cairo Governorate 565487"

            // Step 2: Extract city and zipcode
            val cityWords = cityAndZip.split(" ")
            val city = cityWords[0]   // "Cairo"
            val zipcode = cityWords.last() // "565487"


            println("Street: $street")
            editTextAddress.setText(street)
            editTextCity.setText(city)
            editTextZipCode.setText(zipcode)
            editTextCountry.setText(country)



            fun checkAllFields(): Boolean {
                // Create a list of (TextInputEditText, TextInputLayout, errorMessage)
                val fields = listOf(
                    Triple(editTextName, textInputLayoutName, "Name is required"),
                    Triple(editTextZipCode, textInputLayoutZipCode, "Zip code is required"),
                    Triple(editTextCity, textInputLayoutCity, "City is required"),
                    Triple(editTextCountry, textInputLayoutCountry, "Country is required"),
                    Triple(editTextAddress, textInputLayoutAddress, "Address is required"),
                    Triple(editTextPhone, textInputLayoutPhone, "Phone is required")
                )

                for ((editText, layout, message) in fields) {
                    // Clear previous error first (good UX)
                    layout.error = null

                    if (editText.text.isNullOrBlank()) {
                        layout.error = message
                        return false
                    }
                }

                // Special check for phone number length
                if (editTextPhone.text!!.length < 11) {
                    textInputLayoutPhone.error = "Phone must be at least 11 digits"
                    return false
                }

                return true
            }

            fun createAddressCustomer() {
                val addressCustomer = AddressBody(
                    first_name = editTextName.text.toString(),
                    zip = editTextZipCode.text.toString(),
                    address1 = editTextAddress.text.toString(),
                    city = editTextCity.text.toString(),
                    country = editTextCountry.text.toString(),
                    phone = editTextPhone.text.toString()
                )

                customerId?.toLongOrNull()?.let { id ->
                    addressViewModel.createCustomerAddress(
                        id, CreateCustomerAddressRequest(
                            addressCustomer
                        )
                    )
                    Log.e("CustomerSuccess", "customer_id is null or invalid  + $id")
                } ?: run {
                    Log.e("Custome>>>r", "customer_id is null or invalid")
                }

            }

            savebutton.setOnClickListener {
                Toast.makeText(requireContext(), "Save Button :", Toast.LENGTH_SHORT).show()
                Log.i("nameUpdate__::  ", updateCustomer.toString())

                if (checkAllFields() && updateCustomer) {
                    // suppose  take customerAddress from API Responce so id , custom_id is not real
                    Log.i("nameUpdate::  ", updateCustomer.toString())

                    createAddressCustomer()
                    customerId?.toLongOrNull()?.let { id ->
                        addressViewModel.updateCustomer(
                            id, CustomerRequest(
                                CustomerBody(
                                    first_name = editTextName.text.toString(),
                                    phone = editTextPhone.text.toString(),
                                    email = customerEmail.toString()
                                )
                            )
                        )
                        Log.i("name::  ", "customerupdatedone ")
                    } ?: run {
                        Log.e("Customer", "customer_id is null or invalid")
                    }
                    val action =
                        AddressFragmentDirections.actionAdressFragmentToDisplaySavedAddressFragment(
                            "address",

                            )
                    findNavController().navigate(action)


                } else if (checkAllFields() && !updateCustomer) {

                    // ✅ Collect server response BEFORE navigation
                    lifecycleScope.launch {
                        createAddressCustomer()
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            addressViewModel.createCustomerAddress.collect { response ->

                                if (response != null) {

                                    val namee = response.customer_address.first_name
                                    // ✅ Success response from API
                                    val createdAddress =
                                        response.customer_address  // adjust according to your model
                                    Log.e(
                                        "responcerehhh",
                                        " Suucess responce not is null or invalid $namee"
                                    )
                                    val action =
                                        AddressFragmentDirections.actionAdressFragmentToDisplaySavedAddressFragment(
                                            "address",

                                            )

                                    findNavController().navigate(action)

                                } else {
                                    // ✅ When ViewModel sets null on error

                                    Snackbar.make(
                                        requireView(),
                                        "Failed to create address",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                    Log.e(
                                        "responcrehhh", "Not Suucess responce not is null or inva"
                                    )
                                }
                            }
                        }

                        println("City: $city")
                        println("Zipcode: $zipcode")
                        println("Country: $country")
                    }


                }
            }

        }



        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()

        }

    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = AddressFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}