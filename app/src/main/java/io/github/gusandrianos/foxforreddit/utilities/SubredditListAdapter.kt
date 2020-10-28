package io.github.gusandrianos.foxforreddit.utilities

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

class SubredditListAdapter(private val listener: OnItemClickListener) : PagingDataAdapter<Data, SubredditListAdapter.ViewHolder>(ITEM_COMPARATOR) {

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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subredditImg = view.findViewById(R.id.img_subreddit_list_item_icon) as ImageView
        val subredditTitle = view.findViewById(R.id.txt_subreddit_list_item_title) as TextView

        init {
            itemView.setOnClickListener {
                onClick(bindingAdapterPosition)
            }
        }
    }

    fun onClick(position: Int) {
        if (position != RecyclerView.NO_POSITION) {    //For index -1. (Ex. animation and item deleted and its position is -1)
            val item = getItem(position)
            if (item != null) {
                listener.onItemClick(item)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.subreddit_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.subredditTitle.text = data!!.displayNamePrefixed
        if (data.iconImg!!.isNotEmpty())
            Glide.with(holder.itemView).load(data.iconImg.split("\\?".toRegex())[0]).into(holder.subredditImg)
        else if (data.communityIcon!!.isNotEmpty()) {
            val toLoad = data.communityIcon.split("\\?".toRegex())[0]
            Glide.with(holder.itemView).load(toLoad).into(holder.subredditImg)
        } else {
            holder.subredditImg.setImageResource(R.drawable.default_subreddit_image)
        }
    }
}