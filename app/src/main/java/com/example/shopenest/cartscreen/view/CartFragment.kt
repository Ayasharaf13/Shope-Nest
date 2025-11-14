package com.example.shopenest.cartscreen.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.address.viewmodel.AddressViewModel
import com.example.shopenest.address.viewmodel.AddressViewModelFactory
import com.example.shopenest.auth.view.WelcomeFragmentDirections
import com.example.shopenest.cartscreen.viewmodel.CartViewModel
import com.example.shopenest.cartscreen.viewmodel.CartViewModelFactory
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.view.BrandAdapter
import com.example.shopenest.homescreen.view.DetailsProductFragmentArgs
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.*
import com.example.shopenest.network.ShoppingClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch


class CartFragment : Fragment() {

// to get info product details that user choose

    lateinit var detailsProductViewModel: HomeViewModel
    lateinit var detailsProductFactory: HomeViewModelFactory
    lateinit var cartViewModel:CartViewModel
    lateinit var cartFactory: CartViewModelFactory
    var totalPriceAfterDiscount :String? = null
    lateinit var cardAdapter:CartAdapter
    lateinit var recyclerCart:RecyclerView
    lateinit var imageEmptyCart:ImageView
    lateinit var buttonCheckout: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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


        imageEmptyCart = view.findViewById(R.id.emptyCard)
        buttonCheckout = view.findViewById(R.id.btnCheckout)
        cartViewModel.getDraftOrder()



      var  addressFactory = AddressViewModelFactory(
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

        recyclerCart = view.findViewById(R.id.recyclerCart)


        val args: CartFragmentArgs by navArgs()
        val productQuantity = args.productQuantity

 //if (productQuantity !=-1){

    Log.i("testQCart","frombuttonAddToCart")

            var adapter = CartAdapter(productQuantity, null)

            //   recyclerBrand.layoutManager = LinearLayoutManager(requireContext(),HorizontalScrollView)
            recyclerCart.setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            )


// 1214597595426 ordreId
            lifecycleScope.launch(Dispatchers.Main) {
                detailsProductViewModel.draftOrder.collect { draftOrderResponse ->

                    if (draftOrderResponse != null) {
                        if (draftOrderResponse.isSuccessful) {
                            val draftOrder = draftOrderResponse.body()
                            //  if (draftOrder != null && draftOrder.draft_order != "") {
                            //  val totalPrice_id = draftOrder.draft_order.get(7)?.id//?.total_price
                            // val totalPrice = draftOrder?.draft_order?.total_price
                            val totalPrice = draftOrderResponse.body()?.draft_order?.total_price
                            val orderId = draftOrderResponse.body()?.draft_order?.order_id
                            //  val latestDraftOrder = draftOrder.draft_orders.maxByOrNull { it.created_at.toString() } // newest by ID
                            //  val totalPrice = latestDraftOrder?.total_price
                            Log.i("draftOrderID", "✅ Retrieved total price: $orderId")

                            totalPriceAfterDiscount = totalPrice
                            adapter.updateTotalPrice(totalPriceAfterDiscount)
                            //} else {
                            //  Log.w("draftOrdercart", "⚠️ No draft orders found in response")
                            //  }
                        } else {
                            Log.e(
                                "draftOrdercart",
                                "❌ API failed: code=${draftOrderResponse.code()} message=${draftOrderResponse.message()}"
                            )
                        }
                    } else {
                        Log.w("draftOrdercart", "⚠️ No response yet (flow is still null)")
                    }
                }
            }


            lifecycleScope.launch(Dispatchers.Main) {

                detailsProductViewModel.productDetails.collect { productResponse ->

                    val productList = mutableListOf<Product>(productResponse.product)// wrap in list
                    // productList.add(productResponse.product)
                    recyclerCart.adapter = adapter
                    adapter.submitList(productList)

                }
            }
       // }else{

    //  Log.i("testQCart","fromNavBottom")

// }


 }

           // Log.i("testQCart","fromNavBottom")
          //  recyclerCart.visibility = View.GONE

         //   imageEmptyCart.visibility = View.VISIBLE



  //  }


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
        @JvmStatic fun newInstance(param1: String, param2: String) =
                CartFragment().apply {
                    arguments = Bundle().apply {



                    }
                }
    }
}