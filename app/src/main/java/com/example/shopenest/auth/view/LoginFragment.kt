package com.example.shopenest.auth.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopenest.MainActivity
import com.example.shopenest.R
import com.example.shopenest.auth.viewmodel.CreateUserViewModel
import com.example.shopenest.auth.viewmodel.CreateUserViewModelFactory
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.utilities.GoogleAuthHelper
import com.example.shopenest.utilities.ValidationUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {


    lateinit var userName: EditText
    lateinit var userPassword: EditText
    lateinit var forgotPasswordText:TextView
    lateinit var mAuth: FirebaseAuth

    lateinit var buttonLogIn: Button
    lateinit var   loginViewModelFactory:CreateUserViewModelFactory
    lateinit var loginViewModel:CreateUserViewModel
    lateinit var buttonGoogle: ImageButton
    lateinit var pref :SharedPreferences
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

        var firbaseapp = FirebaseApp.getApps(requireContext())
        if (firbaseapp.isEmpty()) {
            Log.e("Firebase", "Firebase is not initialized!");
        } else {
            Log.d("Firebase", "Firebase initialized successfully!");
        }

        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName = view.findViewById(R.id.editTextTextPersonNameLogin)
        userPassword = view.findViewById(R.id.editTextTextPasswordLogin)
        buttonLogIn = view.findViewById(R.id.buttonLogin)
        forgotPasswordText = view.findViewById(R.id.textForgotPassword)
        buttonGoogle = view.findViewById(R.id.buttonGoogleLogin)

        loginViewModelFactory = CreateUserViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            ))


        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(CreateUserViewModel::class.java)



          fun searchCustomerByEmail (email:String){

              loginViewModel.searchCustomerByEmail(email)

              lifecycleScope.launchWhenStarted {

                  loginViewModel.searchCustomer.collect{ response->

                      if (response != null) {
                          // ✅ User was created successfully
                          val customer = response.body()?.customers?.find{it.email == email}

                          Log.d("Search ", "Search By Email: ${customer?.id}")

                          Log.d("CreateCustomer", "Customer created: ${customer?.email}")

                          val customerId = customer?.id
                          val customerEmail = customer?.email

                          if (customerId != null && customerEmail != null) {
                              // Save only after successful creation
                              pref.edit()
                                  .putString("customer_id", customerId.toString())
                                  .putString("email", customerEmail)
                                  .apply()

                              Toast.makeText(requireContext(), "found and saved!", Toast.LENGTH_SHORT).show()
                          }

                          //   Log.i ("customer_id", response.body().customer.email.toString())
                          // Save to SharedPreferences or navigate
                      } else {
                          // ❌ Creation failed or still loading
                          Toast.makeText(requireContext(), " search failed", Toast.LENGTH_SHORT)
                              .show()
                      }
                      }
              }


          }

       //  loginViewModel.deleteCustomer(  9143269097762)

        lifecycleScope.launchWhenStarted {
            loginViewModel.deleteCustomer.collect {res->

                Log.d("SdeleyeEmailllllll ", "delete By Email: ${res.toString()}")




            }
        }

//email:samshop@333.yahoo.com
        searchCustomerByEmail("email:ayasharaf444@yahoo.com")
        lifecycleScope.launchWhenStarted {
            loginViewModel.searchCustomer.collect {res->

              //  Log.d("SearchByEmailllllll ", "Search By Email: ${res?.body()?.customers?.get(0)?.id}")

                if (res?.isSuccessful == true) {
                    val customers = res.body()?.customers
                    if (!customers.isNullOrEmpty()) {
                        Log.d("Search:NN", "Customer found: ${customers[0].email}")
                        Log.d("Search:NN", "Customer found: ${customers[0].id}")
                    } else {
                        Log.d("Search:NN", "No customer found with that email")
                    }
                } else {
                    Log.d("Search:NN", "Request failed: ${res?.code()}")
                }


            }
        }


        fun checkFondCustomer(email: String) {

            val editor = pref.edit()
            val savedEmail = pref.getString("email", null)


                searchCustomerByEmail(email)

             if (savedEmail != email ) {
                 Toast.makeText(requireContext(), "you can sign up ", Toast.LENGTH_SHORT)
                     .show()

            } else {
                Toast.makeText(requireContext(), "Log in is correct ", Toast.LENGTH_SHORT)
                    .show()

            }
        }


            buttonGoogle.setOnClickListener {
                lifecycleScope.launch {
                googleAuthHelper.startGoogleSignIn()
            }
        }
        forgotPasswordText.setOnClickListener {

            val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            findNavController().navigate(action)

        }

        fun loginAccount () {
            val email = userName.text.toString()
            val password = userPassword.text.toString()

            if (ValidationUtils.validateUserInput(requireContext(),email, password, isSignUp = false)) {

                 mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            checkFondCustomer(email)
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish() // This removes AuthActivity from the back stack

                            //  updateUI(user)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            // updateUI(null)
                        }
                    }
            }
        }

        buttonLogIn.setOnClickListener{
            loginAccount()
        }


    }






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}