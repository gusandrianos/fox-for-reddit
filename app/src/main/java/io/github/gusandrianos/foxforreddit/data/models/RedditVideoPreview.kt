package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

class RedditVideoPreview {
    @SerializedName("duration")
    val duration = 0

    @SerializedName("is_gif")
    val isGif = false

    @SerializedName("dash_url")
    val dashUrl: String? = null

    @SerializedName("fallback_url")
    val fallbackUrl: String? = null

    @SerializedName("width")
    val width = 0

    @SerializedName("scrubber_media_url")
    val scrubberMediaUrl: String? = null

    @SerializedName("hls_url")
    val hlsUrl: String? = null

    @SerializedName("transcoding_status")
    val transcodingStatus: String? = null

    @SerializedName("height")
    val height = 0
}