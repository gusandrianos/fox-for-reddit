package io.github.gusandrianos.foxforreddit.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter {  //<RecyclerViewAdapter.mViewHolder>

    Context mContext;
    List<Post> mPosts;

    public RecyclerViewAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @Override
    public int getItemViewType(int position) {

        if (mPosts.get(position).getPost_hint() == null){       //If null then choose TextViewHolder (don't create body)
            return 0;
        }                                                       //So, If NOT null then choose one of the ViewHolders
        if (mPosts.get(position).getPost_hint().contains("self")) {
            return 0;
        }

        if (mPosts.get(position).getPost_hint().contains("image")) {
            return 1;
        }

        return 2;  //Not created yet ViewHolder (SimpleViewHolder) returns if not null, otherwise will be returned first If statement
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view;


        Log.d("PROBLEM", String.valueOf(viewType));
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

        //Return header layout
        view = layoutInflater.inflate(R.layout.header, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mPosts.get(position).getPost_hint() == null) {
            Log.d("POSTTYPENULL", "It is null");
            Log.d("POSTTYPENULL", mPosts.get(position).getAuthor());
            Log.d("POSTTYPENULL", mPosts.get(position).getSubreddit());
            Log.d("POSTTYPENULL", mPosts.get(position).getSelftext());

        } else {
            Log.d("POSTTYPE", mPosts.get(position).getPost_hint());
            Log.d("POSTTYPE", mPosts.get(position).getSubreddit());
        }

        if (mPosts.get(position).getPost_hint() == null) {
            //Bind TextViewHolder with no body - just title
            TextViewHolder textViewHolder = (TextViewHolder) holder;

            String user = "Posted by u/" + mPosts.get(position).getAuthor()
                    + " - "
                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(mPosts.get(position).getCreatedUtc() * 1000));
            String subreddit = "r/" + mPosts.get(position).getSubreddit();

            textViewHolder.mTxt_subreddit.setText(subreddit);
            textViewHolder.mTxt_user.setText(user);
            textViewHolder.mTxt_score.setText(String.valueOf(mPosts.get(position).getScore()));
            textViewHolder.mTxt_postTitle.setText(mPosts.get(position).getTitle());
//            textViewHolder.mTxt_postText.setText(mPosts.get(position).getSelftext()); //must be empty
        } else if (mPosts.get(position).getPost_hint() != null && mPosts.get(position).getPost_hint().contains("self")) {
            //Bind TextViewHolder
            TextViewHolder textViewHolder = (TextViewHolder) holder;

            String user = "Posted by u/" + mPosts.get(position).getAuthor()
                    + " - "
                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(mPosts.get(position).getCreatedUtc() * 1000));
            String subreddit = "r/" + mPosts.get(position).getSubreddit();

            textViewHolder.mTxt_subreddit.setText(subreddit);
            textViewHolder.mTxt_user.setText(user);
            textViewHolder.mTxt_score.setText(String.valueOf(mPosts.get(position).getScore()));
            textViewHolder.mTxt_postTitle.setText(mPosts.get(position).getTitle());
            textViewHolder.mTxt_postText.setText(mPosts.get(position).getSelftext());

        } else if (mPosts.get(position).getPost_hint() != null && mPosts.get(position).getPost_hint().contains("image")) {
            //Bind ImageViewHolder
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;

            String user = "Posted by u/" + mPosts.get(position).getAuthor()
                    + " - "
                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(mPosts.get(position).getCreatedUtc() * 1000));
            String subreddit = "r/" + mPosts.get(position).getSubreddit();

            imageViewHolder.mTxt_subreddit.setText(subreddit);
            imageViewHolder.mTxt_user.setText(user);
            imageViewHolder.mTxt_score.setText(String.valueOf(mPosts.get(position).getScore()));
            imageViewHolder.mTxt_postTitle.setText(mPosts.get(position).getTitle());
            Picasso.get().load(mPosts.get(position).getUrlOverriddenByDest()).into(imageViewHolder.mImg_postImage);


        } else {
            //Bind SimpleViewHolder
            SimpleViewHolder simpleViewHolder = (SimpleViewHolder) holder;

            String user = "Posted by u/" + mPosts.get(position).getAuthor()
                    + " - "
                    + ((String) android.text.format.DateUtils.getRelativeTimeSpanString(mPosts.get(position).getCreatedUtc() * 1000));
            String subreddit = "r/" + mPosts.get(position).getSubreddit();

            simpleViewHolder.mTxt_subreddit.setText(subreddit);
            simpleViewHolder.mTxt_user.setText(user);
            simpleViewHolder.mTxt_score.setText(String.valueOf(mPosts.get(position).getScore()));

        }
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder {

        TextView mTxt_score;
        TextView mTxt_subreddit;
        TextView mTxt_user;
        TextView mTxt_postTitle;
        TextView mTxt_postText;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxt_subreddit = itemView.findViewById(R.id.txt_subreddit);
            mTxt_user = itemView.findViewById(R.id.txt_user);
            mTxt_score = itemView.findViewById(R.id.txt_score);
            mTxt_postTitle = itemView.findViewById(R.id.txt_postTitle);
            mTxt_postText = itemView.findViewById(R.id.txt_postText);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView mTxt_score;
        TextView mTxt_subreddit;
        TextView mTxt_user;
        TextView mTxt_postTitle;
        ImageView mImg_postImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxt_subreddit = itemView.findViewById(R.id.txt_subreddit);
            mTxt_user = itemView.findViewById(R.id.txt_user);
            mTxt_score = itemView.findViewById(R.id.txt_score);
            mTxt_postTitle = itemView.findViewById(R.id.txt_postTitle);
            mImg_postImage = itemView.findViewById(R.id.img_postImage);
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        TextView mTxt_score;
        TextView mTxt_subreddit;
        TextView mTxt_user;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxt_subreddit = itemView.findViewById(R.id.txt_subreddit);
            mTxt_user = itemView.findViewById(R.id.txt_user);
            mTxt_score = itemView.findViewById(R.id.txt_score);
        }
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
//            holder.mTxt_score.setText(String.valueOf(mPosts.get(position).getScore()));
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
