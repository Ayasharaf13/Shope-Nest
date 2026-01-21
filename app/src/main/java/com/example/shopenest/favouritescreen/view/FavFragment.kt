package com.example.shopenest.favouritescreen.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.favouritescreen.viewmodel.FavViewModel
import com.example.shopenest.favouritescreen.viewmodel.FavViewModelFactory
import com.example.shopenest.homescreen.view.HomeFragmentDirections
import com.example.shopenest.homescreen.view.OnFavClickListener
import com.example.shopenest.homescreen.viewmodel.SharedFavViewModel
import com.example.shopenest.model.Product
import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.example.shopenest.utilities.CustomerPref
import kotlinx.coroutines.launch



class FavFragment : Fragment() {

    lateinit var favViewModel:FavViewModel
    lateinit var favViewModelFactory: FavViewModelFactory
    private val sharedViewModel: SharedFavViewModel by activityViewModels()

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
        return inflater.inflate(R.layout.fragment_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favViewModelFactory = FavViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )



       favViewModel =
            ViewModelProvider(this, favViewModelFactory).get(FavViewModel::class.java)

       var idCustomer = CustomerPref(requireContext() ).getCustomerId()

        favViewModel.getAllProducts(idCustomer.let { it?.toLong() ?: 0L })

        var recyclerFav:RecyclerView = view.findViewById(R.id.recyclerFav)



        // 1. تعريف الـ Adapter مع تمرير الدالتين بوضوح
        val favAdapter = FavAdapter(
            requireContext(),
            requireView(),
            "Home",
            onItemClick = { proId ->
                // الكود الذي ينفذ عند الضغط على الكارد (الانتقال للتفاصيل)
                if (findNavController().currentDestination?.id == R.id.favFragment) {
                    val action = FavFragmentDirections.actionFavFragmentToDetailsProductFragment(proId)
                    findNavController().navigate(action)
                }
            },
            onRemoveClick = { proId ->
                // الكود الذي ينفذ عند الضغط على زر الحذف (ViewModel)
                val customerId = idCustomer?.toLongOrNull() ?: 0L
                favViewModel.deleteProduct(proId, customerId)

                // نصيحة: يمكنك إضافة Toast هنا لتأكيد الحذف للمستخدم
                Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
        )



     //Avoid by navArgs() if Fragment can open without Safe Args
        val product: Product? = try {
            FavFragmentArgs.fromBundle(requireArguments()).product
        } catch (e: Exception) {
            null
        }


        val fromScreen: String? = arguments?.getString("fromscreen")

        if (fromScreen == "Home" && product != null) {
          //  favViewModel.saveProduct(product)

        }


        // داخل onViewCreated
        viewLifecycleOwner.lifecycleScope.launch {
            // هذه الدالة تضمن أن الـ Flow يتوقف عن العمل تماماً عندما تخرج من الشاشة
            // ويبدأ العمل فقط عندما تعود إليها (STARTED)
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.selectedProduct.collect { product ->
                    product?.let {
                        // 1. تنفيذ عملية الحفظ
                        favViewModel.saveProduct(it)

                        Log.i("FavFragment", "تم حفظ المنتج: ${it.title}")

                        // 2. تصفير الحالة فوراً
                        sharedViewModel.clearProduct()
                    }
                }
            }
        }



        recyclerFav.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            GridLayoutManager.VERTICAL,
            false
        )




        // Start collecting the StateFlow
        lifecycleScope.launch {
            favViewModel.products?.collect { productList ->

                recyclerFav.adapter = favAdapter
                favAdapter.submitList(productList)
            }



        }




    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }


}