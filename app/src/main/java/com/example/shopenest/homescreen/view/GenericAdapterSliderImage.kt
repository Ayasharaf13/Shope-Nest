package com.example.shopenest.homescreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopenest.R


class GenericAdapterSliderImage<T>(
    private val items: List<T>,
    private val context: Context,
    private val bind: (item: T, itemView: View, position: Int) -> Unit
) : RecyclerView.Adapter<GenericAdapterSliderImage<T>.GenericViewHolder>() {


    inner class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: T, position: Int) {
            bind(item, itemView, position)
        }
      //  val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.autoimage,parent,false)
        return GenericViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        holder.bindItem(items[position], position)
      //  holder.imageView.setImageResource(items[position])
    }

    override fun getItemCount(): Int = items.size


//class ImageAdapter(private val images: List<Int>,context: Context) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    //var context = context
    /*  class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.autoimage, parent, false)
        return ImageViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
   holder.imageView.setImageResource(images[position])

        holder.imageView.setOnClickListener {
            println("img slider viewPager ${images[position]}")

            // initializing clip board manager on below line.
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // initializing clip data on below line.
            val clipData = ClipData.newPlainText(
                "Copy if you want",
                "img Data"
            ) .apply {
                // on below line adding description
                description.extras = PersistableBundle().apply {
                    // only available for Android13 or higher
                    putBoolean(ClipDescription.MIMETYPE_TEXT_PLAIN, true)
                    // use raw string for older versions
                    // android.content.extra.IS_SENSITIVE
                }
            }

            // on below line setting primary clip for clip board manager.
            clipboardManager.setPrimaryClip(clipData)
        }


    }

    override fun getItemCount(): Int = images.size
}

   */
}

