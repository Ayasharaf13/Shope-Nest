package com.example.shopenest.homescreen.view

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.GenericAdapterSliderImage

import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.ImageProduct
import com.example.shopenest.model.Product
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailsProductFragment : Fragment() {

  lateinit var detailsProductViewModel:HomeViewModel
    lateinit var detailsProductFactory: HomeViewModelFactory
    lateinit var textDetails:TextView
    lateinit var textTitle:TextView
    lateinit var  slidProducts:ViewPager2
    lateinit var sliderImageAdapter: GenericAdapterSliderImage<ImageProduct>

    lateinit var productImages :Array<Image>

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         textDetails = view.findViewById(R.id.textDetails)
        textTitle = view.findViewById(R.id.textNameProduct)
        slidProducts = view.findViewById(R.id.productSlider)
      //  GenericAdapterSliderImage()




        detailsProductFactory = HomeViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            ))


        detailsProductViewModel =
            ViewModelProvider(this, detailsProductFactory).get(HomeViewModel::class.java)

        val args: DetailsProductFragmentArgs by navArgs()
        val idProduct = args.idProductDetails

        detailsProductViewModel.getProductDetails(idProduct)


        Log.i("DetailsFragmenhhhhht", "bodyHtml = ${idProduct}")

        lifecycleScope.launch(Dispatchers.Main) {

            detailsProductViewModel.productDetails.collect { responce ->
                Log.i("DetailsFragment", "bodTiltle = ${responce.product.body_html}")
          textDetails.text =   responce.product.body_html
                textTitle.text = responce.product.title
              val imagess =  responce.product.images
                sliderImageAdapter =  GenericAdapterSliderImage<ImageProduct>(imagess,requireContext(),  bind = { product, view, position ->

                 var imageView =   view.findViewById<ImageView>(R.id.imageView)
                   // imageView.setImageResource(R.drawable.ic_baseline_favorite_24)
                    Glide.with(imageView.context)
                        .load(imagess[position].src)
                        .placeholder(R.drawable.ic_launcher_background) // optional
                        .error(R.drawable.custombottomnav)       // optional
                        .into(imageView)


                })
                //  viewPagerAds.adapter = adapter
                slidProducts.adapter = sliderImageAdapter



              //      sliderProductAdapter = ImageAdapter( ,requireContext())
            }
        }

    }








}