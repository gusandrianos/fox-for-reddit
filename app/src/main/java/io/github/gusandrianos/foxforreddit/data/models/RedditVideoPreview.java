package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

public class RedditVideoPreview{

    @SerializedName("duration")
    private int duration;

    @SerializedName("is_gif")
    private boolean isGif;

    @SerializedName("dash_url")
    private String dashUrl;

    @SerializedName("fallback_url")
    private String fallbackUrl;

    @SerializedName("width")
    private int width;

    @SerializedName("scrubber_media_url")
    private String scrubberMediaUrl;

    @SerializedName("hls_url")
    private String hlsUrl;

    @SerializedName("transcoding_status")
    private String transcodingStatus;

    @SerializedName("height")
    private int height;
}