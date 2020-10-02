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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.OptionsItem;
import io.github.gusandrianos.foxforreddit.data.models.Post;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<Post> mPosts;

    @Override
    public int getItemViewType(int position) {
        if (mPosts.get(position).getPost_hint() == null) {
//            if(mPosts.get(position).getPollData()!=null){   //IF it is poll THEN must have poll data
//                return 4;
//            }
//
//            if(mPosts.get(position).getMedia()!=null){      //IF it's not the above THEN: IF it is rich/hosted:video THEN must have Media
//                return 3;
//            }
//
//            if(mPosts.get(position).getUrl().contains("https://i.")){ //IF it's nothing from the above THEN: IF it is image THEN contains https://i. (not sure)
//                return 1;
//            }
//
//            if(mPosts.get(position).getDomain().contains("self.")){ //IF it's nothing from the above THEN: IF it is self THEN contains domain with self. (not sure)
//                return 0;
//            }
//
//            return 2; //IF it's nothing from the above THEN choose link
            return 4;
        }

        if (mPosts.get(position).getPost_hint().contains("self")) {
            return 0;
        }

        if (mPosts.get(position).getPost_hint().contains("image")) {
            return 1;
        }

        if (mPosts.get(position).getPost_hint().contains("link")) {
            return 2;
        }

        if (mPosts.get(position).getPost_hint().contains("video")) {
            return 3;
        }

        if (mPosts.get(position).getPost_hint().contains("poll")) {
            return 4;
        }

        return 9;  //Not created yet ViewHolder (aViewHolder) returns if not null, otherwise will be returned first If statement
    }

    public PostRecyclerViewAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view;

        Log.d("VIEWTIPE", String.valueOf(viewType));
        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.post_self_layout, parent, false);
            return new PostSelfViewHolder(view);
        }

        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.post_image_layout, parent, false);
            return new PostImageViewHolder(view);
        }

        if (viewType == 2) {
            view = layoutInflater.inflate(R.layout.post_link_layout, parent, false);
            return new PostLinkViewHolder(view);
        }

        if (viewType == 3) {
            view = layoutInflater.inflate(R.layout.post_video_layout, parent, false);
            return new PostVideoViewHolder(view);
        }

        if (viewType == 4) {
            view = layoutInflater.inflate(R.layout.post_poll_layout, parent, false);
            return new PostPollViewHolder(view);
        }

        view = layoutInflater.inflate(R.layout.post_self_layout, parent, false);
        return new PostSelfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (mPosts.get(position).getPost_hint() == null) {
            if (mPosts.get(position).getPollData() != null) {   //IF it is poll THEN must have poll data
                PostPollViewHolder postPollViewHolder = (PostPollViewHolder) holder;
                postPollViewHolder.onBind(mPosts.get(position));
            }

//            if(mPosts.get(position).getMedia()!=null){      //IF it's not the above THEN: IF it is rich/hosted:video THEN must have Media
//                PostVideoViewHolder postVideoViewHolder = (PostVideoViewHolder) holder;
//                postVideoViewHolder.onBind(mPosts.get(position));
//            }
//
//            if(mPosts.get(position).getUrl().contains("https://i.")){ //IF it's nothing from the above THEN: IF it is image THEN contains https://i. (not sure)
//                PostImageViewHolder postImageViewHolder = (PostImageViewHolder) holder;
//                postImageViewHolder.onBind(mPosts.get(position));
//            }
//
//            if(mPosts.get(position).getDomain().contains("self.")){ //IF it's nothing from the above THEN: IF it is self THEN contains domain with self. (not sure)
//                PostSelfViewHolder postSelfViewHolder = (PostSelfViewHolder) holder;
//                postSelfViewHolder.onBind(mPosts.get(position));
//            }
//
//            PostLinkViewHolder postLinkViewHolder = (PostLinkViewHolder) holder;
//            postLinkViewHolder.onBind(mPosts.get(position));        //IF it's nothing from the above THEN choose link

        } else if (mPosts.get(position).getPost_hint().contains("self")) {
            PostSelfViewHolder postSelfViewHolder = (PostSelfViewHolder) holder;
            postSelfViewHolder.onBind(mPosts.get(position));
        } else if (mPosts.get(position).getPost_hint().contains("image")) {
            PostImageViewHolder postImageViewHolder = (PostImageViewHolder) holder;
            postImageViewHolder.onBind(mPosts.get(position));
        } else if (mPosts.get(position).getPost_hint().contains("link")) {
            PostLinkViewHolder postLinkViewHolder = (PostLinkViewHolder) holder;
            postLinkViewHolder.onBind(mPosts.get(position));
        } else if (mPosts.get(position).getPost_hint().contains("video")) {
            PostVideoViewHolder postVideoViewHolder = (PostVideoViewHolder) holder;
            postVideoViewHolder.onBind(mPosts.get(position));
        } else {
            PostPollViewHolder postPollViewHolder = (PostPollViewHolder) holder;
            postPollViewHolder.onBind(mPosts.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
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
        TextView mTxt_post_thumbnail;

        public PostVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxt_post_thumbnail = itemView.findViewById(R.id.txt_post_thumbnail);
        }

        @Override
        public void onBind(Post post) {
            super.onBind(post);

            Glide.with(parent.getContext())
                    .load(post.getThumbnail())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource,
                                                    @Nullable Transition<? super Drawable> transition) {

                            mTxt_post_thumbnail.setBackground(resource);

                        }
                    });

        }
    }

    //ToDo poll

    public static class PostPollViewHolder extends AbstractPostViewHolder {

        RadioGroup mRg_post_votes;
        Button mBtn_post_vote;
        TextView mTxt_post_vote_num, mTxt_post_vote_time_left;

        public PostPollViewHolder(@NonNull View itemView) {
            super(itemView);
            mRg_post_votes = itemView.findViewById(R.id.rg_post_votes);
            mBtn_post_vote = itemView.findViewById(R.id.btn_post_vote);
            mTxt_post_vote_num = itemView.findViewById(R.id.txt_post_vote_num);
            mTxt_post_vote_time_left = itemView.findViewById(R.id.txt_post_vote_time_left);
        }

        @Override
        public void onBind(Post post) {
            super.onBind(post);

            for (OptionsItem optionsItem : post.getPollData().getOptions()) {
                RadioButton rdbtn = new RadioButton(itemView.getContext());
                rdbtn.setId(View.generateViewId());
                rdbtn.setText(optionsItem.getText());
                rdbtn.setTag(optionsItem.getId());
                mRg_post_votes.addView(rdbtn);
            }

            String votes = String.valueOf(post.getPollData().getTotalVoteCount()) + " Votes";
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
