package com.example.shopenest.address.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.address.viewmodel.AddressViewModel
import com.example.shopenest.address.viewmodel.AddressViewModelFactory
import com.example.shopenest.db.ConcreteLocalSource
import com.example.shopenest.model.CustomerBody
import com.example.shopenest.model.CustomerRequest

import com.example.shopenest.model.Repository
import com.example.shopenest.network.ShoppingClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [DisplaySavedAddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisplaySavedAddressFragment : Fragment() {

   lateinit var addressAdapter: AddressAdapter

    lateinit var pref: SharedPreferences
   lateinit var buttonAddNewAddress :Button

    private var selectedAddressId: Long? = null


    lateinit var  addressViewModel: AddressViewModel
    lateinit var addressFactory: AddressViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }


        addressFactory = AddressViewModelFactory(
            Repository.getInstance(
                ShoppingClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            ))

        addressViewModel =
            ViewModelProvider(this, addressFactory).get(AddressViewModel::class.java)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_saved_address, container, false)
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        pref = requireContext().
        getSharedPreferences("MyPref", Context.MODE_PRIVATE) // 0 - for private mode

        val customerId = pref.getString("customer_id", null)

        customerId?.toLongOrNull()?.let { id ->
            addressViewModel.getAddresses(id)

        } ?: run {
            Log.e("Customer", "customer_id is null or invalid")
        }


        var recyclerAddress : RecyclerView = view.findViewById(R.id.recyclerAddress)

        buttonAddNewAddress = view.findViewById(R.id.buttonaddNewAddress)


        //Attach ItemTouchHelper for swipe
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val address = addressAdapter.currentList[position]

                // Call the lambda
              //  addressAdapter.onDeleteSwipe(address)

                addressViewModel.deleteAddress(address)

                // Optionally show undo Snackbar
                Snackbar.make(recyclerAddress, "Address deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        // You could reinsert the item in ViewModel if needed
                       // addressViewModel.insertAddress(address)
                    }.show()
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerAddress)



        recyclerAddress.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        val args: DisplaySavedAddressFragmentArgs by navArgs()

        var screen = args.fromScreen


       // addressAdapter = AddressAdapter(requireView())
        addressAdapter = AddressAdapter(requireView()) { addressId ->
            // Handle set default action here
            selectedAddressId = addressId
            customerId?.let { id ->
                addressViewModel.setDefaultAddress(id.toLong(), addressId)
            }

           // addressViewModel.setDefaultAddress(addressId , customerId.let { it.toLong() })
        }




        if ( screen == "profile"  ||  selectedAddressId !=null  || screen == "checkout"  || screen == "address") {

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    addressViewModel.address.collect { addressList ->
                        // This block is called every time the data changes
                        val addresses = addressList
                        if (addresses != null && addresses.isNotEmpty() || screen == "profile" || screen == "checkout" ||  screen == "address"){
                                // Do something with the list
                                // e.g. update adapter
                                //adapter.submitList(addressList)
                                recyclerAddress.adapter = addressAdapter

                                addressAdapter.submitList(addresses)
                            } else {
                                // Handle empty state
                         //   recyclerAddress.visibility = View.GONE
                            Log.i("NoAddress","NoAddressToDisplay")

                            }
                        }
                    }
                }

        }



        buttonAddNewAddress.setOnClickListener {

            val action = DisplaySavedAddressFragmentDirections
                .actionDisplaySavedAddressFragmentToAdressFragment()
            findNavController().navigate(action)


        }





    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DisplaySavedAddressFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DisplaySavedAddressFragment().apply {
                arguments = Bundle().apply {


                }


            }


    }


}