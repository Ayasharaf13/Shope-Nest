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
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.favouritescreen.view.FavFragmentArgs
import com.example.shopenest.homescreen.GenericAdapterSliderImage

import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.*
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.setting.viewmodel.SettingViewModel
import com.example.shopenest.setting.viewmodel.SettingViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.NumberFormat
import java.util.*


class DetailsProductFragment : Fragment() {


    lateinit var pref: SharedPreferences
    lateinit var detailsProductViewModel: HomeViewModel

    var idDraft: Long = 0L

    lateinit var detailsProductFactory: HomeViewModelFactory
    lateinit var textDetails: TextView
    lateinit var textTitle: TextView
    lateinit var textPrice: TextView
    var currentIdItem: Long? = null
    var price: String? = null
    val discount = 45
    lateinit var slidProducts: ViewPager2
    lateinit var sliderImageAdapter: GenericAdapterSliderImage<ImageProduct>
    lateinit var decreaseButton: Button
    lateinit var increaseButton: Button
    lateinit var counterText: TextView
    lateinit var buttonAddToCart: Button
    lateinit var productImages: Array<Image>

    var idVariants: Long? = 0L
    var idProduct: Long? = 0L
    var draftId: Long? = 0L

    var cou: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
        pref = requireContext().getSharedPreferences(
            "MyPref",
            Context.MODE_PRIVATE
        ) // 0 - for private mode


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


    @SuppressLint("LongLogTag")
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
        val toolbar =
            view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.myCustomToolbarProductDetails)


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


        var settingViewModelFactory = SettingViewModelFactory(
            CurrencyRepository.getInstance(

                requireContext()
            )
        )


        var settingViewModel =
            ViewModelProvider(this, settingViewModelFactory).get(SettingViewModel::class.java)


        val args: DetailsProductFragmentArgs by navArgs()
        var idProduct = args.idProductDetails






        viewLifecycleOwner.lifecycleScope.launch {
            detailsProductViewModel.getProductDetails(idProduct)
            // Combine product details and currency flows
            combine(
                detailsProductViewModel.productDetails,
                settingViewModel.currencyFlow
            ) { response, currency ->
                // Prepare price
                val price = response.product.variants.firstOrNull()?.price?.toDoubleOrNull() ?: 0.0

                // Format price according to selected currency
                val formatted = when (currency) {
                    "USD" -> NumberFormat.getCurrencyInstance(Locale.US).format(price)
                    "EGP" -> NumberFormat.getCurrencyInstance(Locale("en", "EG")).format(price)
                    else -> price.toString()
                }

                // Return a Pair of formatted price and response (or whatever you need)
                Pair(response, formatted)
            }.collect { (response, formattedPrice) ->
                // Update UI here
                textTitle.text = response.product.title
                textDetails.text = response.product.body_html
                textPrice.text = formattedPrice

                val imagess = response.product.images
                //  val format = NumberFormat.getCurrencyInstance(Locale("en", "EG"))

                //  var price = response.product.variants.firstOrNull()?.price

                currentIdItem = response.product.variants.firstOrNull()?.inventory_item_id


                price = response.product.variants.firstOrNull()?.price
                idVariants = response.product.variants.firstOrNull()?.id



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
                slidProducts.isSaveEnabled = false
                currentIdItem?.let { id ->
                    detailsProductViewModel.getAvailableProducts(id)


                    // Optional: save ids for variants
                    currentIdItem = response.product.variants.firstOrNull()?.inventory_item_id
                    idVariants = response.product.variants.firstOrNull()?.id
                    idProduct = response.product.id
                }
            }
        }




        fun check(availableProduct: Int) {
            cou = detailsProductViewModel.getInventory(idProduct ?: 0)
            //  cou = Counter.getInventory(currentIdItem ?:0)

            cou?.let { currentCount ->
                if (availableProduct >= currentCount && currentIdItem != null) {

                    counterText.text = detailsProductViewModel.getInventory(idProduct ?: 0)

                        .toString()

                    Log.i("countterrr}}}check : ", cou.toString())


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


        fun requestDraftOrder(): DraftOrderRequest {

            var count = cou ?: 0
            var price = price?.toDouble() ?: 0.0
            var idVarient = idVariants?.toLong() ?: 0L


            var itemOrder = listOf(cou?.let { ItemsOrder(quantity = it, variant_id = idVarient) })


            var amount = (count * price * discount) / 100

            var appliedDiscount =
                AppliedDiscount("SummerDiscount", "45", "summerCode", "$amount", "percentage")


            val draftOrderRequest = DraftOrderRequest(
                draft_order = DraftOrderRequestBody(
                    line_items = itemOrder as List<ItemsOrder>,
                    applied_discount = appliedDiscount,
                    customer = pref.getString("customer_id", null)
                        ?.toLong()
                        ?.let { CustomerRef(it) },
                    use_customer_default_address = true
                )
            )



            return draftOrderRequest
        }

        val draftOrderState = MutableStateFlow<DraftOrderUiState>(DraftOrderUiState.Idle)


        suspend fun createDraftOrderAndNavigate() {
            val request = requestDraftOrder()

            val response = detailsProductViewModel.createDraftOrder(request)
            if (response?.isSuccessful != true || response.body() == null) return

            val body = response.body()?.draft_order
            val customerId = pref.getString("customer_id", null)?.toLong() ?: 0
            draftId = body?.idDraftOrder
            val header =
                body?.let { detailsProductViewModel.mapToDraftOrderHeaderEntity(it, customerId) }
            val lineItems = body?.line_items?.map { item ->
                draftId?.let {
                    item.copy(
                        draftOrderId = it,
                        customerId = customerId
                    )
                }
            } ?: emptyList()

            // Save in correct order (atomic)
            if (header != null) {
                detailsProductViewModel.saveDraftOrder(header, lineItems as List<LineItem>)
            }


        }





        buttonAddToCart.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                createDraftOrderAndNavigate()

                withContext(Dispatchers.Main) {
                    // 4️⃣ Navigate AFTER saving
                    val action = draftId?.let { it1 ->
                        DetailsProductFragmentDirections
                            .actionDetailsProductFragmentToCartFragment(
                                draftId = it1,
                                productQuantity = cou ?: 0,
                                fromScreen = "DetailsProduct"
                            )
                    }
                    action?.let { it1 -> findNavController().navigate(it1) }
                }
            }


        }


        viewLifecycleOwner.lifecycleScope.launch {
            detailsProductViewModel.inventory.collect { availableProduct ->


                increaseButton.setOnClickListener {
                    idProduct?.let { id ->

                        detailsProductViewModel.increaseInventory(id)

                        var test = detailsProductViewModel.getInventory(id)
                        Log.i("countIcreaseButton : ", test.toString())

                        if (availableProduct != null) {
                            check(availableProduct)
                        }
                    }
                }





                decreaseButton.setOnClickListener {
                    //  var coun =   detailsProductViewModel.countter--
                    idProduct?.let { id ->
                        detailsProductViewModel.decreaseInventory(id)

                        if (availableProduct != null) {
                            check(availableProduct)
                        }
                    }

                }

                if (availableProduct != null) {
                    check(availableProduct)
                }
            }

        }
// 2. برمجة السهم ليعود للخلف عند الضغط عليه
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            // findNavController().popBackStack(R.id.homeFragment, false)
        }


        Log.i("DetailFrag_inventory_item", "inventory_item = ${currentIdItem}")
    }
}









