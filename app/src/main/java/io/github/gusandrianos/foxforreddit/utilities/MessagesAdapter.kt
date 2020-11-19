package io.github.gusandrianos.foxforreddit.utilities

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Data

class MessagesAdapter(private val listener: MessagesItemClickListener) : PagingDataAdapter<Data, MessagesAdapter.ViewHolder>(ITEM_COMPARATOR) {

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
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        val user = "u/${data?.author}"
        holder.txtUser.text = user
        holder.txtTimeAgo.text = DateUtils.getRelativeTimeSpanString(data?.createdUtc as Long * 1000).toString()
        holder.txtSubject.text = data?.subject
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUser: TextView = view.findViewById(R.id.txt_message_item_sent_by_user)
        val txtTimeAgo: TextView = view.findViewById(R.id.txt_message_item_time_ago)
        val txtSubject: TextView = view.findViewById(R.id.txt_message_item_subject)

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
                listener.onMessageItemClicked(item)
            }
        }
    }

    interface MessagesItemClickListener {
        fun onMessageItemClicked(item: Data)
    }
}
