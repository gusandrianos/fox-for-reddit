package io.github.gusandrianos.foxforreddit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Data

class SubredditListAdapter : PagingDataAdapter<Data, SubredditListAdapter.ViewHolder>(ITEM_COMPARATOR) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subredditImg = view.findViewById(R.id.img_generic_item_icon) as ImageView
        val subredditTitle = view.findViewById(R.id.txt_generic_item_title) as TextView
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {

            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.toString() == newItem.toString()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.generic_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.subredditTitle.text = data!!.displayNamePrefixed
        if (data.iconImg.isNotEmpty())
            Glide.with(holder.itemView).load(data.iconImg.split("\\?".toRegex())[0]).into(holder.subredditImg)
        else {
            val toLoad = data.communityIcon.split("\\?".toRegex())[0]
            Glide.with(holder.itemView).load(toLoad).into(holder.subredditImg)
        }
    }
}