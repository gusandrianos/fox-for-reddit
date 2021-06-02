package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

class OptionsItem {

    @SerializedName("text")
    val text: String? = null

    @SerializedName("id")
    val id: String? = null

    @SerializedName("vote_count")
    val voteCount: Int? = null
}
