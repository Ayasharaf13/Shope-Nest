package com.example.shopenest.address.view



import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R
import com.example.shopenest.model.CustomerAddress



class AddressAdapter (var context: View ,   private var onSetDefault: (Long) -> Unit  ): ListAdapter<CustomerAddress, AddressAdapter.ViewHolder>(AddressDiffUtil()) {


    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var textAddress: TextView = itemview.findViewById(R.id.address_details)
        var textAddressName:TextView = itemview.findViewById(R.id.address_name)
        var textAddressPhone:TextView = itemview.findViewById(R.id.address_phone)
        var textSetDefault:TextView = itemview.findViewById(R.id.textSetDefault)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.addressitem, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val address = getItem(position)

        holder.textAddress.text = address.address1
        holder.textAddressName.text = address.first_name
        holder.textAddressPhone.text = address.phone


        holder.textSetDefault.setOnClickListener {

            if (address !=null) {

                onSetDefault(address.id)  // 👈 pass ID to fragment
                val action =
                    DisplaySavedAddressFragmentDirections.actionDisplaySavedAddressFragmentToCheckoutFragment()
                   Navigation.findNavController(context).navigate(action);

            }
        }


    }

    class AddressDiffUtil() : DiffUtil.ItemCallback<CustomerAddress>() {
        override fun areItemsTheSame(oldItem: CustomerAddress, newItem: CustomerAddress): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CustomerAddress, newItem: CustomerAddress): Boolean {
            return oldItem == newItem
        }


    }

}





