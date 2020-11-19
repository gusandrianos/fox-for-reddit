package io.github.gusandrianos.foxforreddit.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Flair(
        @field:SerializedName("background_color")
        val backgroundColor: String = "",

        @field:SerializedName("allowable_content")
        val allowableContent: String? = null,

        @field:SerializedName("max_emojis")
        val maxEmojis: Int? = null,

        @field:SerializedName("richtext")
        val richtext: List<RichtextItem>? = null,

        @field:SerializedName("text")
        val text: String? = null,

        @field:SerializedName("text_color")
        val textColor: String? = null,

        @field:SerializedName("id")
        val id: String = "",

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("text_editable")
        val textEditable: Boolean? = null,

        @field:SerializedName("mod_only")
        val modOnly: Boolean? = null
) : Parcelable

@Parcelize
data class RichtextItem(
        @field:SerializedName("t")
        val text: String = "",

        @field:SerializedName("e")
        val type: String = "",

        @field:SerializedName("a")
        val richText: String = "",

        @field:SerializedName("u")
        val source: String = ""
) : Parcelable
