package com.example.shopenest.auth.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.shopenest.R


class WelcomeFragment : Fragment() {

    lateinit var buttonLogin: Button
    lateinit var buttonSignUp: Button

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
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonLogin = view.findViewById(R.id.buttonWelcomeLogin)
        buttonSignUp = view.findViewById(R.id.buttonWelcomeSignUp)

        buttonLogin.setOnClickListener {
            val action = WelcomeFragmentDirections
                .actionWelcomeFragmentToLoginFragment()
            findNavController().navigate(action)

        }

        buttonSignUp.setOnClickListener {

            val action = WelcomeFragmentDirections
                .actionWelcomeFragmentToSignupFragment()
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
         * @return A new instance of fragment WelcomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WelcomeFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}

