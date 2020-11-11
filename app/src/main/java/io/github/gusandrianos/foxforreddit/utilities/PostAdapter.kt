package io.github.gusandrianos.foxforreddit.utilities

import android.graphics.Color
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.gusandrianos.foxforreddit.Constants
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.formatValue

import java.time.Instant

class PostAdapter(private val listener: OnItemClickListener) : PagingDataAdapter<Data, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {

            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.toString() == newItem.toString()
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        if (currentItem?.name!!.startsWith("t1_"))
            return Constants.COMMENT

        if (currentItem.post_hint == null) {
            if (currentItem.pollData != null)    //IF it is poll THEN must have poll data
                return Constants.POLL
            if (currentItem.media != null)       //IF it's not the above THEN: IF it is rich/hosted:video THEN must have Media
                return Constants.VIDEO
            if (currentItem.url!!.contains("https://i.") || currentItem.isGallery == true)  //IF it's nothing from the above THEN: IF it is image THEN contains https://i. (not sure)
                return Constants.IMAGE
            return if (currentItem.domain!!.contains("self."))  //IF it's nothing from the above THEN: IF it is self THEN contains domain with self. (not sure)
                Constants.SELF
            else Constants.LINK
        }
        if (currentItem.post_hint.contains("self"))
            return Constants.SELF
        if (currentItem.post_hint.contains("image"))
            return Constants.IMAGE
        if (currentItem.post_hint.contains("link"))
            return Constants.LINK
        if (currentItem.post_hint.contains("video"))
            return Constants.VIDEO
        return if (currentItem.post_hint.contains("poll"))
            Constants.POLL
        else
            Constants.SELF    //If all the above do not feet, return SELF which is the "safest" type for binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        when (viewType) {
            Constants.SELF -> {
                view = layoutInflater.inflate(R.layout.post_self_layout, parent, false)
                return PostSelfViewHolder(view, Constants.SELF)
            }
            Constants.IMAGE -> {
                view = layoutInflater.inflate(R.layout.post_image_layout, parent, false)
                return PostImageViewHolder(view, Constants.IMAGE)
            }
            Constants.LINK -> {
                view = layoutInflater.inflate(R.layout.post_link_layout, parent, false)
                return PostLinkViewHolder(view, Constants.LINK)
            }
            Constants.VIDEO -> {
                view = layoutInflater.inflate(R.layout.post_video_layout, parent, false)
                return PostVideoViewHolder(view, Constants.VIDEO)
            }
            Constants.POLL -> {
                view = layoutInflater.inflate(R.layout.post_poll_layout, parent, false)
                return PostPollViewHolder(view, Constants.POLL)
            }
            Constants.COMMENT -> {
                view = layoutInflater.inflate(R.layout.post_comment_layout, parent, false)
                return PostCommentViewHolder(view, Constants.COMMENT)
            }
        }
        view = layoutInflater.inflate(R.layout.post_self_layout, parent, false) //Just in case...
        return PostSelfViewHolder(view, Constants.SELF)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (getItemViewType(position)) {
            Constants.SELF -> {
                val postSelfViewHolder = holder as PostSelfViewHolder
                postSelfViewHolder.onBind(currentItem!!)
            }
            Constants.IMAGE -> {
                val postImageViewHolder = holder as PostImageViewHolder
                postImageViewHolder.onBind(currentItem!!)
            }
            Constants.LINK -> {
                val postLinkViewHolder = holder as PostLinkViewHolder
                postLinkViewHolder.onBind(currentItem!!)
            }
            Constants.VIDEO -> {
                val postVideoViewHolder = holder as PostVideoViewHolder
                postVideoViewHolder.onBind(currentItem!!)
            }
            Constants.POLL -> {
                val postPollViewHolder = holder as PostPollViewHolder
                postPollViewHolder.onBind(currentItem!!)
            }
            Constants.COMMENT -> {
                val postCommentViewHolder = holder as PostCommentViewHolder
                postCommentViewHolder.onBind(currentItem!!)
            }
        }
    }

    abstract inner class AbstractPostViewHolder(itemView: View, private val mPostType: Int) : RecyclerView.ViewHolder(itemView) {
        private val mImgPostSubreddit: ImageView = itemView.findViewById(R.id.img_post_subreddit)
        private val mTxtPostSubreddit: TextView = itemView.findViewById(R.id.txt_post_subreddit)
        private val mTxtPostUser: TextView = itemView.findViewById(R.id.txt_post_user)
        private val mTxtTimePosted: TextView = itemView.findViewById(R.id.txt_time_posted)
        private val mTxtPostTitle: TextView = itemView.findViewById(R.id.txt_post_title)
        private val mTxtPostScore: TextView = itemView.findViewById(R.id.txt_post_score)
        private val mImgBtnPostVoteUp: ImageButton = itemView.findViewById(R.id.imgbtn_post_vote_up)
        private val mImgBtnPostVoteDown: ImageButton = itemView.findViewById(R.id.imgbtn_post_vote_down)
        private val mBtnPostNumComments: Button = itemView.findViewById(R.id.btn_post_num_comments)
        private val mBtnPostShare: Button = itemView.findViewById(R.id.btn_post_share)


        init {
            itemView.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_ITEM, mPostType)
            }
            mImgPostSubreddit.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SUBREDDIT, mPostType)
            }
            mTxtPostSubreddit.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SUBREDDIT, mPostType)
            }
            mTxtPostUser.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_USER, mPostType)
            }
            mImgBtnPostVoteUp.setOnClickListener {
                if (getItem((bindingAdapterPosition))?.likes == null || getItem((bindingAdapterPosition))?.likes == false) {
                    mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24_orange)
                    mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24)
                    mTxtPostScore.setTextColor(Color.parseColor("#FFE07812"))
                } else {
                    mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24)
                    mTxtPostScore.setTextColor(Color.parseColor("#AAAAAA"))
                }
                onClick(bindingAdapterPosition, Constants.POST_VOTE_UP, mPostType)
            }
            mImgBtnPostVoteDown.setOnClickListener {
                if (getItem((bindingAdapterPosition))?.likes == null || getItem((bindingAdapterPosition))?.likes == true) {
                    mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24_blue)
                    mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24)
                    mTxtPostScore.setTextColor(Color.parseColor("#FF5AA4FF"))
                } else {
                    mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24)
                    mTxtPostScore.setTextColor(Color.parseColor("#AAAAAA"))
                }
                onClick(bindingAdapterPosition, Constants.POST_VOTE_DOWN, mPostType)
            }
            mBtnPostNumComments.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_COMMENTS_NUM, mPostType)
            }
            mBtnPostShare.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SHARE, mPostType)
            }
        }

        open fun onBind(post: Data) {
            val user = "Posted by u/" + post.author
            val subreddit = "r/" + post.subreddit
            //Glide.with(parent.getContext()).load().placeholder(R.drawable.ic_launcher_background).into(mImgPostSubreddit);  //MUST GET SUBREDDIT ICON
            mTxtPostSubreddit.text = subreddit
            mTxtPostUser.text = user
            mTxtTimePosted.text = DateUtils.getRelativeTimeSpanString(post.createdUtc * 1000).toString()
            mTxtPostTitle.text = post.title
            mTxtPostScore.text = formatValue(post.score.toDouble())
            mBtnPostNumComments.text = formatValue(post.numComments.toDouble())
            if (post.likes != null) {
                if (post.likes == true) {
                    mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24_orange)
                    mTxtPostScore.setTextColor(Color.parseColor("#FFE07812"))
                } else {
                    mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24_blue)
                    mTxtPostScore.setTextColor(Color.parseColor("#FF5AA4FF"))
                }
            }
        }
    }

    inner class PostSelfViewHolder(itemView: View, private val mPostType: Int) : AbstractPostViewHolder(itemView, mPostType) {

        override fun onBind(post: Data) {
            super.onBind(post)
        }
    }

    inner class PostImageViewHolder(itemView: View, private val mPostType: Int) : AbstractPostViewHolder(itemView, mPostType) {
        private val mImgPostThumbnail: ImageView = itemView.findViewById(R.id.img_post_thumbnail)

        init {
            mImgPostThumbnail.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_THUMBNAIL, mPostType)
            }
        }

        override fun onBind(post: Data) {
            super.onBind(post)
            //Todo if it is nsfw
            Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.ic_launcher_background).into(mImgPostThumbnail)
        }
    }

    inner class PostLinkViewHolder(itemView: View, private val mPostType: Int) : AbstractPostViewHolder(itemView, mPostType) {
        private val mFlThumbnail: FrameLayout = itemView.findViewById(R.id.fl_thumbnail)
        private val mImgPostThumbnail: ImageView = itemView.findViewById(R.id.img_post_thumbnail)
        private val mTxtPostDomain: TextView = itemView.findViewById(R.id.txt_post_domain)

        init {
            mFlThumbnail.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_THUMBNAIL, mPostType)
            }
        }

        override fun onBind(post: Data) {
            super.onBind(post)
            Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.ic_launcher_background).into(mImgPostThumbnail)
            mTxtPostDomain.text = post.domain
            mTxtPostDomain.tag = post.urlOverriddenByDest
        }
    }

    inner class PostVideoViewHolder(itemView: View, private val mPostType: Int) : AbstractPostViewHolder(itemView, mPostType) {
        private val mFlThumbnail: FrameLayout = itemView.findViewById(R.id.fl_thumbnail)
        private val mImgPostThumbnail: ImageView = itemView.findViewById(R.id.img_post_thumbnail)

        init {
            mFlThumbnail.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_THUMBNAIL, mPostType)
            }
        }

        override fun onBind(post: Data) {
            super.onBind(post)
            Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.ic_launcher_background).into(mImgPostThumbnail)
        }
    }

    inner class PostPollViewHolder(itemView: View, private val mPostType: Int) : AbstractPostViewHolder(itemView, mPostType) {
        private val mBtnPostVoteNow: Button = itemView.findViewById(R.id.btn_post_vote_now)
        private val mTxtPostVoteNum: TextView = itemView.findViewById(R.id.txt_post_vote_num)
        private val mTxtPostVoteTimeLeft: TextView = itemView.findViewById(R.id.txt_post_vote_time_left)

        init {
            mBtnPostVoteNow.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_VOTE_NOW, mPostType)
            }
        }

        override fun onBind(post: Data) {
            super.onBind(post)
            val votes = post.pollData!!.totalVoteCount.toString() + " Votes"
            mTxtPostVoteNum.text = votes
            mTxtPostVoteTimeLeft.text = getPollEndingDate(post.pollData!!.votingEndTimestamp)
        }
    }

    // We know naming it PostComment instead of Comment is stupid, don't judge hey :p
    inner class PostCommentViewHolder(itemView: View, private val mPostType: Int) : RecyclerView.ViewHolder(itemView) {
        private val mTxtPostUser: TextView = itemView.findViewById(R.id.txt_post_user)
        private val mTxtTimePosted: TextView = itemView.findViewById(R.id.txt_time_posted)
        private val mTxtPostScore: TextView = itemView.findViewById(R.id.txt_post_score)
        private val mTxtPostTitle: TextView = itemView.findViewById(R.id.txt_post_title)
        private val mTxtPostSubreddit: TextView = itemView.findViewById(R.id.txt_post_subreddit)
        private val mTxtComment: TextView = itemView.findViewById(R.id.txt_comment)

        init {
            itemView.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_ITEM, mPostType)
            }
            mTxtPostUser.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_USER, mPostType)
            }
            mTxtPostSubreddit.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SUBREDDIT, mPostType)
            }
        }

        fun onBind(comment: Data) {
            val subreddit = "In r/" + comment.subreddit
            mTxtPostUser.text = comment.author
            mTxtTimePosted.text = DateUtils.getRelativeTimeSpanString(comment.createdUtc * 1000).toString()
            mTxtPostScore.text = comment.score.toString().trim()
            mTxtPostTitle.text = comment.linkTitle
            mTxtPostSubreddit.text = subreddit
            mTxtComment.text = comment.body
        }
    }

    fun onClick(position: Int, clicked: String, postType: Int) {
        if (position != RecyclerView.NO_POSITION) {    //For index -1. (Ex. animation and item deleted and its position is -1)
            val item = getItem(position)
            if (item != null) {                          //Maybe getItem return null
                listener.onItemClick(item, clicked, postType)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: Data, clicked: String, postType: Int)
    }
}

fun getPollEndingDate(timestamp: Long): String {
    val now = Instant.now().toEpochMilli()
    if (now > timestamp)
        return "Poll has ended"
    return "Ends " + DateUtils.getRelativeTimeSpanString(timestamp, now, 0L, DateUtils.FORMAT_ABBREV_RELATIVE).toString()
}
