package com.example.shopenest.homescreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import kotlinx.coroutines.launch


class KidsFragment : Fragment() {


    lateinit var kidsViewModel:HomeViewModel
    lateinit var kidsAdapter: GenericHomeAdapter
    lateinit var kidsFactory: HomeViewModelFactory
    lateinit var images: MutableList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
        // Direct initialization at the point of declaration
        images = mutableListOf()


        images.add(R.drawable.discount3)
        images.add(R.drawable.discount4)
        images.addAll(images)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kids, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         // var  gridView: GridView = view.findViewById(R.id.gridProductKid);


      // create a object of myBaseAdapter
  //   var  baseAdapter: GridProductAdapter =  GridProductAdapter( images);
    //  gridView.setAdapter(baseAdapter);


        //   homeViewModel.getBrands()
        kidsFactory = HomeViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            ))


        kidsViewModel =
            ViewModelProvider(this, kidsFactory).get(HomeViewModel::class.java)


        kidsViewModel.getProductKids()


        var recyclerKids : RecyclerView = view.findViewById(R.id.recyclerKids)

        kidsAdapter = GenericHomeAdapter(requireView(),"Home")

        //   recyclerBrand.layoutManager = LinearLayoutManager(requireContext(),HorizontalScrollView)

        recyclerKids.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )





        // Start collecting the StateFlow
        lifecycleScope.launch {
            kidsViewModel.product.collect { productList ->

                recyclerKids.adapter = kidsAdapter
                kidsAdapter.submitList(productList)

            }

        }


    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KidsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}