package com.example.shopenest.search.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.view.GenericHomeAdapter
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.search.viewmodel.SearchViewModel
import com.example.shopenest.search.viewmodel.SearchViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [SeeAllFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class SeeAllFragment : Fragment() {

    private var savedTitleBrand: String = ""
    lateinit var seeAllViewModel: SearchViewModel
    lateinit var seeAllFactory: SearchViewModelFactory

    lateinit var editTextSearch: EditText
    lateinit var searchIconFilter: ImageView

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


        var recyclerSeeAll: RecyclerView = view.findViewById(R.id.recyclerViewSearchResult)


        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerSeeAll.layoutManager = layoutManager




        seeAllFactory = SearchViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )


        seeAllViewModel =
            ViewModelProvider(requireActivity(), seeAllFactory).get(SearchViewModel::class.java)






        editTextSearch = view.findViewById(R.id.searchTextSearchResult)


        // إذا كانت الدالة (onItemClick) هي آخر معامل في الـ Constructor
        val adapter = GenericHomeAdapter(requireView()) { proId ->
            val action =
                SeeAllFragmentDirections.actionSeeAllFragmentToDetailsProductFragment(proId)

            // فحص إضافي للأمان: التأكد من أننا في الوجهة الصحيحة قبل الانتقال
            if (findNavController().currentDestination?.id == R.id.seeAllFragment) {
                findNavController().navigate(action)
            }
        }

        searchIconFilter = view.findViewById(R.id.searchIconSearchResultFilter)

        searchIconFilter.setOnClickListener {

            val action = SeeAllFragmentDirections
                .actionSeeAllFragmentToBottomFilterDialogFragment()
            findNavController().navigate(action)


        }





        recyclerSeeAll.setLayoutManager(
            GridLayoutManager(
                requireContext(),
                2 // Number of columns you want in the grid

            )
        )




        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(inputUser: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(input: Editable?) {
                seeAllViewModel.searchAllProducts(input.toString())

            }


        })


        val argsSeeAll: SeeAllFragmentArgs by navArgs()
        var price1 = argsSeeAll.rangePrice1
        var price2 = argsSeeAll.rangePrice2
        var isApply = argsSeeAll.isApply


        val args: SeeAllFragmentArgs by navArgs()
        val tabPosition = args.position
        //  var titleBrand= ""


        if (args.isItFromTheBrand) {
            seeAllViewModel.titleBrand = args.title

            Log.i("range_TitleaaaaBrandddd_1:    ", args.title)
        }

        val titleBrand = seeAllViewModel.titleBrand ?: args.title

        Log.i("range_TitleaaaaBrandddd_2:    ", args.title)



        Log.i("range_Titleaaaa:    ", titleBrand)
        Log.i("range_TitleBBB:    ", args.isItFromTheBrand.toString())
        seeAllViewModel.getProductsBrand(titleBrand)

        if (isApply) {

            seeAllViewModel.filterProductsBasedPrice(price1, price2, titleBrand)

            Log.i("range1:    ", price1.toString())
            Log.i("range2:    ", price2.toString())
            Log.i("range_Title:    ", titleBrand)
        }
        lifecycleScope.launch(Dispatchers.Main) {

            seeAllViewModel.filterProducts.collect { filterList ->

                recyclerSeeAll.adapter = adapter
                adapter.submitList(filterList)


            }

        }


        lifecycleScope.launch(Dispatchers.Main) {

            seeAllViewModel.product.collect { products ->

                recyclerSeeAll.adapter = adapter
                adapter.submitList(products)


            }
        }


    }


}