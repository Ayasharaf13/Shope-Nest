package com.example.shopenest.profile.view

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.shopenest.AuthActivity
import com.example.shopenest.MainActivity
import com.example.shopenest.R
import com.example.shopenest.address.viewmodel.AddressViewModel
import com.example.shopenest.address.viewmodel.AddressViewModelFactory
import com.example.shopenest.auth.view.WelcomeFragmentDirections
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.model.Product
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.utilities.GoogleAuthHelper
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    lateinit var addressProfile: LinearLayout
    lateinit var settingProfile: LinearLayout
    lateinit var favProfile: LinearLayout
    lateinit var orderProfile: LinearLayout
    lateinit var helpProfile: LinearLayout
    lateinit var privacy: LinearLayout
    lateinit var btnLogout: Button
    lateinit var auth: FirebaseAuth
    private lateinit var pref: SharedPreferences

    private lateinit var googleAuthHelper: GoogleAuthHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressProfile = view.findViewById(R.id.addressprofile)
        settingProfile = view.findViewById(R.id.settingProfile)
        favProfile = view.findViewById(R.id.myFav)
        orderProfile = view.findViewById(R.id.myOrder)
        helpProfile = view.findViewById(R.id.helpSupport)
        privacy = view.findViewById(R.id.privacyPolicyLayout)
        btnLogout = view.findViewById(R.id.btnLogout)

        auth = FirebaseAuth.getInstance()


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




        btnLogout.setOnClickListener {
            // don not forget write nav to log in
            val user = auth.currentUser

            // Check the provider used
            val isGoogleSignIn =
                user?.providerData?.any { it.providerId == "google.com" } == true

            // Sign out from Firebase
            auth.signOut()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)

            //   if (isGoogleSignIn) {
            googleAuthHelper.googleSignInClient.signOut().addOnCompleteListener {
                Log.d("Logout", "Signed out from Google")
                //  }


            } //else {
            // Log.d("Logout", "Signed out from Firebase (email)")

            // }


            val editor = pref.edit()
            editor.clear()
            editor.apply()


        }

        privacy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://policies.google.com/privacy")
            startActivity(intent)
        }


        favProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToFavFragment(Product(), "")
            findNavController().navigate(action)

        }

        helpProfile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToHelpFragment()
            findNavController().navigate(action)

        }

        orderProfile.setOnClickListener {

            val action = ProfileFragmentDirections.actionProfileFragmentToCartFragment(0L, "", 0)
            findNavController().navigate(action)
        }

        settingProfile.setOnClickListener {

            val action = ProfileFragmentDirections.actionProfileFragmentToSettingFragment()
            findNavController().navigate(action)

        }
        addressProfile.setOnClickListener {

            val action = ProfileFragmentDirections
                .actionProfileFragmentToDisplaySavedAddressFragment("profile")
            findNavController().navigate(action)


        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}