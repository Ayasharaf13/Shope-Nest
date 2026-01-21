package com.example.shopenest.cartscreen.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.address.viewmodel.AddressViewModel
import com.example.shopenest.address.viewmodel.AddressViewModelFactory

import com.example.shopenest.cartscreen.viewmodel.CartViewModel
import com.example.shopenest.cartscreen.viewmodel.CartViewModelFactory
import com.example.shopenest.db.ConcreteLocalSource

import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.*
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.utilities.CustomerPref
import kotlinx.coroutines.*


class CartFragment : Fragment() {


    lateinit var detailsProductViewModel: HomeViewModel
    lateinit var detailsProductFactory: HomeViewModelFactory
    lateinit var cartViewModel: CartViewModel
    lateinit var cartFactory: CartViewModelFactory
    var totalPriceAfterDiscount: String? = null
    lateinit var recyclerCart: RecyclerView
    lateinit var imageEmptyCart: ImageView
    lateinit var buttonCheckout: Button


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
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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


        cartFactory = CartViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireActivity())
            )
        )


        cartViewModel =
            ViewModelProvider(
                requireActivity(),
                cartFactory
            ).get(CartViewModel::class.java)


        //  var customerId = CustomerPref(requireContext() ).getCustomerId()


        // To load data
        val customerId = CustomerPref(requireContext()).getCustomerId()?.toLong()

        customerId?.let { cartViewModel.loadDraftOrder(it) }

        imageEmptyCart = view.findViewById(R.id.emptyCard)
        buttonCheckout = view.findViewById(R.id.btnCheckout)
        recyclerCart = view.findViewById(R.id.recyclerCart)
        // cartViewModel.getDraftOrder()


        var addressFactory = AddressViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )


        var addressViewModel =
            ViewModelProvider(this, addressFactory).get(AddressViewModel::class.java)



        buttonCheckout.setOnClickListener {

            val action = CartFragmentDirections
                .actionCartFragmentToCheckoutFragment()
            findNavController().navigate(action)


        }


        //DetailsProduct
        // Try to read Safe Args (only works if launched with Safe Args)
        val safeArgs = try {
            CartFragmentArgs.fromBundle(requireArguments())
        } catch (e: Exception) {
            null
        }
        //  var  = CustomerPref(requireContext()).getCustomerId()?.toLong()

        val idDraftOrder = safeArgs?.draftId

        val productQuantity = safeArgs?.productQuantity
        val fromScreen = arguments?.getString("fromscreen")

        //if (productQuantity !=-1){


        Log.i("testQCartDraft: ", idDraftOrder.toString())
        Log.i("testQCartCustomer: ", customerId.toString())

        // var cardAdapter: CartAdapter? = productQuantity?.let { CartAdapter(it, "", requireView()) }
        // 1️⃣ Set adapter once in onViewCreated

        var cardAdapter = CartAdapter(requireContext(), requireView()) { draftOrderId ->
            customerId?.let { cartViewModel.deleteDraftOrder(draftOrderId, it) }
        }


        // \\  recyclerBrand.layoutManager = LinearLayoutManager(requireContext(),HorizontalScrollView)
        recyclerCart.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        recyclerCart.adapter = cardAdapter




        suspend fun loadDraftOrderItems(
            lineItems: List<LineItem> //Response<ResponseDraftOrderForRequestCreate>
        ): List<DraftOrderItemUI> = coroutineScope {


            val deferredList = lineItems.map { item ->
                async(Dispatchers.IO) {

                    val productId = item.product_id ?: return@async null

                    val productResponse = detailsProductViewModel.getProductDetails(productId)
                    val product = productResponse?.product ?: return@async null

                    DraftOrderItemUI(
                        title = product.title ?: "",
                        image = product.image?.src ?: "",
                        quantity = item.quantity,
                        lineItemId = item.idLineItem,
                        draftOrderId = item.draftOrderId,
                        //draftId
                        productId = item.product_id,
                        price = item.price?.toDouble() ?: 0.0
                    )


                }
            }

            deferredList.awaitAll().filterNotNull()
        }





        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                cartViewModel.draftOrderWithItems.collect { (header, items) ->

                    if (header == null || items.isEmpty()) {
                        Log.i("CartFragment", "Waiting for DB data...")
                        return@collect

                    }

                    // 1️⃣ Heavy work → IO
                    val uiList = withContext(Dispatchers.IO) {
                        loadDraftOrderItems(items)

                    }

                    // 2️⃣ UI update → Main
                    totalPriceAfterDiscount = header.subtotal_price
                    // cardAdapter?.updateTotalPrice(totalPriceAfterDiscount)
                    Log.i("CartFragment_Hed", "$header")
                    Log.i("CartFragment_Item", "$items")

                    // VERY IMPORTANT → new list instance
                    cardAdapter?.submitList(uiList.toList())

                }

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
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}
