package io.github.gusandrianos.foxforreddit.utilities

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Thing
import io.noties.markwon.Markwon
import org.apache.commons.text.StringEscapeUtils

class ConversationAdapter(
    private val thing: Thing,
    private val listener: UserClickedListener,
    private val markwon: Markwon
) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUser: TextView = view.findViewById(R.id.txt_messages_with_user_item_username)
        val txtTimeSent: TextView = view.findViewById(R.id.txt_messages_with_user_item_time_sent)
        val txtBody: TextView = view.findViewById(R.id.txt_messages_with_user_item_body)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_messages_with_user, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = thing.data?.children!![position].data!!
        holder.txtUser.text = data.author
        holder.txtTimeSent.text = DateUtils.getRelativeTimeSpanString(data.createdUtc.times(1000)).toString()
        markwon.setMarkdown(holder.txtBody, StringEscapeUtils.unescapeXml(data.body))
        holder.txtUser.setOnClickListener { listener.onUserClicked(holder.txtUser.text.toString()) }
    }

    override fun getItemCount(): Int {
        return thing.data?.children?.size!!
    }

    interface UserClickedListener {
        fun onUserClicked(user: String?)
    }
}
