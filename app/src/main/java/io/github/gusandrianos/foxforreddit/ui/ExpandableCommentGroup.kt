package io.github.gusandrianos.foxforreddit.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.generatedComments.comments.ChildrenItem

import kotlinx.android.synthetic.main.single_post_expandable_comment.view.*

class ExpandableCommentGroup constructor(private val mComment: ChildrenItem, private val depth: Int = 0) : ExpandableGroup(ExpandableCommentItem(mComment, depth)) {

    init {
        if (mComment.data.replies != null)
            for (comment in mComment.data.replies.data.children) {
                add(ExpandableCommentGroup(comment, comment.data.depth))
            }
    }

}

open class ExpandableCommentItem constructor(private val mComment: ChildrenItem, private val depth: Int) : Item<GroupieViewHolder>(), ExpandableItem {
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
            viewHolder.itemView.cl_comment.visibility= View.GONE
            viewHolder.itemView.cl_loadmore.visibility= View.VISIBLE
            viewHolder.itemView.cl_loadmore.tag = mComment.data.parentId
            var more = ""
            var i = 0
            for(child in mComment.data.children) {
                if(i<100)
                    more += child.loadMoreChild + ","
                i++
            }
            viewHolder.itemView.txt_more_childs.text= mComment.data.parentId+"$i Replies"
        } else {
            addDepthViews(viewHolder)
            viewHolder.itemView.cl_comment.visibility= View.VISIBLE
            viewHolder.itemView.cl_loadmore.visibility= View.GONE
            viewHolder.itemView.tv_user.text = mComment.data.author
            viewHolder.itemView.body.text = mComment.data.body
            viewHolder.itemView.tv_votes.text = mComment.data.score.toString()
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
}