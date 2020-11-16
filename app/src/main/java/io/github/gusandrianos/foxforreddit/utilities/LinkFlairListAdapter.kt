package io.github.gusandrianos.foxforreddit.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.libRG.CustomTextView
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Flair

class LinkFlairListAdapter(private val flairList: List<Flair>, private val listener: LinkFlairListAdapter.OnItemClickListener) : RecyclerView.Adapter<LinkFlairListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var flair = view.findViewById(R.id.item_custom_text_link_flair) as CustomTextView

        init {
            itemView.setOnClickListener {
                onClick(bindingAdapterPosition)
            }
        }
    }

    fun onClick(position: Int) {
        if (position != RecyclerView.NO_POSITION) {    //For index -1. (Ex. animation and item deleted and its position is -1)
            val item = flairList[position]
            listener.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_link_flair, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        FoxToolkit.makeFlair(flairList[position], holder.flair)
    }

    override fun getItemCount(): Int {
        return flairList.size
    }

    interface OnItemClickListener {
        fun onItemClick(flair: Flair)
    }
}
