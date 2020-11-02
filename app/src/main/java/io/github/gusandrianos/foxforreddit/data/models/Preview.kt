package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

class Preview {
    @SerializedName("enabled")
    val enabled = false

    @SerializedName("images")
    val images: List<ImagesItem>? = null

    @SerializedName("reddit_video_preview")
    val redditVideoPreview: RedditVideoPreview? = null
}