package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

class CommentListing {

    @SerializedName("data")
    val data: CommentData? = null

    @SerializedName("kind")
    val kind: String? = null
}
