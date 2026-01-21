package com.example.shopenest.homescreen.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.homescreen.viewmodel.HomeViewModel
import com.example.shopenest.homescreen.viewmodel.HomeViewModelFactory
import com.example.shopenest.homescreen.viewmodel.SharedFavViewModel
import com.example.shopenest.model.Product
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import kotlinx.coroutines.launch


class KidsFragment : Fragment(),OnFavClickListener {


    lateinit var kidsViewModel:HomeViewModel
    lateinit var kidsAdapter: GenericHomeAdapter
    lateinit var kidsFactory: HomeViewModelFactory
    lateinit var images: MutableList<Int>
    private val sharedViewModel: SharedFavViewModel by activityViewModels()
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


        kidsAdapter = GenericHomeAdapter(requireView(), this) { proId ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsProductFragment(proId)

            // فحص إضافي للأمان: التأكد من أننا في الوجهة الصحيحة قبل الانتقال
            if (findNavController().currentDestination?.id == R.id.homeFragment) {
                findNavController().navigate(action)
            }
        }
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

    override fun onFavClick(product: Product) {
        // نمرر المنتج للـ ViewModel فقط
        Log.i("prodadded",product.title)
        sharedViewModel.passProductToFav(product)

        // تغيير شكل القلب في الـ UI
        Toast.makeText(context, "Add To Favrouit ", Toast.LENGTH_SHORT).show()
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