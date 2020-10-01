package io.github.gusandrianos.foxforreddit.data.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Preview{
    @SerializedName("enabled")
    private boolean enabled;

    @SerializedName("reddit_video_preview")
    private RedditVideoPreview redditVideoPreview;

    public RedditVideoPreview getRedditVideoPreview() {
        return redditVideoPreview;
    }
}