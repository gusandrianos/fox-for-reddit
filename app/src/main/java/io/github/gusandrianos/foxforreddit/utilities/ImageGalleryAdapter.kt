package io.github.gusandrianos.foxforreddit.utilities

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import io.github.gusandrianos.foxforreddit.R
import kotlinx.android.synthetic.main.image_gallery.view.*

class ImageGalleryAdapter(
        private val images: List<String>,
        private val context: Context
) : RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_gallery, parent, false)
        return ImageGalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageGalleryViewHolder, position: Int) {
        val currentImage = images[position]
        Glide.with(holder.itemView.img_gallery).load(currentImage).into(holder.itemView.img_gallery)
        holder.itemView.setOnClickListener {
            StfalconImageViewer.Builder(context,images){ imageView, imageUrl -> Glide.with(context).load(imageUrl).into(imageView) }
                    .withStartPosition(position)
                    .show()
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageGalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}