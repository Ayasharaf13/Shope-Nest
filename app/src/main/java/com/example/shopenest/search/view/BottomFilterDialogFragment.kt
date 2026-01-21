package com.example.shopenest.search.view

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.auth.view.WelcomeFragmentDirections
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.view.GenericHomeAdapter
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.search.viewmodel.SearchViewModel
import com.example.shopenest.search.viewmodel.SearchViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BottomFilterDialogFragment : BottomSheetDialogFragment() {

    lateinit var priceOptionButton1: MaterialButton
    lateinit var priceOptionButton2: MaterialButton
    lateinit var priceOptionButton3: MaterialButton

    lateinit var buttonApply: MaterialButton
    lateinit var buttonReset: MaterialButton


    override fun onStart() {
        super.onStart()

        val dialog = dialog
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = getScreenHeight() //- dpToPx() // leave 50dp at top
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }


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
        return inflater.inflate(R.layout.fragment_bottom_filter_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        priceOptionButton1 = view.findViewById(R.id.price1)
        priceOptionButton2 = view.findViewById(R.id.price2)
        priceOptionButton3 = view.findViewById(R.id.price3)
        buttonReset = view.findViewById(R.id.resetButton)
        buttonApply = view.findViewById(R.id.applyButton)


        fun setupPriceToggle(button: MaterialButton) {
            button.setOnClickListener {
                if (button.isChecked) {
                    button.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.purple_500
                        )
                    )
                    val selectedPriceRange = button.text.toString()
                    Log.d("Toggle", "Selected price range: $selectedPriceRange")
                    button.setTextColor(Color.BLACK)
                } else {
                    button.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
        }

        setupPriceToggle(priceOptionButton1)
        setupPriceToggle(priceOptionButton2)
        setupPriceToggle(priceOptionButton3)


        buttonApply.setOnClickListener {

            if (priceOptionButton1.isChecked) {
                val action =
                    BottomFilterDialogFragmentDirections.actionBottomFilterDialogFragmentToSeeAllFragment(
                        rangePrice1 = 0.00f,
                        rangePrice2 = 100.00f,
                        isApply = true,
                        isItFromTheBrand = false
                    )
                findNavController().navigate(action)



                dismiss()


            } else if (priceOptionButton2.isChecked) {

                val action =
                    BottomFilterDialogFragmentDirections.actionBottomFilterDialogFragmentToSeeAllFragment(
                        rangePrice1 = 100.00f,
                        rangePrice2 = 200.00f,
                        isApply = true,
                        isItFromTheBrand = false
                    )
                findNavController().navigate(action)
                dismiss()

            } else if (priceOptionButton3.isChecked) {

                val action =
                    BottomFilterDialogFragmentDirections.actionBottomFilterDialogFragmentToSeeAllFragment(
                        rangePrice1 = 200.00f,
                        rangePrice2 = 300.00f,
                        isApply = true,
                        isItFromTheBrand = false
                    )
                findNavController().navigate(action)

                dismiss()

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
         * @return A new instance of fragment BottomFilterDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BottomFilterDialogFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}