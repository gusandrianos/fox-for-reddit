package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

class Oembed {
    @SerializedName("author_name")
    val authorName: String? = null

    @SerializedName("provider_url")
    val providerUrl: String? = null

    @SerializedName("title")
    val title: String? = null

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null

    @SerializedName("type")
    val type: String? = null

    @SerializedName("version")
    val version: String? = null

    @SerializedName("thumbnail_height")
    val thumbnailHeight = 0

    @SerializedName("author_url")
    val authorUrl: String? = null

    @SerializedName("thumbnail_width")
    val thumbnailWidth = 0

    @SerializedName("width")
    val width = 0

    @SerializedName("html")
    val html: String? = null

    @SerializedName("provider_name")
    val providerName: String? = null

    @SerializedName("height")
    val height = 0
}