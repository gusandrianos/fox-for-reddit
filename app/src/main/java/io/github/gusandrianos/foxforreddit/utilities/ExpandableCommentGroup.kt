package io.github.gusandrianos.foxforreddit.utilities

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwray.groupie.*
import io.github.gusandrianos.foxforreddit.Constants
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem
import io.github.gusandrianos.foxforreddit.ui.MainActivity
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.downVoteColor
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.formatValue
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.upVoteColor
import kotlinx.android.synthetic.main.single_post_expandable_comment.view.*

class ExpandableCommentGroup constructor(
        mComment: ChildrenItem,
        depth: Int = 0,
        linkId: String,
        listener: ExpandableCommentItem.OnItemClickListener,
        private val mainActivity: MainActivity
) : ExpandableGroup(ExpandableCommentItem(mComment, depth, linkId, listener, mainActivity)) {

    init {
        var repliesItem: ChildrenItem? = null

        if (mComment.data!!.replies != null) {
            if (mComment.data.replies !is String) {
                val repliesType = object : TypeToken<ChildrenItem?>() {}.type
                val gson = Gson()
                repliesItem = gson.fromJson(gson.toJsonTree(mComment.data.replies).asJsonObject, repliesType)
            }
        } else
            repliesItem = null;

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
                add(ExpandableCommentGroup(item, item.data!!.depth, linkId, listener, mainActivity))
                        .apply { isExpanded = true }
            }
    }
}

open class ExpandableCommentItem constructor(
        private val mComment: ChildrenItem,
        private val depth: Int,
        private val linkId: String,
        private val listener: OnItemClickListener,
        private val mainActivity: MainActivity
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
            val moreChildren = arrayListOf<String>()

            for (child in mComment.data.children!!) {
                moreChildren.add(child as String)
            }

            viewHolder.itemView.txt_more_children.apply {
                setOnClickListener {
                    if (mComment.data.count > 0)
                        listener.onClick(linkId, moreChildren, null, "", position)
                }
            }
            viewHolder.itemView.txt_more_children.text = "Show more"
        } else {
            setCommentActions(viewHolder.itemView, position)
            addDepthViews(viewHolder)
            viewHolder.itemView.cl_comment.visibility = View.VISIBLE
            viewHolder.itemView.cl_load_more.visibility = View.GONE
            viewHolder.itemView.txt_comment_user.text = mComment.data!!.author
            viewHolder.itemView.comment_body.text = mComment.data.body
            viewHolder.itemView.txt_comment_score.text = formatValue(mComment.data.score.toDouble())
            viewHolder.itemView.apply {
                setOnClickListener {
                    expandableGroup.onToggleExpanded()
                }
            }
        }
    }

    private fun setCommentActions(view: View, position: Int) {
        val upvote = view.findViewById(R.id.btn_comment_up_vote) as ImageButton
        val downvote = view.findViewById(R.id.btn_comment_down_vote) as ImageButton
        val score = view.findViewById(R.id.txt_comment_score) as TextView
        val reply = view.findViewById(R.id.btn_comment_reply) as ImageButton
        val moreActions = view.findViewById(R.id.btn_comment_more_actions) as ImageButton

        FoxToolkit.setLikedStatusOnButtons(mComment.data?.likes, upvote, downvote,
                score, mainActivity)

        upvote.setOnClickListener {
            if (!FoxToolkit.isAuthorized(mainActivity.application))
                FoxToolkit.promptLogIn(mainActivity)
            else {
                upVoteColor(mComment.data?.likes, upvote, downvote, score, mainActivity)
                listener.onClick(linkId, null, mComment, Constants.THING_VOTE_UP, position)
            }
        }

        downvote.setOnClickListener {
            if (!FoxToolkit.isAuthorized(mainActivity.application))
                FoxToolkit.promptLogIn(mainActivity)
            else {
                downVoteColor(mComment.data?.likes, upvote, downvote, score, mainActivity)
                listener.onClick(linkId, null, mComment, Constants.THING_VOTE_DOWN, position)
            }
        }


        reply.setOnClickListener {
            listener.onClick(linkId, null, mComment, Constants.THING_VOTE_REPLY, position)
        }

        moreActions.setOnClickListener {
            listener.onClick(linkId, null, mComment, Constants.THING_MORE_ACTIONS, position)
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
        fun onClick(linkId: String, moreChildren: ArrayList<String>?,
                    comment: ChildrenItem?, actionType: String, position: Int)
    }
}
