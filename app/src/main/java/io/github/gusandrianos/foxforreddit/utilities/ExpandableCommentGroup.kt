package io.github.gusandrianos.foxforreddit.utilities

import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.formatValue
import kotlinx.android.synthetic.main.single_post_expandable_comment.view.*

class ExpandableCommentGroup constructor(
        mComment: ChildrenItem,
        depth: Int = 0,
        linkId: String,
        listener: ExpandableCommentItem.OnItemClickListener
) : ExpandableGroup(ExpandableCommentItem(mComment, depth, linkId, listener)) {

    init {
        var repliesItem: ChildrenItem? = null

        if (mComment.data!!.replies != null) {
            if (mComment.data.replies !is String) {
                val repliesType = object : TypeToken<ChildrenItem?>() {}.type
                val gson = Gson()
                repliesItem = gson.fromJson(gson.toJsonTree(mComment.data.replies).asJsonObject, repliesType)
            }
        } else {
            repliesItem = null;
        }

        if (repliesItem != null)
            for (comment in repliesItem.data!!.children!!) {
                var item: ChildrenItem
                item = if (comment is String) {
                    ChildrenItem(comment)
                } else {
                    val childType = object : TypeToken<ChildrenItem?>() {}.type
                    val gson = Gson()
                    gson.fromJson(gson.toJsonTree(comment).asJsonObject, childType)
                }
                add(ExpandableCommentGroup(item, item.data!!.depth, linkId, listener)).apply { isExpanded = true }
            }
    }
}

open class ExpandableCommentItem constructor(
        private val mComment: ChildrenItem,
        private val depth: Int,
        private val linkId: String,
        private val listener: OnItemClickListener
) : Item<GroupieViewHolder>(), ExpandableItem {
    private lateinit var expandableGroup: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

    override fun getLayout(): Int {
        return R.layout.single_post_expandable_comment
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (mComment.kind.equals("more")) {
            addDepthViewsForLoadMore(viewHolder)
            viewHolder.itemView.cl_comment.visibility = View.GONE
            viewHolder.itemView.cl_load_more.visibility = View.VISIBLE
            viewHolder.itemView.cl_load_more.tag = mComment.data!!.parentId
            var moreChildren = ""
            var i = 0
            for (child in mComment.data.children!!) {
                if (i < 100)
                    moreChildren += "," + (child as String)
                i++
            }
            viewHolder.itemView.txt_more_children.apply {
                setOnClickListener {
                    listener.onLoadMoreClicked(linkId, moreChildren.removePrefix(","), position)
                }
            }
            viewHolder.itemView.txt_more_children.text = "$i More Replies"
        } else {
            addDepthViews(viewHolder)
            viewHolder.itemView.cl_comment.visibility = View.VISIBLE
            viewHolder.itemView.cl_load_more.visibility = View.GONE
            viewHolder.itemView.txt_comment_user.text = mComment.data!!.author
            viewHolder.itemView.comment_body.text = mComment.data.body
            viewHolder.itemView.txt_comment_score.text = formatValue(mComment.data.score.toDouble())
            viewHolder.itemView.apply {
                setOnClickListener {
                    expandableGroup.onToggleExpanded()
                    true
                }
            }
        }
    }

    private fun addDepthViews(viewHolder: GroupieViewHolder) {
        viewHolder.itemView.separatorContainer.removeAllViews()
        viewHolder.itemView.separatorContainer.visibility =
                if (depth > 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        for (i in 1..depth) {
            val v: View = LayoutInflater.from(viewHolder.itemView.context)
                    .inflate(R.layout.separator_view, viewHolder.itemView.separatorContainer, false)
            viewHolder.itemView.separatorContainer.addView((v))
        }
    }

    private fun addDepthViewsForLoadMore(viewHolder: GroupieViewHolder) {
        viewHolder.itemView.separatorContainer2.removeAllViews()
        viewHolder.itemView.separatorContainer2.visibility =
                if (depth > 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        for (i in 1..depth) {
            val v: View = LayoutInflater.from(viewHolder.itemView.context)
                    .inflate(R.layout.separator_view, viewHolder.itemView.separatorContainer2, false)
            viewHolder.itemView.separatorContainer2.addView((v))
        }
    }

    interface OnItemClickListener {
        fun onLoadMoreClicked(linkId: String, moreChildren: String, position: Int)
    }
}
