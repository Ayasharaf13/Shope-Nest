package com.example.shopenest.homescreen

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R


class GenericAdapterSliderImage<T>(
    private val onClick: String?,
    private val items: List<T>,
    private val context: Context,

    private val bind: (item: T, itemView: View, position: Int) -> Unit,

    ) : RecyclerView.Adapter<GenericAdapterSliderImage<T>.GenericViewHolder>() {


    inner class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: T, position: Int) {
            bind(item, itemView, position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.imageslideritem, parent, false)
        return GenericViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        holder.bindItem(items[position], position)

        holder.itemView.setOnClickListener {
            //  onClick.invoke()
            Log.i("discoundclickImge", onClick.toString())


            // initializing clip board manager on below line.
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // initializing clip data on below line.
            val clipData = ClipData.newPlainText(
                "Copy discount code ",
                onClick
            ).apply {
                // on below line adding description
                description.extras = PersistableBundle().apply {
                    // only available for Android13 or higher
                    putBoolean(ClipDescription.MIMETYPE_TEXT_PLAIN, true)

                }
            }

            // on below line setting primary clip for clip board manager.
            clipboardManager.setPrimaryClip(clipData)


            Toast.makeText(context, "Copied promo code", Toast.LENGTH_SHORT).show()
        }

    }


    override fun getItemCount(): Int = items.size


}

