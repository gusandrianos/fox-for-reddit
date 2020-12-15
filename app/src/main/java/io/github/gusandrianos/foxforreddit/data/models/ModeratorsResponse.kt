package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

data class ModeratorsResponse(
    @field:SerializedName("data")
    val modList: ModeratorsList? = null,
)

data class Moderator(
    @field:SerializedName("author_flair_css_class")
    val authorFlairCssClass: Any? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("author_flair_text")
    val authorFlairText: Any? = null
)

data class ModeratorsList(
    @field:SerializedName("children")
    val moderators: List<Moderator?>? = null
)
