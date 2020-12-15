package io.github.gusandrianos.foxforreddit.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.RulesItem
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.linkify.LinkifyPlugin
import org.apache.commons.text.StringEscapeUtils

class RulesListAdapter(private val rules: List<RulesItem>) : RecyclerView.Adapter<RulesListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ruleTitle = view.findViewById(R.id.text_rule_title) as TextView
        var ruleBody = view.findViewById(R.id.text_rule_body) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rules, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val markwon = Markwon.builder(holder.itemView.context)
            .usePlugin(TablePlugin.create(holder.itemView.context))
            .usePlugin(LinkifyPlugin.create())
            .build()
        holder.ruleTitle.text = StringEscapeUtils.unescapeXml(rules[position].shortName)

        if (rules[position].description.isNullOrEmpty())
            markwon.setMarkdown(holder.ruleBody, "")
        else
            markwon.setMarkdown(holder.ruleBody, StringEscapeUtils.unescapeXml(rules[position].description!!))
    }

    override fun getItemCount(): Int {
        return rules.size
    }
}