package com.example.shopenest.registration.view

import android.content.ContentValues.TAG
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
import android.widget.Toast
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.shopenest.MainActivity
import com.example.shopenest.R
import com.example.shopenest.utilities.GoogleAuthHelper
import com.example.shopenest.utilities.ValidationUtils
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {

    lateinit var userName: EditText
    lateinit var userPassword: EditText
    lateinit var confirmPassword: EditText
    lateinit var mAuth: FirebaseAuth
    lateinit var buttonSignUp: Button
    lateinit var buttonSignIn: ImageButton

    private lateinit var credentialManager: CredentialManager
    private lateinit var googleAuthHelper: GoogleAuthHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        buttonSignIn = view.findViewById(R.id.googleButton)




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


        buttonSignIn.setOnClickListener {


            googleAuthHelper.startGoogleSignIn()


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