package com.example.shopenest.homescreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.shopenest.R
import com.example.shopenest.auth.view.WelcomeFragmentDirections
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.GenericAdapterSliderImage

import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.*
import com.example.shopenest.network.ShoppingClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*


class DetailsProductFragment : Fragment() {


    lateinit var pref: SharedPreferences
  lateinit var detailsProductViewModel:HomeViewModel
    lateinit var detailsProductFactory: HomeViewModelFactory
    lateinit var textDetails:TextView
    lateinit var textTitle:TextView
    lateinit var textPrice:TextView
    var currentIdItem: Long? = null
  var price :String? = null
    val discount = 45
    lateinit var  slidProducts:ViewPager2
    lateinit var sliderImageAdapter: GenericAdapterSliderImage<ImageProduct>
    lateinit var decreaseButton: Button
    lateinit var increaseButton: Button
    lateinit var counterText:TextView
    lateinit var buttonAddToCart:Button
    lateinit var productImages :Array<Image>

    var idVariants:Long? =0L

var cou:Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
        pref = requireContext().
        getSharedPreferences("MyPref", Context.MODE_PRIVATE) // 0 - for private mode


       // val CustomerId = pref.getString("customer_id", null)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_product, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailsProductFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textDetails = view.findViewById(R.id.textDetails)
        textTitle = view.findViewById(R.id.textNameProduct)
        slidProducts = view.findViewById(R.id.productSlider)
        textPrice = view.findViewById(R.id.textPrice)
        decreaseButton = view.findViewById(R.id.buttonMinus)
        increaseButton = view.findViewById(R.id.buttonPlus)
        counterText = view.findViewById(R.id.textCounter)
        buttonAddToCart = view.findViewById(R.id.btnAddToCard)
        //  GenericAdapterSliderImage()






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

        val args: DetailsProductFragmentArgs by navArgs()
        val idProduct = args.idProductDetails


        detailsProductViewModel.getProductDetails(idProduct)



        Log.i("DetailsFragmenhhhhht", "bodyHtml = ${idProduct}")

        //lifecycleScope.launch(Dispatchers.Main) {
        /*   viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main) {
            detailsProductViewModel.productDetails.collect { responce ->
                Log.i("DetailsFragment", "bodTiltle = ${responce.product.body_html}")
                textDetails.text =   responce.product.body_html
                textTitle.text = responce.product.title
              val imagess =  responce.product.images
             textPrice.text =   responce.product.variants.firstOrNull()?.price
              var iditem=  responce.product.variants.firstOrNull()?.inventory_item_id

      */


        viewLifecycleOwner.lifecycleScope.launch {
            detailsProductViewModel.productDetails.collect { response ->
                Log.i("DetailsFragment", "bodTiltle = ${response.product.body_html}")
                textDetails.text = response.product.body_html
                textTitle.text = response.product.title
                val imagess = response.product.images
                val format = NumberFormat.getCurrencyInstance(Locale("en", "EG"))
                var price = response.product.variants.firstOrNull()?.price

                val priceDouble = price?.toDoubleOrNull() ?: 0.0
                textPrice.text =   format.format(priceDouble)


                currentIdItem = response.product.variants.firstOrNull()?.inventory_item_id


                 price =  response.product.variants.firstOrNull()?.price
                 idVariants  =  response.product.variants.firstOrNull()?.id



                sliderImageAdapter = GenericAdapterSliderImage<ImageProduct>(
                    "",
                    imagess,
                    requireContext(),
                    bind = { product, view, position ->

                       var imageView = view.findViewById<ImageView>(R.id.imageView)
                        // imageView.setImageResource(R.drawable.ic_baseline_favorite_24)
                        Glide.with(imageView.context)
                            .load(imagess[position].src)
                            .placeholder(R.drawable.ic_launcher_background) // optional
                            .error(R.drawable.custombottomnav)       // optional
                            .into(imageView)


                    })
                //  viewPagerAds.adapter = adapter
                slidProducts.adapter = sliderImageAdapter
                currentIdItem?.let { id ->
                    detailsProductViewModel.getAvailableProducts(id)

                }
            }
        }



