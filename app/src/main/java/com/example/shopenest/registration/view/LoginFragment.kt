package com.example.shopenest.registration.view

import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.example.shopenest.MainActivity
import com.example.shopenest.R
import com.example.shopenest.utilities.GoogleAuthHelper
import com.example.shopenest.utilities.ValidationUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {


    lateinit var userName: EditText
    lateinit var userPassword: EditText
    lateinit var forgotPasswordText:TextView
    lateinit var mAuth: FirebaseAuth
    lateinit var buttonLogIn: Button
    lateinit var buttonGoogle: ImageButton
    private lateinit var googleAuthHelper: GoogleAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()



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


        buttonGoogle.setOnClickListener {

            googleAuthHelper.startGoogleSignIn()
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