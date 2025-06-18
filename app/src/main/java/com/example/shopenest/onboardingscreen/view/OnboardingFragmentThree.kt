package com.example.shopenest.onboardingscreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.shopenest.AuthActivity
import com.example.shopenest.R
import com.example.shopenest.onboardingscreen.ViewPagerAdapterAuth
import com.example.shopenest.registration.view.WelcomeFragment


class OnboardingFragmentThree : Fragment() {

lateinit var buttonSkip: Button
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
        return inflater.inflate(R.layout.fragment_onboarding_three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSkip = view.findViewById(R.id.buttonPage3)
        buttonSkip.setOnClickListener{
            val fragmentB = WelcomeFragment()
            val onboardingFragmentThree = OnboardingFragmentThree()



            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentB)
                .addToBackStack(null) // Optional: adds to back stack
                .commit()
           // (activity as? AuthActivity)?.goToNextPage(2)
        }
      /*  buttonSkip.setOnClickListener{
            val action = OnboardingFragmentThreeDirections
                .actionOnboardingFragmentThreeToWelcomeFragment()
            findNavController().navigate(action)
        }

       */
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OnboardingFragmentThree.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OnboardingFragmentThree().apply {
                arguments = Bundle().apply {

                }
            }
    }
}