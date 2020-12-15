package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

data class SubmitData(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("drafts_count")
    val draftsCount: Int? = null
)