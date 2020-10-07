package io.github.gusandrianos.foxforreddit.ui

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Post


import java.text.DecimalFormat
import java.text.NumberFormat

private var LOADING = 0
private var SELF = 1
private var LINK = 2
private var IMAGE = 3
private var VIDEO = 4
private var POLL = 5

class PostAdapter : PagingDataAdapter<Post, RecyclerView.ViewHolder>(POST_COMPARATOR) {
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

//            return 0;
//            IF it's nothing from the above THEN choose link
//            return 4;
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
        else 9
        //Not created yet ViewHolder (aViewHolder) returns if not null, otherwise will be returned first If statement
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
//            LOADING -> {
//                val viewLoading: View = layoutInflater.inflate(R.layout.post_progress_layout, parent, false)
//                return PostLoadingViewHolder(viewLoading)
//            }
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
//            LOADING -> {
//                val postLoadingViewHolder = holder as PostLoadingViewHolder
//                postLoadingViewHolder.mProgressBar.visibility = View.VISIBLE
//            }
        }
    }

    abstract class AbstractPostViewHolder(var parent: View) : RecyclerView.ViewHolder(parent) {
        var mImg_post_subreddit: ImageView = itemView.findViewById(R.id.img_post_subreddit)
        var mTxt_post_subreddit: TextView = itemView.findViewById(R.id.txt_post_subreddit)
        var mTxt_post_user: TextView = itemView.findViewById(R.id.txt_post_user)
        var mTxt_post_title: TextView = itemView.findViewById(R.id.txt_post_title)
        var mTxt_post_score: TextView = itemView.findViewById(R.id.txt_post_score)
        var mImgbtn_post_vote_up: ImageButton = itemView.findViewById(R.id.imgbtn_post_vote_up)
        var mImgbtn_post_vote_down: ImageButton = itemView.findViewById(R.id.imgbtn_post_vote_down)
        var Btn_post_num_comments: Button = itemView.findViewById(R.id.btn_post_num_comments)
        var mbtn_post_share: Button = itemView.findViewById(R.id.btn_post_share)
        open fun onBind(post: Post) {
            val user = ("Posted by u/" + post.author
                    + " - "
                    + DateUtils.getRelativeTimeSpanString(post.createdUtc * 1000) as String)
            val subreddit = "r/" + post.subreddit

            //Glide.with(parent.getContext()).load().placeholder(R.drawable.ic_launcher_background).into(mImg_post_subreddit);  //MUST GET SUBREDDIT ICON
            mTxt_post_subreddit.text = subreddit
            mTxt_post_user.text = user
            mTxt_post_title.text = post.title
            mTxt_post_score.text = formatValue(post.score.toDouble())
            Btn_post_num_comments.text = formatValue(post.numComments.toDouble())
        }

    }

    class PostSelfViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {
        override fun onBind(post: Post) {
            super.onBind(post)
        }
    }

    class PostImageViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {
        var mImg_post_thumbnail: ImageView
        override fun onBind(post: Post) {
            super.onBind(post)
            //Todo if it is nfsw
            Glide.with(parent).load(post.thumbnail).placeholder(R.drawable.ic_launcher_background).into(mImg_post_thumbnail)
        }

        init {
            mImg_post_thumbnail = itemView.findViewById(R.id.img_post_thumbnail)
        }
    }

    class PostLinkViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {
        var mImg_post_thumbnail: ImageView
        var mTxt_post_domain: TextView
        override fun onBind(post: Post) {
            super.onBind(post)
            Glide.with(parent).load(post.thumbnail).placeholder(R.drawable.ic_launcher_background).into(mImg_post_thumbnail)
            mTxt_post_domain.text = post.domain
            mTxt_post_domain.tag = post.urlOverriddenByDest
        }

        init {
            mImg_post_thumbnail = itemView.findViewById(R.id.img_post_thumbnail)
            mTxt_post_domain = itemView.findViewById(R.id.txt_post_domain)
        }
    }

    class PostVideoViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {
        var mImg_post_thumbnail: ImageView
        override fun onBind(post: Post) {
            super.onBind(post)
            Glide.with(parent).load(post.thumbnail).placeholder(R.drawable.ic_launcher_background).into(mImg_post_thumbnail)
        }

        init {
            mImg_post_thumbnail = itemView.findViewById(R.id.img_post_thumbnail)
        }
    }

    //ToDo poll
    class PostPollViewHolder(itemView: View) : AbstractPostViewHolder(itemView) {
        var mBtn_post_vote_now: Button = itemView.findViewById(R.id.btn_post_vote_now)
        var mTxt_post_vote_num: TextView = itemView.findViewById(R.id.txt_post_vote_num)
        var mTxt_post_vote_time_left: TextView = itemView.findViewById(R.id.txt_post_vote_time_left)
        override fun onBind(post: Post) {
            super.onBind(post)
            val votes = post.pollData.totalVoteCount.toString() + " Votes"
            mTxt_post_vote_num.text = votes
            val ends_at = "Ends at " + getDate(post.pollData.votingEndTimestamp)
            mTxt_post_vote_time_left.text = ends_at
        }
    }
}

//ToDo fix hours
fun getDate(timestamp: Long): String {
    return DateUtils.getRelativeTimeSpanString(timestamp * 1000).toString();
}

fun formatValue(value: Double): String {
    var value = value
    if (value == 0.0) {
        return 0.toString()
    }
    val power: Int
    val suffix = " kmbt"
    var formattedNumber = ""
    val formatter: NumberFormat = DecimalFormat("#,###.#")
    power = StrictMath.log10(value).toInt()
    value = value / Math.pow(10.0, power / 3 * 3.toDouble())
    formattedNumber = formatter.format(value)
    formattedNumber = formattedNumber + suffix[power / 3]
    return if (formattedNumber.length > 4) formattedNumber.replace("\\.[0-9]+".toRegex(), "") else formattedNumber
}