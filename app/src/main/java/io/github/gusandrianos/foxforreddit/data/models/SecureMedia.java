package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

public class SecureMedia{

    @SerializedName("reddit_video")
    private RedditVideo redditVideo;

    public RedditVideo getRedditVideo(){
        return redditVideo;
    }
}