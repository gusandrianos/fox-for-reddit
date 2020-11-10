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
import io.github.gusandrianos.foxforreddit.Constants.KIND_SUBREDDIT
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.formatValue

class SearchResultsAdapter(private val mKind: String, private val listener: OnSearchResultsItemClickListener) : PagingDataAdapter<Data, SearchResultsAdapter.ViewHolder>(ITEM_COMPARATOR) {

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
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        if (mKind == KIND_SUBREDDIT) {
            val members = "${formatValue(data?.subscribers!!.toDouble())} members"
            holder.txtName.text = data.displayNamePrefixed
            holder.txtInfo.text = members
            holder.name = data.displayNamePrefixed!!

            if (!data.iconImg.isNullOrEmpty())
                Glide.with(holder.itemView).load(data.iconImg.split("\\?".toRegex())[0]).into(holder.icon)
            else if (!data.communityIcon.isNullOrEmpty())
                Glide.with(holder.itemView).load(data.communityIcon.split("\\?".toRegex())[0]).into(holder.icon)
            else
                holder.icon.setImageResource(R.drawable.default_subreddit_image)
        } else {
            val prefixefName = "u/${data?.name}"
            val totalKarma = data?.awardeeKarma!! + data.awarderKarma+ data.commentKarma + data.linkKarma
            val karma = "${formatValue(totalKarma.toDouble())} karma"
            holder.txtName.text = prefixefName
            holder.txtInfo.text = karma
            holder.name = data.name!!

            if (!data.iconImg.isNullOrEmpty())
                Glide.with(holder.itemView).load(data.iconImg).into(holder.icon)
            else
                holder.icon.setImageResource(R.drawable.default_profile_image)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById(R.id.search_item_icon) as ImageView
        val txtName: TextView = view.findViewById(R.id.txt_search_item_name)
        val txtInfo: TextView = view.findViewById(R.id.txt_search_item_info)
        lateinit var name: String

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
                listener.onSearchItemClick(item)
            }
        }
    }

    interface OnSearchResultsItemClickListener {
        fun onSearchItemClick(item: Data)
    }
}