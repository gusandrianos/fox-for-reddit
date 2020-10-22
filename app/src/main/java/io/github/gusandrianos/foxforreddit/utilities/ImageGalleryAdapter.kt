package io.github.gusandrianos.foxforreddit.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.gusandrianos.foxforreddit.R
import kotlinx.android.synthetic.main.image_gallery.view.*

class ImageGalleryAdapter(
        val images: List<String>
) : RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_gallery, parent, false)
        return ImageGalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageGalleryViewHolder, position: Int) {
        val curImage = images[position]
        var url = "https://i.redd.it/$curImage.jpg"
        Glide.with(holder.itemView).load(url).into(holder.itemView.img_gallery)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageGalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}