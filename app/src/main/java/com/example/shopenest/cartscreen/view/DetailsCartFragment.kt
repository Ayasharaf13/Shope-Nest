package com.example.shopenest.cartscreen.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.shopenest.R
import com.example.shopenest.cartscreen.viewmodel.CartViewModel
import com.example.shopenest.cartscreen.viewmodel.CartViewModelFactory
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.*
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.utilities.CustomerPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailsCartFragment : Fragment() {


    lateinit var detailsProductViewModel: HomeViewModel
    lateinit var detailsProductFactory: HomeViewModelFactory
    lateinit var cartViewModel: CartViewModel
    lateinit var cartViewModelFactory: CartViewModelFactory
    lateinit var textDisplaycounter: TextView
    lateinit var buttonIncrease: ImageButton
    lateinit var buttonDecrease: ImageButton
    lateinit var editText: EditText
    lateinit var textDisplayTotalPrice: TextView
    lateinit var textDisplayDiscount: TextView
    lateinit var textDisplayPriceAfterDiscount: TextView
    lateinit var buttonApply: Button
    lateinit var imgeProduct: ImageView
    lateinit var nameOfProduct: TextView
    var currentIdItem: Long? = null
    lateinit var buttonChecout: Button
    private var availableProduct: Int? = null

    var price: Float = 0.0f
    var quantity = ""
    lateinit var pricetext: TextView
    var lineItemId: Long = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        detailsProductFactory = HomeViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )


        detailsProductViewModel =
            ViewModelProvider(
                requireActivity(),
                detailsProductFactory
            ).get(HomeViewModel::class.java)
        detailsProductViewModel.getInventory(currentIdItem ?: 0).toString()
        //  countProduct = detailsProductViewModel.getInventory(currentIdItem ?:0)


        cartViewModelFactory = CartViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )

        cartViewModel = ViewModelProvider(
            requireActivity(),
            cartViewModelFactory
        ).get(CartViewModel::class.java)

        // load LineItems again because user will change quantity
        // currentIdItem?.let { cartViewModel.getAllLineItems (it) }
        currentIdItem?.let { cartViewModel.setCustomerId(it) }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_cart, container, false)
    }


    @SuppressLint("SuspiciousIndentation")
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
        pricetext = view.findViewById(R.id.textPrice)
        imgeProduct = view.findViewById(R.id.imageProductCartDetails)
        nameOfProduct = view.findViewById(R.id.textViewTitleCardDetails)
        buttonChecout = view.findViewById(R.id.buttonCheckout)


        val customerId = CustomerPref(requireContext()).getCustomerId()?.toLong()

        val args: DetailsCartFragmentArgs by navArgs()

        currentIdItem = args.id
        lineItemId = args.idLineItem
        price = args.price.toFloat()
        quantity = args.quantity
        //  var pricebeforeDiscount = price*quantity
        //  pricetext.text = pricebeforeDiscount
        val priceBeforeDiscount = price * quantity.toFloat()
        pricetext.text = "$%.2f".format(priceBeforeDiscount)



        lifecycleScope.launch(Dispatchers.Main) {

            //   detailsProductViewModel.initInventoryForProduct(currentIdItem?:-101)
            Log.i("currentProductItem_id", currentIdItem.toString())


            detailsProductViewModel.getProductDetails(currentIdItem ?: 0)

            Log.i(
                "countttter",
                detailsProductViewModel.getInventory(currentIdItem ?: 0)
                    .toString()/*detailsProductViewModel.getInventory(currentIdItem ?:0).toString()*/
            )

            detailsProductViewModel.productDetails.collect { productResponse ->

                val productList = mutableListOf<Product>(productResponse.product)// wrap in list
                // productList.add(productResponse.product)
                Glide.with(requireContext())
                    .load(productResponse.product.image.src)

                    .placeholder(R.drawable.ic_launcher_background) // Optional placeholder
                    .into(imgeProduct)

                nameOfProduct.text = productResponse.product.title
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.lineItemsOnly.collect { items ->

                Log.i("FLOW", "Items emitted size=${items.size}")

                val item = items.find { it.idLineItem == lineItemId }
                Log.i("FLOW", "Items emitted size=${items}")
                Log.i("FLOW", "lineItem args: =${lineItemId}")
                Log.i("FLOW", "Items emitted lineItem: =${items.firstOrNull()?.idLineItem}")

                if (item != null) {
                    textDisplaycounter.text = item.quantity.toString()
                }
            }
        }



        fun check(availableProduct: Int) {

            Log.i("countProduct_1", quantity.toString())

            if (quantity != null)
                if (availableProduct >= quantity.toInt() && currentIdItem != null) {

                    Log.i("getcounttextt_2", currentIdItem.toString())


                } else {
                    textDisplaycounter.text =
                        availableProduct//detailsProductViewModel.getInventory(currentIdItem)
                            .toString()
                    Log.i("countProduct_3", quantity.toString())
                    Toast.makeText(
                        requireContext(),
                        "No Product found in store",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

        }


        buttonDecrease.setOnClickListener {
            //  var coun =   detailsProductViewModel.countter--
            currentIdItem?.let { id ->

                //  detailsProductViewModel.decreaseInventory(id)
                customerId?.let { it1 -> detailsProductViewModel.decreaseItem(lineItemId, it1) }
                Log.i("Id_LineItemcreaee:  ", lineItemId.toString())


                availableProduct?.let {
                    check(it)
                }

            }

            // detailsProductViewModel.saveInventory(iditem,coun)


        }

        buttonChecout.setOnClickListener {

            val action = DetailsCartFragmentDirections.actionDetailsCartFragmentToCheckoutFragment()

            findNavController().navigate(action)
        }


        val toolbar =
            view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.myCustomToolbarDetailsCart)


// 2. برمجة السهم ليعود للخلف عند الضغط عليه
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        buttonIncrease.setOnClickListener {
            Log.i("increase_inventory_item_Aval", "inventory_item_Aval = $availableProduct")


            currentIdItem?.let { id ->
                // textDisplaycounter.text = detailsProductViewModel.getInventory(id).toString()
                Log.i("increase_iD", "inventory_item_Aval = $id")
                //  detailsProductViewModel.increaseInventory(id)

                customerId?.let { it1 -> detailsProductViewModel.increaseItem(lineItemId, it1) }

                availableProduct?.let {
                    check(it)
                }


            }


        }


        viewLifecycleOwner.lifecycleScope.launch {
            detailsProductViewModel.inventory.collect { value ->
                Log.i("DetailFrag_inventory_item_Aval", "inventory_item_Aval = $availableProduct")

                availableProduct = value

                if (value != null) {
                    check(value)
                }
            }

        }


        var test = currentIdItem?.let { detailsProductViewModel.getInventory(it) }





        buttonApply.setOnClickListener {
            var promoCode = editText.text.toString()
            var discount = 45
            //  Toast.makeText(requireContext(),"applayDiscount",Toast.LENGTH_SHORT).show()
            Log.i("codeee", promoCode)
            //  if (promoCode == "Summer45" && price >= 30.00) {
            if (promoCode.trim().equals("Summer45", ignoreCase = true) && price >= 30.00) {

                var amount = (quantity.toInt() * price * discount) / 100
                var appliedDiscount =
                    AppliedDiscount("SummerDiscount", "45", "summerCode", "$amount", "percentage")


                //  cartViewModel.setDiscount(appliedDiscount)
                var request = DraftOrderUpdateRequest(
                    DraftOrderUpdateBody(
                        currentIdItem ?: 0,
                        appliedDiscount
                    )
                )

                detailsProductViewModel.updateDiscount(currentIdItem ?: 0, request)
                var totlaPreic = quantity.toInt() * price

                textDisplayTotalPrice.text = totlaPreic.toString()

                textDisplayDiscount.text = "45%"

                textDisplayPriceAfterDiscount.text = (totlaPreic * (1 - 0.45)).toString()
                Toast.makeText(requireContext(), "applayDiscount2222222222", Toast.LENGTH_SHORT)
                    .show()

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