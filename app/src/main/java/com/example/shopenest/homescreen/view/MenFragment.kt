package com.example.shopenest.homescreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MenFragment : Fragment() {


    lateinit var menViewModel: HomeViewModel
    lateinit var menAdapter: GenericHomeAdapter
    lateinit var menFactory: HomeViewModelFactory

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
        return inflater.inflate(R.layout.fragment_men, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        menFactory = HomeViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            ))


        menViewModel =
            ViewModelProvider(this, menFactory).get(HomeViewModel::class.java)


        menViewModel.getProductMen()


        var recyclerMen : RecyclerView = view.findViewById(R.id.recyclerMen)

        menAdapter = GenericHomeAdapter(requireView(),"Home")



        recyclerMen.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )



        // Start collecting the StateFlow
        lifecycleScope.launch {
            menViewModel.product.collect { productList ->


                    recyclerMen.adapter = menAdapter

                    menAdapter.submitList(productList)
            }
        }

    }
    companion object {


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}