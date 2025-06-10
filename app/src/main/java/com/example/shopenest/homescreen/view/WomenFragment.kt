package com.example.shopenest.homescreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class WomenFragment : Fragment() {

    lateinit var womenViewModel: HomeViewModel
    lateinit var womenAdapter: GenericHomeAdapter
    lateinit var womenFactory: HomeViewModelFactory

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
        return inflater.inflate(R.layout.fragment_women, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WomenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WomenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //   homeViewModel.getBrands()
        womenFactory = HomeViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            ))

        womenViewModel =
            ViewModelProvider(this, womenFactory).get(HomeViewModel::class.java)


        womenViewModel.getProductWomen()



        var recyclerWomen : RecyclerView = view.findViewById(R.id.recylerWomen)

        womenAdapter = GenericHomeAdapter(requireView(),"Home")

        //   recyclerBrand.layoutManager = LinearLayoutManager(requireContext(),HorizontalScrollView)

        recyclerWomen.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )

        // Start collecting the StateFlow
        lifecycleScope.launch {
            womenViewModel.product.collect { productList ->

                recyclerWomen.adapter = womenAdapter
                womenAdapter.submitList(productList)

            }

        }


    }


}