package com.example.shopenest.auth.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.shopenest.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : Fragment() {

    lateinit var editEmail:EditText
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var buttonResetPassword:Button
    lateinit var buttonBackToLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()


        var firbaseapp = FirebaseApp.getApps(requireContext())
        if (firbaseapp.isEmpty()) {
            Log.e("Firebase", "Firebase is not initialized!");
        } else {
            Log.d("Firebase", "Firebase initialized successfully!");
        }
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editEmail = view.findViewById(R.id.editTextTextEmailForgotPassword)
        buttonResetPassword = view.findViewById(R.id.buttonResetPassword)
        buttonBackToLogin = view.findViewById(R.id.buttonBackToSignIn)

        buttonBackToLogin.setOnClickListener {

            val action =
                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()

            findNavController().navigate(action)

        }

        buttonResetPassword.setOnClickListener {
            var email = editEmail.text.toString().trim()
            fun String.isValidEmail(): Boolean {
                return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }

            if (!email.isValidEmail()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter a valid email address",
                    Toast.LENGTH_SHORT
                ).show()
            } else {


                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email sent.")
                        }
                    }


            }
            Snackbar.make(
                view, // any view in your layout (e.g. rootLayout)
                "Please check your inbox and follow the instructions to reset your password.\nIf you don’t see the email, check spam or junk folder.",
                Snackbar.LENGTH_LONG
            ).show()

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                ForgotPasswordFragment().apply {
                    arguments = Bundle().apply {


                    }
                }
    }
}

