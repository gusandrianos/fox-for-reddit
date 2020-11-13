package io.github.gusandrianos.foxforreddit.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RulesItem(
        @field:SerializedName("violation_reason")
        val violationReason: String? = null,

        @field:SerializedName("kind")
        val kind: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("short_name")
        val shortName: String? = null,

        @field:SerializedName("description_html")
        val descriptionHtml: String? = null,

        @field:SerializedName("created_utc")
        val createdUtc: Double? = null,

        @field:SerializedName("priority")
        val priority: Int? = null
) : Parcelable
