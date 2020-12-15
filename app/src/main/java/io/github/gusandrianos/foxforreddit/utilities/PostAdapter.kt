package io.github.gusandrianos.foxforreddit.utilities

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.libRG.CustomTextView
import io.github.gusandrianos.foxforreddit.Constants
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.db.TokenDao
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository
import io.github.gusandrianos.foxforreddit.ui.MainActivity
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.linkify.LinkifyPlugin
import jp.wasabeef.glide.transformations.BlurTransformation
import org.apache.commons.text.StringEscapeUtils


class PostAdapter(private val mainActivity: MainActivity,
                  private val listener: OnItemClickListener,
                  private val mTokenDao: TokenDao,
                  private val mTokenRepository: TokenRepository
) : PagingDataAdapter<Data, RecyclerView.ViewHolder>(POST_COMPARATOR) {

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
                return PostCommentViewHolder(view, Constants.COMMENT, parent.context)
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
        private val mTxtPostSubreddit: TextView = itemView.findViewById(R.id.txt_post_subreddit)
        private val mTxtPostUser: TextView = itemView.findViewById(R.id.txt_post_user)
        private val mTxtTimePosted: TextView = itemView.findViewById(R.id.txt_time_posted)
        private val txtIsSpoiler: TextView = itemView.findViewById(R.id.txt_post_is_spoiler)
        private val txtIsOver18: TextView = itemView.findViewById(R.id.txt_post_is_over_18)
        private val mTxtPostTitle: TextView = itemView.findViewById(R.id.txt_post_title)

        private val customTxtPostFlair: CustomTextView = itemView.findViewById(R.id.post_link_flair)
        private val mTxtPostScore: TextView = itemView.findViewById(R.id.txt_post_score)
        private val mImgBtnPostVoteUp: ImageButton = itemView.findViewById(R.id.imgbtn_post_vote_up)
        private val mImgBtnPostVoteDown: ImageButton = itemView.findViewById(R.id.imgbtn_post_vote_down)
        private val mTxtPostNumComments: TextView = itemView.findViewById(R.id.btn_post_num_comments)
        private val mTxtPostShare: TextView = itemView.findViewById(R.id.btn_post_share)
        private val mTxtPostMoreActions: TextView = itemView.findViewById(R.id.txt_post_more_actions)

        init {
            itemView.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_ITEM, mPostType, it)
            }

            mTxtPostSubreddit.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SUBREDDIT, mPostType, it)
            }

            mTxtPostUser.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.THING_AUTHOR, mPostType, it)
            }

            mImgBtnPostVoteUp.setOnClickListener {
                if (!FoxToolkit.isAuthorized(mTokenDao, mTokenRepository))
                    FoxToolkit.promptLogIn(mainActivity)
                else
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        val item = getItem(bindingAdapterPosition)
                        if (item != null) {
                            FoxToolkit.upVoteColor(item.likes, mImgBtnPostVoteUp, mImgBtnPostVoteDown,
                                mTxtPostScore, mainActivity, mTxtPostShare.currentTextColor)
                            onClick(bindingAdapterPosition, Constants.THING_VOTE_UP, mPostType, it)
                        }
                    }
            }

            mImgBtnPostVoteDown.setOnClickListener {
                if (!FoxToolkit.isAuthorized(mTokenDao, mTokenRepository))
                    FoxToolkit.promptLogIn(mainActivity)
                else
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        val item = getItem(bindingAdapterPosition)
                        if (item != null) {
                            FoxToolkit.downVoteColor(item.likes, mImgBtnPostVoteUp, mImgBtnPostVoteDown,
                                mTxtPostScore, mainActivity, mTxtPostShare.currentTextColor)
                            onClick(bindingAdapterPosition, Constants.THING_VOTE_DOWN, mPostType, it)
                        }
                    }
            }

            mTxtPostNumComments.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_COMMENTS_NUM, mPostType, it)
            }

            mTxtPostShare.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SHARE, mPostType, it)
            }

            mTxtPostMoreActions.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.THING_MORE_ACTIONS, mPostType, it)
            }
        }

        open fun onBind(post: Data) {
            val user = "u/" + post.author
            val subreddit = "r/" + post.subreddit

            mTxtPostSubreddit.text = subreddit
            mTxtPostUser.text = user
            mTxtTimePosted.text = DateUtils.getRelativeTimeSpanString(post.createdUtc * 1000).toString()
            mTxtPostTitle.text = StringEscapeUtils.unescapeXml(post.title)
            mTxtPostScore.text = FoxToolkit.formatValue(post.score.toDouble())
            mTxtPostNumComments.text = FoxToolkit.formatValue(post.numComments.toDouble())

            FoxToolkit.setLikedStatusOnButtons(post.likes, mImgBtnPostVoteUp, mImgBtnPostVoteDown,
                mTxtPostScore, mainActivity, mTxtPostShare.currentTextColor)

            if (post.isOver18 != null && post.isOver18 == true)
                txtIsOver18.visibility = View.VISIBLE
            else
                txtIsOver18.visibility = View.GONE

            if (post.spoiler != null && post.spoiler == true)
                txtIsSpoiler.visibility = View.VISIBLE
            else
                txtIsSpoiler.visibility = View.GONE

            if (!post.linkFlairText.isNullOrEmpty())
                FoxToolkit.makeFlair(post.linkFlairType, post.linkFlairRichtext, post.linkFlairText,
                    post.linkFlairTextColor, post.linkFlairBackgroundColor, customTxtPostFlair)
            else
                customTxtPostFlair.visibility = View.GONE
        }
    }

    inner class PostSelfViewHolder(itemView: View, mPostType: Int) : AbstractPostViewHolder(itemView, mPostType)

    inner class PostImageViewHolder(itemView: View, private val mPostType: Int) : AbstractPostViewHolder(itemView, mPostType) {
        private val mImgPostThumbnail: ImageView = itemView.findViewById(R.id.img_post_thumbnail)

        init {
            mImgPostThumbnail.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_THUMBNAIL, mPostType, it)
            }
        }

        override fun onBind(post: Data) {
            super.onBind(post)
            if ((post.spoiler != null && post.spoiler == true) || (post.isOver18 != null && post.isOver18 == true))
                Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.placeholder_ic_baseline_photo_size_select_actual_80).apply(bitmapTransform(BlurTransformation(25, 3))).into(mImgPostThumbnail)
            else
                Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.placeholder_ic_baseline_photo_size_select_actual_80).into(mImgPostThumbnail)
        }
    }

    inner class PostLinkViewHolder(itemView: View, private val mPostType: Int) : AbstractPostViewHolder(itemView, mPostType) {
        private val mFlThumbnail: FrameLayout = itemView.findViewById(R.id.fl_thumbnail)
        private val mImgPostThumbnail: ImageView = itemView.findViewById(R.id.img_post_thumbnail)
        private val mTxtPostDomain: TextView = itemView.findViewById(R.id.txt_post_domain)

        init {
            mFlThumbnail.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_THUMBNAIL, mPostType, it)
            }
        }

        override fun onBind(post: Data) {
            super.onBind(post)
            if ((post.spoiler != null && post.spoiler == true) || (post.isOver18 != null && post.isOver18 == true))
                Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.placeholder_ic_baseline_link_80).apply(bitmapTransform(BlurTransformation(25, 3))).into(mImgPostThumbnail)
            else
                Glide.with(itemView).load(post.thumbnail).placeholder(R.drawable.placeholder_ic_baseline_link_80).into(mImgPostThumbnail)
            mTxtPostDomain.text = post.domain
            mTxtPostDomain.tag = post.urlOverriddenByDest
        }
    }

    inner class PostVideoViewHolder(itemView: View, private val mPostType: Int) : AbstractPostViewHolder(itemView, mPostType) {
        private val mFlThumbnail: FrameLayout = itemView.findViewById(R.id.fl_thumbnail)
        private val mImgPostThumbnail: ImageView = itemView.findViewById(R.id.img_post_thumbnail)

        init {
            mFlThumbnail.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_THUMBNAIL, mPostType, it)
            }
        }

        override fun onBind(post: Data) {
            super.onBind(post)
            if ((post.spoiler != null && post.spoiler == true) || (post.isOver18 != null && post.isOver18 == true))
                Glide.with(itemView).load(post.thumbnail).apply(bitmapTransform(BlurTransformation(25, 3))).into(mImgPostThumbnail)
            else
                Glide.with(itemView).load(post.thumbnail).into(mImgPostThumbnail)
        }
    }

    inner class PostPollViewHolder(itemView: View, private val mPostType: Int) : AbstractPostViewHolder(itemView, mPostType) {
        private val mBtnPostVoteNow: ImageButton = itemView.findViewById(R.id.btn_post_vote_now)
        private val mTxtPostVoteNum: TextView = itemView.findViewById(R.id.txt_post_vote_num)
        private val mTxtPostVoteTimeLeft: TextView = itemView.findViewById(R.id.txt_post_vote_time_left)

        init {
            mBtnPostVoteNow.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_VOTE_NOW, mPostType, it)
            }
        }

        override fun onBind(post: Data) {
            super.onBind(post)
            val votes = FoxToolkit.formatValue(post.pollData!!.totalVoteCount.toDouble()) + " Votes"
            val endsAt = FoxToolkit.getPollEndingDate(post.pollData.votingEndTimestamp)
            mTxtPostVoteNum.text = votes
            mTxtPostVoteTimeLeft.text = endsAt
        }
    }

    // We know naming it PostComment instead of Comment is stupid, don't judge hey :p
    inner class PostCommentViewHolder(itemView: View, private val mPostType: Int, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val mTxtPostUser: TextView = itemView.findViewById(R.id.txt_post_user)
        private val mTxtTimePosted: TextView = itemView.findViewById(R.id.txt_time_posted)
        private val mTxtPostScore: TextView = itemView.findViewById(R.id.txt_post_score)
        private val mTxtPostTitle: TextView = itemView.findViewById(R.id.txt_post_title)
        private val mTxtPostSubreddit: TextView = itemView.findViewById(R.id.txt_post_subreddit)
        private val mTxtComment: TextView = itemView.findViewById(R.id.txt_comment)

        init {
            itemView.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_ITEM, mPostType, it)
            }
            mTxtPostUser.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.THING_AUTHOR, mPostType, it)
            }
            mTxtPostSubreddit.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_SUBREDDIT, mPostType, it)
            }
            mTxtComment.setOnClickListener {
                onClick(bindingAdapterPosition, Constants.POST_ITEM, mPostType, it)
            }
        }

        fun onBind(comment: Data) {
            val markwon = Markwon.builder(context)
                .usePlugin(TablePlugin.create(context))
                .usePlugin(LinkifyPlugin.create())
                .build()
            mTxtPostUser.text = comment.author
            mTxtTimePosted.text = DateUtils.getRelativeTimeSpanString(comment.createdUtc * 1000).toString()
            mTxtPostScore.text = comment.score.toString().trim()
            mTxtPostTitle.text = StringEscapeUtils.unescapeXml(comment.title)
            mTxtPostSubreddit.text = comment.subreddit.toString()
            markwon.setMarkdown(mTxtComment, StringEscapeUtils.unescapeXml(comment.body))
        }
    }

    fun onClick(position: Int, clicked: String, postType: Int, view: View) {
        if (position != RecyclerView.NO_POSITION) {    //For index -1. (Ex. animation and item deleted and its position is -1)
            val item = getItem(position)
            if (item != null) {                          //Maybe getItem return null
                listener.onItemClick(item, clicked, postType, view)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: Data, clicked: String, postType: Int, view: View)
    }
}
