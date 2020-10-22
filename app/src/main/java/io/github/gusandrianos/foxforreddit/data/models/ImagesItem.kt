package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

class ImagesItem {
    @SerializedName("resolutions")
    val resolutions: List<ResolutionsItem>? = null

    @SerializedName("source")
    val source: Source? = null

    @SerializedName("id")
    val id: String? = null
}