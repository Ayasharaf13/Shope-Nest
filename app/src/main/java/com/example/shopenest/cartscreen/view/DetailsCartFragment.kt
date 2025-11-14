package com.example.shopenest.cartscreen.view

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
import androidx.navigation.fragment.navArgs
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.view.DetailsProductFragmentArgs
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import kotlinx.coroutines.launch


class DetailsCartFragment : Fragment() {


    lateinit var detailsProductViewModel:HomeViewModel
    lateinit var detailsProductFactory: HomeViewModelFactory
    lateinit var textDisplaycounter:TextView
    lateinit var buttonIncrease:ImageButton
    lateinit var buttonDecrease:ImageButton
    lateinit var editText: EditText
    lateinit var textDisplayTotalPrice:TextView
    lateinit var textDisplayDiscount:TextView
    lateinit var textDisplayPriceAfterDiscount:TextView
    lateinit var buttonApply:Button
    var currentIdItem: Long? = null
     var countProduct:Int  = 0
    var price: Float = 0.0f


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
        return inflater.inflate(R.layout.fragment_details_cart, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         textDisplaycounter = view.findViewById(R.id.textDisplaycountDetailsCard)
         buttonDecrease = view.findViewById(R.id.decreaseButton)
         buttonIncrease = view.findViewById(R.id.increaseButton)
        editText = view.findViewById(R.id.editTextTextPromoCode)
        textDisplayDiscount = view.findViewById(R.id.textViewDisplayDiscount)
        textDisplayTotalPrice = view.findViewById(R.id.textViewDisplayTotalPrice)
        textDisplayPriceAfterDiscount = view.findViewById(R.id.textViewTotalPriceAfterDiscount)
        buttonApply = view.findViewById(R.id.buttonApply)




        val args: DetailsCartFragmentArgs by navArgs()
        currentIdItem = args.id
        price = args.price.toFloat()





      //  Log.i("codeee",promoCode)

        detailsProductFactory = HomeViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireActivity())
            )
        )



        detailsProductViewModel =
            ViewModelProvider(
                requireActivity(),
                detailsProductFactory
            ).get(HomeViewModel::class.java)




        fun check(availableProduct: Int) {
           countProduct = detailsProductViewModel.getInventory(currentIdItem)?.toInt() ?:0
            if (countProduct != null)
                if (availableProduct >= countProduct && currentIdItem != null) {

                    textDisplaycounter.text = detailsProductViewModel.getInventory(currentIdItem)
                        .toString()  //detailsProductViewModel.countter.toString()


                } else {
                    textDisplaycounter.text = availableProduct//detailsProductViewModel.getInventory(currentIdItem)
                        .toString()
                    Toast.makeText(requireContext(), "No Product found in store", Toast.LENGTH_SHORT)
                        .show()
                }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            detailsProductViewModel.inventory.collect { availableProduct ->
                Log.i("DetailFrag_inventory_item_Aval", "inventory_item_Aval = $availableProduct")

                buttonIncrease.setOnClickListener {
                    currentIdItem?.let { id ->
                        detailsProductViewModel.increaseInventory(id)
                        if (availableProduct != null) {
                            check(availableProduct)
                        }
                    }
                }


                buttonDecrease.setOnClickListener {
                    //  var coun =   detailsProductViewModel.countter--
                    currentIdItem?.let { id ->
                        detailsProductViewModel.decreaseInventory(id)
                        if (availableProduct != null) {
                            check(availableProduct)
                        }
                    }

                    // detailsProductViewModel.saveInventory(iditem,coun)


                }

                if (availableProduct != null) {
                    check(availableProduct)
                }
            }

        }

       buttonApply.setOnClickListener {
           var promoCode =   editText.text.toString()
         //  Toast.makeText(requireContext(),"applayDiscount",Toast.LENGTH_SHORT).show()
           Log.i("codeee",promoCode)
         //  if (promoCode == "Summer45" && price >= 30.00) {
           if (promoCode.trim().equals("Summer45", ignoreCase = true) && price >= 30.00) {

               var totlaPreic = countProduct * price

               textDisplayTotalPrice.text = totlaPreic.toString()

               textDisplayDiscount.text = "45%"

               textDisplayPriceAfterDiscount.text = (totlaPreic * (1 - 0.45)).toString()
               Toast.makeText(requireContext(),"applayDiscount2222222222",Toast.LENGTH_SHORT).show()

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
         * @return A new instance of fragment DetailsCartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailsCartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}