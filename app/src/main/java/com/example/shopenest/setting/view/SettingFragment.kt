package com.example.shopenest.setting.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.CurrencyRepository
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.setting.viewmodel.SettingViewModel
import com.example.shopenest.setting.viewmodel.SettingViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.intellij.lang.annotations.Language

class SettingFragment : Fragment() {

    private lateinit var rowCurrency: LinearLayout
    private lateinit var tvCurrencyValue: TextView
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var settingViewModelFactory: SettingViewModelFactory

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
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        settingViewModelFactory = SettingViewModelFactory(
            CurrencyRepository.getInstance(

                requireContext()
            )
        )


        settingViewModel =
            ViewModelProvider(this, settingViewModelFactory).get(SettingViewModel::class.java)




        rowCurrency = view.findViewById(R.id.rowCurrency)
        //  rowLanguage = view.findViewById(R.id.rowLanguage)
        tvCurrencyValue = view.findViewById(R.id.tvCurrencyValue)
        //   tvLanguageValue = view.findViewById(R.id.tvLanguageValue)

        fun showCurrencyDialog() {

            val items = arrayOf("USD", "EGP")
            var selectedIndex = items.indexOf(tvCurrencyValue.text.toString())

            var builder = AlertDialog.Builder(requireContext())
                .setTitle("Choose Currency")
                .setSingleChoiceItems(items, selectedIndex) { dialog, index ->
                    selectedIndex = index
                }
                .setPositiveButton("Apply") { dialog, _ ->
                    tvCurrencyValue.text = items[selectedIndex]
                    settingViewModel.updateCurrency(items[selectedIndex])

                    dialog.dismiss()

                }

                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

            val dialog = builder.create()

            // Show the Alert Dialog box
            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)

        }


        rowCurrency.setOnClickListener {

            showCurrencyDialog()


        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}