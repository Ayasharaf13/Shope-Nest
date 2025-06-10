package com.example.shopenest.homescreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [SeeAllFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SeeAllFragment : Fragment() {
    lateinit var seeAllViewModel: HomeViewModel
    lateinit var seeAllAdapter: GenericHomeAdapter
    lateinit var seeAllFactory: HomeViewModelFactory

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
        return inflater.inflate(R.layout.fragment_see_all, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SeeAllFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SeeAllFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var recyclerSeeAll : RecyclerView = view.findViewById(R.id.recyclerSeelAll)

        seeAllAdapter = GenericHomeAdapter(requireView(),"seeAll")

        //   recyclerBrand.layoutManager = LinearLayoutManager(requireContext(),HorizontalScrollView)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerSeeAll.layoutManager = layoutManager




      seeAllFactory = HomeViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            ))


     seeAllViewModel =
            ViewModelProvider(this, seeAllFactory).get(HomeViewModel::class.java)





        val args: SeeAllFragmentArgs by navArgs()
        val tabPosition = args.position
        val id = args.id
        val isBrand = args.isItFromTheBrand


        if (tabPosition == 0 && !isBrand ){

            seeAllViewModel.getProductWomen()
        }else if (tabPosition == 1){

            seeAllViewModel.getProductMen()
        }else if (tabPosition == 2){
            seeAllViewModel.getProductKids()
        }else{
            seeAllViewModel.getProductsBrand(id)
        }

        lifecycleScope.launch(Dispatchers.Main) {

            seeAllViewModel.product.collect { products ->

                recyclerSeeAll.adapter = seeAllAdapter
                seeAllAdapter.submitList(products)


            }
        }
    }
}