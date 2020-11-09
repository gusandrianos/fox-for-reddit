package io.github.gusandrianos.foxforreddit.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Listing

class SearchAdapter(private val listener: OnSearchItemClickListener, private val results: Listing) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        val child = results.data?.children!![position]
        val kind = child.kind
        val data = child.data!!


        if (kind.equals("t5")) {
            holder.txtName.text = results.data.children[position].data?.displayNamePrefixed
            holder.txtInfo.text = results.data.children[position].data?.subscribers.toString()
            holder.name = results.data.children[position].data?.displayNamePrefixed!!

            if (!data.iconImg.isNullOrEmpty())
                Glide.with(holder.itemView).load(data.iconImg.split("\\?".toRegex())[0]).into(holder.icon)
            else if (!data.communityIcon.isNullOrEmpty())
                Glide.with(holder.itemView).load(data.communityIcon.split("\\?".toRegex())[0]).into(holder.icon)
            else
                holder.icon.setImageResource(R.drawable.default_subreddit_image)

        } else {
            val prefixefName = "u/${results.data.children[position].data?.name}"
            val karma = "${results.data.children[position].data?.totalKarma.toString()} karma"
            holder.txtName.text = prefixefName
            holder.txtInfo.text = karma
            holder.name = results.data.children[position].data?.name!!

            if (!data.iconImg.isNullOrEmpty())
                Glide.with(holder.itemView).load(data.iconImg).into(holder.icon)
            else
                holder.icon.setImageResource(R.drawable.default_profile_image)
        }

        holder.kind = kind!!
    }

    override fun getItemCount(): Int {
        return results.data?.children?.size!!
    }

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById(R.id.search_item_icon) as ImageView
        val txtName: TextView = view.findViewById(R.id.txt_search_item_name)
        val txtInfo: TextView = view.findViewById(R.id.txt_search_item_info)
        lateinit var name: String
        lateinit var kind: String

        init {
            itemView.setOnClickListener {
                listener.onSearchItemClick(name, kind)
            }
        }
    }

    interface OnSearchItemClickListener {
        fun onSearchItemClick(destination: String, type: String)
    }

}



