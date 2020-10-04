package io.github.gusandrianos.foxforreddit.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.security.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.OptionsItem;
import io.github.gusandrianos.foxforreddit.data.models.Post;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    private List<Post> mPosts;
    private boolean isLoadingAdded = false;

    private static final int LOADING = 0;
    private static final int SELF = 1;
    private static final int LINK = 2;
    private static final int IMAGE = 3;
    private static final int VIDEO = 4;
    private static final int POLL = 5;

    public PostRecyclerViewAdapter(Context context) {
        mContext = context;
        mPosts = new LinkedList<>();
    }

    public void setPosts(List<Post> posts) {
        mPosts = posts;
    }

    @Override
    public int getItemViewType(int position) {
        if (mPosts.get(position).getPost_hint() == null) {
            if (mPosts.get(position).getPollData() != null) {   //IF it is poll THEN must have poll data
                return POLL;
            }

            if (mPosts.get(position).getMedia() != null) {      //IF it's not the above THEN: IF it is rich/hosted:video THEN must have Media
                return VIDEO;
            }
//
            if (mPosts.get(position).getUrl().contains("https://i.")) { //IF it's nothing from the above THEN: IF it is image THEN contains https://i. (not sure)
                return IMAGE;
            }

            if (mPosts.get(position).getDomain().contains("self.")) { //IF it's nothing from the above THEN: IF it is self THEN contains domain with self. (not sure)
                return SELF;
            }
//
//            return 0;
            return LINK; //IF it's nothing from the above THEN choose link
//            return 4;
        }

        if (mPosts.get(position).getPost_hint().contains("self")) {
            return SELF;
        }

        if (mPosts.get(position).getPost_hint().contains("image")) {
            return IMAGE;
        }

        if (mPosts.get(position).getPost_hint().contains("link")) {
            return LINK;
        }

        if (mPosts.get(position).getPost_hint().contains("video")) {
            return VIDEO;
        }

        if (mPosts.get(position).getPost_hint().contains("poll")) {
            return POLL;
        }

        return 9;  //Not created yet ViewHolder (aViewHolder) returns if not null, otherwise will be returned first If statement
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view;

        switch (viewType) {
            case SELF:
                view = layoutInflater.inflate(R.layout.post_self_layout, parent, false);
                return new PostSelfViewHolder(view);
            case IMAGE:
                view = layoutInflater.inflate(R.layout.post_image_layout, parent, false);
                return new PostImageViewHolder(view);
            case LINK:
                view = layoutInflater.inflate(R.layout.post_link_layout, parent, false);
                return new PostLinkViewHolder(view);
            case VIDEO:
                view = layoutInflater.inflate(R.layout.post_video_layout, parent, false);
                return new PostVideoViewHolder(view);
            case POLL:
                view = layoutInflater.inflate(R.layout.post_poll_layout, parent, false);
                return new PostPollViewHolder(view);
            case LOADING:
                View viewLoading = layoutInflater.inflate(R.layout.post_progress_layout, parent, false);
                return new PostLoadingViewHolder(viewLoading);
        }

//        if (viewType == SELF) {
//            view = layoutInflater.inflate(R.layout.post_self_layout, parent, false);
//            return new PostSelfViewHolder(view);
//        }
//
//        if (viewType == IMAGE) {
//            view = layoutInflater.inflate(R.layout.post_image_layout, parent, false);
//            return new PostImageViewHolder(view);
//        }
//
//        if (viewType == LINK) {
//            view = layoutInflater.inflate(R.layout.post_link_layout, parent, false);
//            return new PostLinkViewHolder(view);
//        }
//
//        if (viewType == VIDEO) {
//            view = layoutInflater.inflate(R.layout.post_video_layout, parent, false);
//            return new PostVideoViewHolder(view);
//        }
//
//        if (viewType == POLL) {
//            view = layoutInflater.inflate(R.layout.post_poll_layout, parent, false);
//            return new PostPollViewHolder(view);
//        }

        view = layoutInflater.inflate(R.layout.post_self_layout, parent, false); //Just in case...
        return new PostSelfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        switch (getItemViewType(position)) {
            case SELF:
                PostSelfViewHolder postSelfViewHolder = (PostSelfViewHolder) holder;
                postSelfViewHolder.onBind(mPosts.get(position));
                break;
            case IMAGE:
                PostImageViewHolder postImageViewHolder = (PostImageViewHolder) holder;
                postImageViewHolder.onBind(mPosts.get(position));
                break;
            case LINK:
                PostLinkViewHolder postLinkViewHolder = (PostLinkViewHolder) holder;
                postLinkViewHolder.onBind(mPosts.get(position));
                break;
            case VIDEO:
                PostVideoViewHolder postVideoViewHolder = (PostVideoViewHolder) holder;
                postVideoViewHolder.onBind(mPosts.get(position));
                break;
            case POLL:
                PostPollViewHolder postPollViewHolder = (PostPollViewHolder) holder;
                postPollViewHolder.onBind(mPosts.get(position));
                break;
            case LOADING:
                PostLoadingViewHolder postLoadingViewHolder = (PostLoadingViewHolder) holder;
                postLoadingViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                break;
        }

//        if (getItemViewType(position) == SELF) {
//            PostSelfViewHolder postSelfViewHolder = (PostSelfViewHolder) holder;
//            postSelfViewHolder.onBind(mPosts.get(position));
//        } else if (getItemViewType(position) == IMAGE) {
//            PostImageViewHolder postImageViewHolder = (PostImageViewHolder) holder;
//            postImageViewHolder.onBind(mPosts.get(position));
//        } else if (getItemViewType(position) == LINK) {
//            PostLinkViewHolder postLinkViewHolder = (PostLinkViewHolder) holder;
//            postLinkViewHolder.onBind(mPosts.get(position));
//        } else if (getItemViewType(position) == VIDEO) {
//            PostVideoViewHolder postVideoViewHolder = (PostVideoViewHolder) holder;
//            postVideoViewHolder.onBind(mPosts.get(position));
//        } else {
//            PostPollViewHolder postPollViewHolder = (PostPollViewHolder) holder;
//            postPollViewHolder.onBind(mPosts.get(position));
//        }


    }

    @Override
    public int getItemCount() {
        return mPosts == null ? 0 : mPosts.size();
    }



    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Post());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mPosts.size() - 1;
        Post result = getItem(position);

        if (result != null) {
            mPosts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(Post post) {
        mPosts.add(post);
        notifyItemInserted(mPosts.size() - 1);
    }

    public void addAll(List<Post> postResults) {
        for (Post result : postResults) {
            add(result);
        }
    }

    public Post getItem(int position) {
        return mPosts.get(position);
    }



    public static class PostLoadingViewHolder extends RecyclerView.ViewHolder{

        ProgressBar mProgressBar;

        public PostLoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
        }
    }

    public abstract static class AbstractPostViewHolder extends RecyclerView.ViewHolder {
        View parent;
        ImageView mImg_post_subreddit;
        TextView mTxt_post_subreddit, mTxt_post_user, mTxt_post_title, mTxt_post_score;
        ImageButton mImgbtn_post_vote_up, mImgbtn_post_vote_down;
        Button Btn_post_num_comments, mbtn_post_share;

        public AbstractPostViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;
            mImg_post_subreddit = itemView.findViewById(R.id.img_post_subreddit);
            mTxt_post_subreddit = itemView.findViewById(R.id.txt_post_subreddit);
            mTxt_post_user = itemView.findViewById(R.id.txt_post_user);
            mTxt_post_title = itemView.findViewById(R.id.txt_post_title);
            mTxt_post_score = itemView.findViewById(R.id.txt_post_score);
            mImgbtn_post_vote_up = itemView.findViewById(R.id.imgbtn_post_vote_up);
            mImgbtn_post_vote_down = itemView.findViewById(R.id.imgbtn_post_vote_down);
            Btn_post_num_comments = itemView.findViewById(R.id.btn_post_num_comments);
            mbtn_post_share = itemView.findViewById(R.id.btn_post_share);
        }

        public void onBind(Post post) {
            String user = "Posted by u/" + post.getAuthor()
                    + " - "
                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(post.getCreatedUtc() * 1000));
            String subreddit = "r/" + post.getSubreddit();

            //Glide.with(parent.getContext()).load().placeholder(R.drawable.ic_launcher_background).into(mImg_post_subreddit);  //MUST GET SUBREDDIT ICON
            mTxt_post_subreddit.setText(subreddit);
            mTxt_post_user.setText(user);
            mTxt_post_title.setText(post.getTitle());
            mTxt_post_score.setText(formatValue(post.getScore()));
            Btn_post_num_comments.setText(formatValue(post.getNumComments()));


        }

    }

    public static class PostSelfViewHolder extends AbstractPostViewHolder {
        public PostSelfViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(Post post) {
            super.onBind(post);
        }
    }

    public static class PostImageViewHolder extends AbstractPostViewHolder {

        ImageView mImg_post_thumbnail;

        public PostImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mImg_post_thumbnail = itemView.findViewById(R.id.img_post_thumbnail);
        }

        @Override
        public void onBind(Post post) {
            super.onBind(post);
            //Todo if it is nfsw
            Glide.with(parent).load(post.getThumbnail()).placeholder(R.drawable.ic_launcher_background).into(mImg_post_thumbnail);
        }
    }

    public static class PostLinkViewHolder extends AbstractPostViewHolder {

        ImageView mImg_post_thumbnail;
        TextView mTxt_post_domain;

        public PostLinkViewHolder(@NonNull View itemView) {
            super(itemView);
            mImg_post_thumbnail = itemView.findViewById(R.id.img_post_thumbnail);
            mTxt_post_domain = itemView.findViewById(R.id.txt_post_domain);
        }

        @Override
        public void onBind(Post post) {
            super.onBind(post);
            Glide.with(parent).load(post.getThumbnail()).placeholder(R.drawable.ic_launcher_background).into(mImg_post_thumbnail);
            mTxt_post_domain.setText(post.getDomain());
            mTxt_post_domain.setTag(post.getUrlOverriddenByDest());
        }
    }

    public static class PostVideoViewHolder extends AbstractPostViewHolder {
        ImageView mImg_post_thumbnail;

        public PostVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            mImg_post_thumbnail = itemView.findViewById(R.id.img_post_thumbnail);
        }

        @Override
        public void onBind(Post post) {
            super.onBind(post);
            Glide.with(parent).load(post.getThumbnail()).placeholder(R.drawable.ic_launcher_background).into(mImg_post_thumbnail);

        }
    }

    //ToDo poll

    public static class PostPollViewHolder extends AbstractPostViewHolder {

        Button mBtn_post_vote_now;
        TextView mTxt_post_vote_num, mTxt_post_vote_time_left;

        public PostPollViewHolder(@NonNull View itemView) {
            super(itemView);
            mBtn_post_vote_now = itemView.findViewById(R.id.btn_post_vote_now);
            mTxt_post_vote_num = itemView.findViewById(R.id.txt_post_vote_num);
            mTxt_post_vote_time_left = itemView.findViewById(R.id.txt_post_vote_time_left);
        }

        @Override
        public void onBind(Post post) {
            super.onBind(post);


            String votes = post.getPollData().getTotalVoteCount() + " Votes";
            mTxt_post_vote_num.setText(votes);

            String ends_at = "Ends at " + getDate(post.getPollData().getVotingEndTimestamp());
            mTxt_post_vote_time_left.setText(ends_at);

        }
    }

    //ToDo fix hours
    public static String getDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("hh:mm dd-MM", cal).toString();
    }

    public static String formatValue(double value) {
        if (value == 0) {
            return String.valueOf(0);
        }
        int power;
        String suffix = " kmbt";
        String formattedNumber = "";

        NumberFormat formatter = new DecimalFormat("#,###.#");
        power = (int) StrictMath.log10(value);
        value = value / (Math.pow(10, (power / 3) * 3));
        formattedNumber = formatter.format(value);
        formattedNumber = formattedNumber + suffix.charAt(power / 3);
        return formattedNumber.length() > 4 ? formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
    }


}
