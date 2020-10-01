package io.github.gusandrianos.foxforreddit.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.data.models.RedditVideoPreview;

public class RecyclerViewAdapter extends RecyclerView.Adapter {  //<RecyclerViewAdapter.mViewHolder>

    Context mContext;
    List<Post> mPosts;

    public RecyclerViewAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @Override
    public int getItemViewType(int position) {

        if (mPosts.get(position).getPost_hint() == null || mPosts.get(position).getPost_hint().contains("self")) {       //If null OR self then choose TextViewHolder
            return 0;
        }

        if (mPosts.get(position).getPost_hint().contains("image")) {
            return 1;
        }

        if (mPosts.get(position).getPost_hint().contains("link")) {
            return 2;
        }

        if (mPosts.get(position).getPost_hint().contains("hosted:video")) {
            return 3;
        }

        return 9;  //Not created yet ViewHolder (aViewHolder) returns if not null, otherwise will be returned first If statement
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view;


        //Return text layout
        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.li_text, parent, false);
            return new TextViewHolder(view);
        }

        //Return image layout
        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.li_image, parent, false);
            return new ImageViewHolder(view);
        }

        if (viewType == 2) {
            view = layoutInflater.inflate(R.layout.li_link, parent, false);
            return new LinkViewHolder(view);
        }

        if (viewType == 3) {
            view = layoutInflater.inflate(R.layout.li_video, parent, false);
            return new VideoViewHolder(view);
        }

        //Return list_item layout
        view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (mPosts.get(position).getPost_hint() == null || mPosts.get(position).getPost_hint().contains("self")) {
            //Bind TextViewHolder (With or without body text)
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            textViewHolder.onBind(mPosts.get(position));

//            String user = "Posted by u/" + mPosts.get(position).getAuthor()
//                    + " - "
//                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(mPosts.get(position).getCreatedUtc() * 1000));
//            String subreddit = "r/" + mPosts.get(position).getSubreddit();
//
//            textViewHolder.mTxt_subreddit.setText(subreddit);
//            textViewHolder.mTxt_user.setText(user);
//            textViewHolder.mTxt_score.setText(formatValue(mPosts.get(position).getScore()));
//            textViewHolder.mBtn_numComments.setText(formatValue(mPosts.get(position).getNumComments()));
//            textViewHolder.mTxt_postTitle.setText(mPosts.get(position).getTitle());
//
//            textViewHolder.mTxt_postText.setText(mPosts.get(position).getSelftext());

        } else if (mPosts.get(position).getPost_hint().contains("image")) {
            //Bind ImageViewHolder
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            imageViewHolder.onBind(mPosts.get(position));
//            String user = "Posted by u/" + mPosts.get(position).getAuthor()
//                    + " - "
//                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(mPosts.get(position).getCreatedUtc() * 1000));
//            String subreddit = "r/" + mPosts.get(position).getSubreddit();
//
//            imageViewHolder.mTxt_subreddit.setText(subreddit);
//            imageViewHolder.mTxt_user.setText(user);
//            imageViewHolder.mTxt_score.setText(formatValue(mPosts.get(position).getScore()));
//            imageViewHolder.mBtn_numComments.setText(formatValue(mPosts.get(position).getNumComments()));
//            imageViewHolder.mTxt_postTitle.setText(mPosts.get(position).getTitle());
//
//            Glide.with(imageViewHolder.itemView).load(mPosts.get(position).getUrlOverriddenByDest()).placeholder(R.drawable.ic_launcher_background).into(imageViewHolder.mImg_postImage);


        } else if (mPosts.get(position).getPost_hint().contains("link")) {
            //Bind LinkViewHolder
            LinkViewHolder linkViewHolder = (LinkViewHolder) holder;
            linkViewHolder.onBind(mPosts.get(position));
//            String user = "Posted by u/" + mPosts.get(position).getAuthor()
//                    + " - "
//                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(mPosts.get(position).getCreatedUtc() * 1000));
//            String subreddit = "r/" + mPosts.get(position).getSubreddit();
//
//            linkViewHolder.mTxt_subreddit.setText(subreddit);
//            linkViewHolder.mTxt_user.setText(user);
//            linkViewHolder.mTxt_score.setText(formatValue(mPosts.get(position).getScore()));
//            linkViewHolder.mBtn_numComments.setText(formatValue(mPosts.get(position).getNumComments()));
//            linkViewHolder.mTxt_postTitle.setText(mPosts.get(position).getTitle());
//
//
//            Glide.with(linkViewHolder.itemView).load(mPosts.get(position).getThumbnail()).placeholder(R.drawable.ic_launcher_foreground).into(linkViewHolder.mImgbtn_postImage);
//            linkViewHolder.mImgbtn_postImage.setTag(mPosts.get(position).getUrlOverriddenByDest());
//
//            linkViewHolder.mTxt_postLink.setText(mPosts.get(position).getDomain());

        } else if (mPosts.get(position).getPost_hint().contains("hosted:video")) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            videoViewHolder.onBind(mPosts.get(position));

        } else {
            //Bind simpleViewHolder (for every holder that has not been created)
            SimpleViewHolder simpleViewHolder = (SimpleViewHolder) holder;

            String user = "Posted by u/" + mPosts.get(position).getAuthor()
                    + " - "
                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(mPosts.get(position).getCreatedUtc() * 1000));
            String subreddit = "THIS IS SIMPLE";

            simpleViewHolder.mTxt_subreddit.setText(subreddit);
            simpleViewHolder.mTxt_user.setText(user);
            simpleViewHolder.mTxt_score.setText(formatValue(mPosts.get(position).getScore()));
            simpleViewHolder.mBtn_numComments.setText(formatValue(mPosts.get(position).getNumComments()));


        }
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    public static class TextViewHolder extends AbstractViewHolder {
        TextView mTxt_postTitle;
        TextView mTxt_postText;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;

            mTxt_subreddit = itemView.findViewById(R.id.txt_subreddit);
            mTxt_user = itemView.findViewById(R.id.txt_user);
            mBtn_numComments = itemView.findViewById((R.id.btn_numComments));
            mTxt_score = itemView.findViewById(R.id.txt_score);

            mTxt_postTitle = itemView.findViewById(R.id.txt_postTitle);
            mTxt_postText = itemView.findViewById(R.id.txt_postText);
        }

        public void onBind(Post post) {
            String user = "Posted by u/" + post.getAuthor()
                    + " - "
                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(post.getCreatedUtc() * 1000));
            String subreddit = "r/" + post.getSubreddit();

            mTxt_subreddit.setText(subreddit);
            mTxt_user.setText(user);
            mTxt_score.setText(formatValue(post.getScore()));
            mBtn_numComments.setText(formatValue(post.getNumComments()));
            mTxt_postTitle.setText(post.getTitle());

            mTxt_postText.setText(post.getSelftext());
        }
    }

    public static class ImageViewHolder extends AbstractViewHolder {

        TextView mTxt_postTitle;
        ImageView mImg_postImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;

            mTxt_subreddit = itemView.findViewById(R.id.txt_subreddit);
            mTxt_user = itemView.findViewById(R.id.txt_user);
            mBtn_numComments = itemView.findViewById((R.id.btn_numComments));
            mTxt_score = itemView.findViewById(R.id.txt_score);

            mTxt_postTitle = itemView.findViewById(R.id.txt_postTitle);
            mImg_postImage = itemView.findViewById(R.id.img_postImage);
        }

        public void onBind(Post post) {
            String user = "Posted by u/" + post.getAuthor()
                    + " - "
                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(post.getCreatedUtc() * 1000));
            String subreddit = "r/" + post.getSubreddit();

            mTxt_subreddit.setText(subreddit);
            mTxt_user.setText(user);
            mTxt_score.setText(formatValue(post.getScore()));
            mBtn_numComments.setText(formatValue(post.getNumComments()));
            mTxt_postTitle.setText(post.getTitle());

            Glide.with(parent.getContext()).load(post.getUrlOverriddenByDest()).placeholder(R.drawable.ic_launcher_background).into(mImg_postImage);
        }
    }

    public static class LinkViewHolder extends AbstractViewHolder {

        ImageButton mImgbtn_postImage;
        TextView mTxt_postLink;

        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;

            mTxt_subreddit = itemView.findViewById(R.id.txt_subreddit);
            mTxt_user = itemView.findViewById(R.id.txt_user);
            mBtn_numComments = itemView.findViewById((R.id.btn_numComments));
            mTxt_score = itemView.findViewById(R.id.txt_score);
            mTxt_postTitle = itemView.findViewById(R.id.txt_postTitle);

            mImgbtn_postImage = itemView.findViewById(R.id.imgbtn_postImage);
            mTxt_postLink = itemView.findViewById(R.id.txt_postLink);
        }

        public void onBind(Post post) {
            String user = "Posted by u/" + post.getAuthor()
                    + " - "
                    + (android.text.format.DateUtils.getRelativeTimeSpanString(post.getCreatedUtc() * 1000));
            String subreddit = "r/" + post.getSubreddit();

            mTxt_subreddit.setText(subreddit);
            mTxt_user.setText(user);
            mTxt_score.setText(formatValue(post.getScore()));
            mBtn_numComments.setText(formatValue(post.getNumComments()));
            mTxt_postTitle.setText(post.getTitle());

            Glide.with(parent.getContext()).load(post.getThumbnail()).placeholder(R.drawable.ic_launcher_foreground).into(mImgbtn_postImage);
            mImgbtn_postImage.setTag(post.getUrlOverriddenByDest());

            mTxt_postLink.setText(post.getDomain());
        }


    }

    public static class VideoViewHolder extends AbstractViewHolder {

        PlayerView mEp_postVideo;
        SimpleExoPlayer  simpleExoPlayer;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;

            mTxt_subreddit = itemView.findViewById(R.id.txt_subreddit);
            mTxt_user = itemView.findViewById(R.id.txt_user);
            mBtn_numComments = itemView.findViewById((R.id.btn_numComments));
            mTxt_score = itemView.findViewById(R.id.txt_score);
            mTxt_postTitle = itemView.findViewById(R.id.txt_postTitle);

            mEp_postVideo = itemView.findViewById(R.id.ep_postVideo);


        }

        public void onBind(Post post/*, RequestManager requestManager*/) {
            //this.requestManager = requestManager;

            String user = "Posted by u/" + post.getAuthor()
                    + " - "
                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(post.getCreatedUtc() * 1000));
            String subreddit = "r/" + post.getSubreddit();

            mTxt_subreddit.setText(subreddit);
            mTxt_user.setText(user);
            mTxt_score.setText(formatValue(post.getScore()));
            mBtn_numComments.setText(formatValue(post.getNumComments()));
            mTxt_postTitle.setText(post.getTitle());

            simpleExoPlayer = new SimpleExoPlayer.Builder(itemView.getContext()).build();


        }
    }

    public static class SimpleViewHolder extends AbstractViewHolder {

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;
            mTxt_subreddit = itemView.findViewById(R.id.txt_subreddit);
            mTxt_user = itemView.findViewById(R.id.txt_user);
            mBtn_numComments = itemView.findViewById((R.id.btn_numComments));
            mTxt_score = itemView.findViewById(R.id.txt_score);
            mTxt_postTitle = itemView.findViewById(R.id.txt_postTitle);
        }
    }


    public abstract static class AbstractViewHolder extends RecyclerView.ViewHolder {

        TextView mTxt_subreddit;
        TextView mTxt_user;
        TextView mTxt_score;
        Button mBtn_numComments;
        TextView mTxt_postTitle;
        View parent;

        public AbstractViewHolder(@NonNull View itemView) {
            super(itemView);
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


//        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    @NonNull
//    @Override
//    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view;
//        view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
//        mViewHolder viewHolder = new mViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
//            String user = "Posted by u/" + mPosts.get(position).getAuthor()
//                    + " - "
//                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(mPosts.get(position).getCreatedUtc()*1000));
//            String subreddit = "r/" + mPosts.get(position).getSubreddit();
//
//            holder.mTxt_subreddit.setText(subreddit);
//            holder.mTxt_user.setText(user);
//            holder.mTxt_score.setText(formatValue(mPosts.get(position).getScore()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mPosts.size();
//    }
//
//    public static class mViewHolder extends RecyclerView.ViewHolder{
//
//        private TextView mTxt_score;
//        private TextView mTxt_subreddit;
//        private TextView mTxt_user;
//
//        public mViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            mTxt_subreddit = itemView.findViewById(R.id.txt_subreddit);
//            mTxt_user = itemView.findViewById(R.id.txt_user);
//            mTxt_score = itemView.findViewById(R.id.txt_score);
//
//        }
//}


}
