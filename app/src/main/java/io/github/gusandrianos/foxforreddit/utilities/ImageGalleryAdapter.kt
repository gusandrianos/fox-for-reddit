package io.github.gusandrianos.foxforreddit.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.gusandrianos.foxforreddit.Constants
import io.github.gusandrianos.foxforreddit.R
import kotlinx.android.synthetic.main.image_gallery.view.*

class ImageGalleryAdapter(
        private val images: List<String>,
        private val screenMode: Int
) : RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_gallery, parent, false)
        return ImageGalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageGalleryViewHolder, position: Int) {
        val curImage = images[position]
        var url = "https://i.redd.it/$curImage.jpg"
        if (screenMode == Constants.FULLSCREEN) {
            holder.itemView.img_gallery.scaleType = ImageView.ScaleType.FIT_XY
            holder.itemView.img_gallery.adjustViewBounds = true
        }
        Glide.with(holder.itemView.img_gallery).load(url).into(holder.itemView.img_gallery)

    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageGalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}