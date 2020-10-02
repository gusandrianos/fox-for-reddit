package io.github.gusandrianos.foxforreddit.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Post;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<Post> mPosts;

    public PostRecyclerViewAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostViewHolder postViewHolder = (PostViewHolder) holder;
        postViewHolder.onBind(mPosts.get(position));

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View parent;
        ImageView mImg_post_subreddit;
        TextView mTxt_post_subreddit, mTxt_post_user, mTxt_post_title, mTxt_post_score, mTxt_post_thumbnail;
        ImageButton mImgbtn_post_vote_up, mImgbtn_post_vote_down;
        Button Btn_post_num_comments, mbtn_post_share;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;
            mImg_post_subreddit = itemView.findViewById(R.id.img_post_subreddit);
            mTxt_post_subreddit = itemView.findViewById(R.id.txt_post_subreddit);
            mTxt_post_user = itemView.findViewById(R.id.txt_post_user);
            mTxt_post_title = itemView.findViewById(R.id.txt_post_title);
//            mImg_post_thumbnail = itemView.findViewById(R.id.img_post_thumbnail);
//            mTxt_post_thumbnail_descr = itemView.findViewById(R.id.txt_post_thumbnail_descr);
            mTxt_post_thumbnail = itemView.findViewById(R.id.txt_post_thumbnail);
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


            if(post.getPost_hint()!=null && !post.getPost_hint().contains(("self"))) {
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

            if(post.getPost_hint()!=null && post.getPost_hint().contains("link")){
                SpannableString str = new SpannableString(post.getDomain());
                str.setSpan(new BackgroundColorSpan(Color.rgb(128,128,128)), 0, str.length(), 0);
                mTxt_post_thumbnail.setText(str);
            }else if(post.getPost_hint()!=null && post.getPost_hint().contains("video")){
                mTxt_post_thumbnail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_round_play_circle_outline_24);
            }

            mTxt_post_score.setText(formatValue(post.getScore()));
            //mImgbtn_post_vote_up
            //mImgbtn_post_vote_down
            Btn_post_num_comments.setText(formatValue(post.getNumComments()));

        }
    }

    public static String formatValue(double value) {
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
