package com.example.shopenest.auth.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.credentials.CreateCustomCredentialRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shopenest.MainActivity
import com.example.shopenest.R
import com.example.shopenest.auth.viewmodel.CreateUserViewModel
import com.example.shopenest.auth.viewmodel.CreateUserViewModelFactory
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.Customer
import com.example.shopenest.model.Repository
import com.example.shopenest.model.ResponseCustomer
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.utilities.Constants
import com.example.shopenest.utilities.GoogleAuthHelper
import com.example.shopenest.utilities.ValidationUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {

    lateinit var userName: EditText
   lateinit var pref: SharedPreferences
    lateinit var userPassword: EditText
    lateinit var confirmPassword: EditText
    lateinit var mAuth: FirebaseAuth
    lateinit var buttonSignUp: Button
    lateinit var buttonSignUPGoogle: ImageButton
    lateinit var createCustomerViewModel: CreateUserViewModel
    lateinit var customerFactory:CreateUserViewModelFactory

    private lateinit var credentialManager: CredentialManager
    private lateinit var googleAuthHelper: GoogleAuthHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

         pref = requireContext().
            getSharedPreferences("MyPref", Context.MODE_PRIVATE) // 0 - for private mode



        googleAuthHelper = GoogleAuthHelper(
            context = requireContext(),
            lifecycleOwner = requireActivity(),

            onSuccess = { user ->
                // ✅ User signed in successfully
                Log.d("GoogleAuth", "Welcome: ${user?.email}")
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                // findNavController().navigate(R.id.action_to_homeFragment)
            },

            onError = { errorMessage ->
                // ❌ Sign-in failed
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }


        )


        // Extract credential from the result returned by Credential Manager


        var firbaseapp = FirebaseApp.getApps(requireContext())
        if (firbaseapp.isEmpty()) {
            Log.e("Firebase", "Firebase is not initialized!");
        } else {
            Log.d("Firebase", "Firebase initialized successfully!");
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName = view.findViewById(R.id.editTextTextPersonNameSignUp)
        userPassword = view.findViewById(R.id.editTextTextPasswordSignUp)
        confirmPassword = view.findViewById(R.id.editTextConfirmPasswordSignUp)
        buttonSignUp = view.findViewById(R.id.buttonSignUp)
        buttonSignUPGoogle= view.findViewById(R.id.googleButton)


        customerFactory = CreateUserViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            ))
        createCustomerViewModel =
            ViewModelProvider(this, customerFactory).get(CreateUserViewModel::class.java)

   fun createCustomer(email: String) {

       createCustomerViewModel.createCustomer(ResponseCustomer(Customer(email)))

        lifecycleScope.launchWhenStarted {
            createCustomerViewModel.customer.collect { response ->
                if (response != null) {
                    // ✅ User was created successfully
                    Log.d("CreateCustomer_Id", "Customer created: ${response.body()?.customer?.id}")
                    Log.d("CreateCustomer", "Customer created: ${response.body()?.customer?.email}")
                    val customerId = response?.body()?.customer?.id
                    val customerEmail = response?.body()?.customer?.email


                    if (customerId != null && customerEmail != null) {
                        // Save only after successful creation
                        pref.edit()
                            .putString("customer_id", customerId.toString())
                            .putString("email", customerEmail)
                            .apply()

                        Toast.makeText(requireContext(), "Customer created and saved! $customerId :: $customerEmail", Toast.LENGTH_SHORT).show()
                    }

                    //   Log.i ("customer_id", response.body().customer.email.toString())
                    // Save to SharedPreferences or navigate
                } else {
                    // ❌ Creation failed or still loading
                    Toast.makeText(requireContext(), "Customer creation failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


        fun checkCreateCustomer(email: String) {

            val editor = pref.edit()
            val savedEmail = pref.getString("email", null)
            val savedCustomerId = pref.getString("customer_id", null)

            if (savedCustomerId.isNullOrEmpty() || savedEmail.isNullOrEmpty()) {

                Log.i("emailtest: ","$savedEmail")
                createCustomer(email)

            } else if (savedEmail != email) {
                editor.clear().apply()
                createCustomer(email)
                Log.i("emailtestNotequal: ","$savedEmail")

            } else {
                Toast.makeText(requireContext(), "Same Email is correct ", Toast.LENGTH_SHORT)
                    .show()
                Log.i("emailtestElse: ","$savedEmail")

            }
        }



     // createCustomerViewModel.searchCustomerByEmail("email:ayasharaf444@yahoo.com")
        lifecycleScope.launchWhenStarted{
        createCustomerViewModel.searchCustomer.collect{response->

            if(response !=null ){
                Log.d("SSCustomerByEmail: ", "sSCustomer created: ${response.body()?.customers?.get(0)}")

            }else{
                Log.d("SSCustomerByEmailError: ", "SsCustomer created failed")

            }
            }
        }


        createCustomerViewModel.getCountCustomer()


        lifecycleScope.launchWhenStarted {
            createCustomerViewModel.countCustomer.collect { count ->

                Log.i("countCustomer: ", count.toString())

            }
        }





        fun createNewAccount() {
            val email = userName.text.toString()
            val password = userPassword.text.toString()
            val confirmPassword = confirmPassword.text.toString()

            //  if (confirmPassword == password){

            if (ValidationUtils.validateUserInput(requireContext(),email, password, confirmPassword, isSignUp = true)) {

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            val intent = Intent(requireContext(), MainActivity::class.java)
                             checkCreateCustomer(email)
                            startActivity(intent)
                            requireActivity().finish() // This removes AuthActivity from the back stack

                            //  updateUI(user)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed: user found .",
                                Toast.LENGTH_SHORT
                            ).show()
                            // updateUI(null)
                        }
                    }
            }
            /*  }else{
                Toast.makeText(
                    requireContext(),
                    "Password is incorrect .",
                    Toast.LENGTH_SHORT
                ).show()

            }

           */


        }

        buttonSignUp.setOnClickListener {

            createNewAccount()
        }


        buttonSignUPGoogle.setOnClickListener {
            lifecycleScope.launch {
                googleAuthHelper.startGoogleSignIn()


                val user = mAuth.currentUser
                val email = user?.email

                Log.d("Firebase", "User email: $email")

                if (email != null) {

                    checkCreateCustomer(email)
                    Log.d("Email: ", "emailcheck: $email")

                } else {
                    Log.d("Email", "Not check email : $email")

                }
            }



        }
    }






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}