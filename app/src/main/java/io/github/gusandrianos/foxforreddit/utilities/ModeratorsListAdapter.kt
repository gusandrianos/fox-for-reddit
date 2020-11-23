package io.github.gusandrianos.foxforreddit.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Moderator

class ModeratorsListAdapter(private val moderators: List<Moderator>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ModeratorsListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var moderatorName = view.findViewById(R.id.text_moderator_name) as TextView

        init {
            itemView.setOnClickListener {
                onClick(bindingAdapterPosition)
            }
        }
    }

    fun onClick(position: Int) {
        if (position != RecyclerView.NO_POSITION) {    //For index -1. (Ex. animation and item deleted and its position is -1)
            val item = moderators[position]
            listener.onItemClick(item.name!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_moderators, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.moderatorName.text = moderators[position].name
    }

    override fun getItemCount(): Int {
        return moderators.size
    }

    interface OnItemClickListener {
        fun onItemClick(username: String)
    }
}