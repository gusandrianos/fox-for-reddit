package io.github.gusandrianos.foxforreddit.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.util.Log
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
import io.github.gusandrianos.foxforreddit.data.models.Post


import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.Instant
import kotlin.math.pow

class PostAdapter(private val listener: OnItemClickListener) : PagingDataAdapter<Post, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    private val SELF = 1
    private val LINK = 2
    private val IMAGE = 3
    private val VIDEO = 4
    private val POLL = 5
    private val COMMENT = 6

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {

            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.toString() == newItem.toString()
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        if (currentItem?.name!!.startsWith("t1_")) {
            Log.i("", "getItemViewType: I work")
            return COMMENT
        }
        if (currentItem?.post_hint == null) {
            if (currentItem?.pollData != null)    //IF it is poll THEN must have poll data
                return POLL
            if (currentItem?.media != null)       //IF it's not the above THEN: IF it is rich/hosted:video THEN must have Media
                return VIDEO
            if (currentItem?.url!!.contains("https://i."))  //IF it's nothing from the above THEN: IF it is image THEN contains https://i. (not sure)
                return IMAGE
            return if (currentItem.domain.contains("self."))  //IF it's nothing from the above THEN: IF it is self THEN contains domain with self. (not sure)
                SELF
            else LINK
        }
        if (currentItem.post_hint.contains("self"))
            return SELF
        if (currentItem.post_hint.contains("image"))
            return IMAGE
        if (currentItem.post_hint.contains("link"))
            return LINK
        if (currentItem.post_hint.contains("video"))
            return VIDEO
        return if (currentItem.post_hint.contains("poll"))
            POLL
        else
            SELF    //If all the above do not feet, return SELF which is the "safest" type for binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        when (viewType) {
            SELF -> {
                view = layoutInflater.inflate(R.layout.post_self_layout, parent, false)
                return PostSelfViewHolder(view)
            }
            IMAGE -> {
                view = layoutInflater.inflate(R.layout.post_image_layout, parent, false)
                return PostImageViewHolder(view)
            }
            LINK -> {
                view = layoutInflater.inflate(R.layout.post_link_layout, parent, false)
                return PostLinkViewHolder(view)
            }
            VIDEO -> {
                view = layoutInflater.inflate(R.layout.post_video_layout, parent, false)
                return PostVideoViewHolder(view)
            }
            POLL -> {
                view = layoutInflater.inflate(R.layout.post_poll_layout, parent, false)
                return PostPollViewHolder(view)
            }
            COMMENT -> {
                view = layoutInflater.inflate(R.layout.post_comment_layout, parent, false)
                return PostCommentViewHolder(view)
            }
        }
        view = layoutInflater.inflate(R.layout.post_self_layout, parent, false) //Just in case...
        return PostSelfViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (getItemViewType(position)) {
            SELF -> {
                val postSelfViewHolder = holder as PostAdapter.PostSelfViewHolder
                postSelfViewHolder.onBind(currentItem!!)
            }
            IMAGE -> {
                val postImageViewHolder = holder as PostAdapter.PostImageViewHolder
                postImageViewHolder.onBind(currentItem!!)
            }
            LINK -> {
                val postLinkViewHolder = holder as PostAdapter.PostLinkViewHolder
                postLinkViewHolder.onBind(currentItem!!)
            }
            VIDEO -> {
                val postVideoViewHolder = holder as PostAdapter.PostVideoViewHolder
                postVideoViewHolder.onBind(currentItem!!)
            }
            POLL -> {
                val postPollViewHolder = holder as PostAdapter.PostPollViewHolder
                postPollViewHolder.onBind(currentItem!!)
            }
            COMMENT -> {
                val postCommentViewHolder = holder as PostAdapter.PostCommentViewHolder
                postCommentViewHolder.onBind(currentItem!!)
            }
        }
    }

    abstract inner class AbstractPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
                onClick(bindingAdapterPosition, Constants.POST_ITEM)
            }
            mImgPostSubreddit.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SUBREDDIT)
            }
            mTxtPostSubreddit.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SUBREDDIT)
            }
            mTxtPostUser.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_USER)
            }
            mImgBtnPostVoteUp.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_VOTE_UP)
            }
            mImgBtnPostVoteDown.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_VOTE_DOWN)
            }
            mBtnPostNumComments.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_COMMENTS_NUM)
            }
            mBtnPostShare.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SHARE)
            }
        }

        open fun onBind(post: Post) {
            val user = "Posted by u/" + post.author
            val subreddit = "r/" + post.subreddit
            //Glide.with(parent.getContext()).load().placeholder(R.drawable.ic_launcher_background).into(mImgPostSubreddit);  //MUST GET SUBREDDIT ICON
            mTxtPostSubreddit.text = subreddit
            mTxtPostUser.text = user
            mTxtTimePosted.text = DateUtils.getRelativeTimeSpanString(post.createdUtc * 1000).toString()
            mTxtPostTitle.text = post.title
            mTxtPostScore.text = formatValue(post.score.toDouble())
            mBtnPostNumComments.text = formatValue(post.numComments.toDouble())
            if (post.likes!=null){
                if(post.likes==true){
                    mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24_orange)
                    mTxtPostScore.setTextColor(Color.parseColor(("#FFE07812")))
                }else{
                    mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24_blue)
                    mTxtPostScore.setTextColor(Color.parseColor(("#FF5AA4FF")))
                }
            }
        }
    }

    inner class PostSelfViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {

        override fun onBind(post: Post) {
            super.onBind(post)
        }
    }

    inner class PostImageViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {
        private val mImgPostThumbnail: ImageView = itemView.findViewById(R.id.img_post_thumbnail)

        init {
            mImgPostThumbnail.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_THUMBNAIL)
            }
        }

        override fun onBind(post: Post) {
            super.onBind(post)
            //Todo if it is nsfw
            Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.ic_launcher_background).into(mImgPostThumbnail)
        }
    }

    inner class PostLinkViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {
        private val mFlThumbnail: FrameLayout = itemView.findViewById(R.id.fl_thumbnail)
        private val mImgPostThumbnail: ImageView = itemView.findViewById(R.id.img_post_thumbnail)
        private val mTxtPostDomain: TextView = itemView.findViewById(R.id.txt_post_domain)

        init {
            mFlThumbnail.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_THUMBNAIL)
            }
        }

        override fun onBind(post: Post) {
            super.onBind(post)
            Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.ic_launcher_background).into(mImgPostThumbnail)
            mTxtPostDomain.text = post.domain
            mTxtPostDomain.tag = post.urlOverriddenByDest
        }
    }

    inner class PostVideoViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {
        private val mFlThumbnail: FrameLayout = itemView.findViewById(R.id.fl_thumbnail)
        private val mImgPostThumbnail: ImageView = itemView.findViewById(R.id.img_post_thumbnail)

        init {
            mFlThumbnail.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_THUMBNAIL)
            }
        }

        override fun onBind(post: Post) {
            super.onBind(post)
            Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.ic_launcher_background).into(mImgPostThumbnail)
        }
    }

    inner class PostPollViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {
        private val mBtnPostVoteNow: Button = itemView.findViewById(R.id.btn_post_vote_now)
        private val mTxtPostVoteNum: TextView = itemView.findViewById(R.id.txt_post_vote_num)
        private val mTxtPostVoteTimeLeft: TextView = itemView.findViewById(R.id.txt_post_vote_time_left)

        init {
            mBtnPostVoteNow.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_VOTE_NOW)
            }
        }

        override fun onBind(post: Post) {
            super.onBind(post)
            val votes = post.pollData.totalVoteCount.toString() + " Votes"
            mTxtPostVoteNum.text = votes
            mTxtPostVoteTimeLeft.text = getPollEndingDate(post.pollData.votingEndTimestamp)
        }
    }

    // We know naming it PostComment instead of Comment is stupid, don't judge hey :p
    inner class PostCommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mTxtPostUser: TextView = itemView.findViewById(R.id.txt_post_user)
        private val mTxtTimePosted: TextView = itemView.findViewById(R.id.txt_time_posted)
        private val mTxtPostScore: TextView = itemView.findViewById(R.id.txt_post_score)
        private val mTxtPostTitle: TextView = itemView.findViewById(R.id.txt_post_title)
        private val mTxtPostSubreddit: TextView = itemView.findViewById(R.id.txt_post_subreddit)
        private val mTxtComment: TextView = itemView.findViewById(R.id.txt_comment)

        init {
            itemView.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_ITEM)
            }
            mTxtPostUser.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_USER)
            }
            mTxtPostSubreddit.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SUBREDDIT)
            }
        }

        fun onBind(comment: Post) {
            val subreddit = "In r/" + comment.subreddit
            mTxtPostUser.text = comment.author
            mTxtTimePosted.text = DateUtils.getRelativeTimeSpanString(comment.createdUtc * 1000).toString()
            mTxtPostScore.text = comment.score.toString().trim()
            mTxtPostTitle.text = comment.linkTitle
            mTxtPostSubreddit.text = subreddit
            mTxtComment.text = comment.body
        }
    }

    fun onClick(position: Int, clicked: String) {
        if (position != RecyclerView.NO_POSITION) {    //For index -1. (Ex. animation and item deleted and its position is -1)
            val item = getItem(position)
            if (item != null) {                          //Maybe getItem return null
                listener.onItemClick(item, clicked)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: Post, pressed: String)
    }
}



fun getPollEndingDate(timestamp: Long): String {
    val now = Instant.now().toEpochMilli()
    if (now > timestamp)
        return "Poll has ended"
    return "Ends " + DateUtils.getRelativeTimeSpanString(timestamp, now, 0L, DateUtils.FORMAT_ABBREV_RELATIVE).toString()
}

fun formatValue(number: Double): String {
    var value = number
    if (value == 0.0) {
        return 0.toString()
    }
    val suffix = " kmbt"
    var formattedNumber = ""
    val formatter: NumberFormat = DecimalFormat("#,###.#")
    val power: Int = StrictMath.log10(value).toInt()
    value /= 10.0.pow(power / 3 * 3.toDouble())
    formattedNumber = formatter.format(value)
    formattedNumber += suffix[power / 3]
    return if (formattedNumber.length > 4) formattedNumber.replace("\\.[0-9]+".toRegex(), "") else formattedNumber
}