        fun check(availableProduct: Int) {
            cou =    detailsProductViewModel.getInventory(currentIdItem)

            cou?.let { currentCount->
                if (availableProduct >= currentCount && currentIdItem != null) {

                    counterText.text = detailsProductViewModel.getInventory(currentIdItem)
                        .toString()  //detailsProductViewModel.countter.toString()

                    Log.i ("countterrr}}}check : ",cou.toString())



         } else {
             counterText.text =
                 availableProduct//detailsProductViewModel.getInventory(currentIdItem)
                     .toString()
             Toast.makeText(
                 requireContext(),
                 "No Product found in store",
                 Toast.LENGTH_SHORT
             )
                 .show()
         }
     }

 }


        fun createDraftOrder() {

            var count = cou ?: 0
            var price = price?.toDouble() ?:0.0
            var idVarient = idVariants?.toLong() ?:0L

            var itemOrder = listOf<LineItem>(LineItem(quantity = cou,variant_id = idVarient ))

            var amount = (count * price * discount) / 100

            var applayDiscount =
                AppliedDiscount("SummerDiscount", "45", "summerCode", "$amount", "percentage")

            var draftOrderRequest = DraftOrderRequest(
                draft_order = DraftOrderRequestBody(
                    line_items = itemOrder,
                    applayDiscount,
                    pref.getString("customer_id", null)?.toLong()?.let { CustomerRef(it) }
                )
            )

            detailsProductViewModel.createDraftOrder(draftOrderRequest)

        }



 buttonAddToCart.setOnClickListener {

    createDraftOrder()
val action = DetailsProductFragmentDirections
  .actionDetailsProductFragmentToCartFragment(cou?:0)
findNavController().navigate(action)

}



viewLifecycleOwner.lifecycleScope.launch {
detailsProductViewModel.inventory.collect { availableProduct ->
  Log.i("DetailFrag_inventory_item_Aval", "inventory_item_Aval = $availableProduct")


  increaseButton.setOnClickListener {
      currentIdItem?.let { id ->
          detailsProductViewModel.increaseInventory(id)
          if (availableProduct != null) {
              check(availableProduct)
          }
      }
  }


  decreaseButton.setOnClickListener {
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


Log.i("DetailFrag_inventory_item", "inventory_item = ${currentIdItem}")


//      sliderProductAdapter = ImageAdapter( ,requireContext())
// }


//  }

}
}






// if (   currentIdItem!= null) {

//   detailsProductViewModel.getAvailableProducts(iditem)
//  Log.i("idItemidddd_1::", "inventory_item_idItemiddd_1:: = ${iditem}")

/*  detailsProductViewModel.inventory.collect{ availableProduct->
        Log.i("DetailFrag_inventory_item_Aval", "inventory_item_Aval = ${availableProduct}")

           increaseButton.setOnClickListener {
          //    var coun = detailsProductViewModel.countter++
          detailsProductViewModel.increaseInventory(   currentIdItem)
            //   detailsProductViewModel.saveInventory(iditem,coun)

               if (availableProduct != null) {
                   check(availableProduct)

               }
           }

           decreaseButton.setOnClickListener {
          //  var coun =   detailsProductViewModel.countter--
               detailsProductViewModel.decreaseInventory(   currentIdItem)
              // detailsProductViewModel.saveInventory(iditem,coun)

               if (availableProduct != null) {
                   check(availableProduct)
               }
           }

           if (availableProduct != null) {
              // detailsProductViewModel.countter = 0
               check(availableProduct)
               Log.i("idItemidddd_2::", "inventory_item_idItemiddd_2:: = ${   currentIdItem}")

           }
       }*/


//  }
