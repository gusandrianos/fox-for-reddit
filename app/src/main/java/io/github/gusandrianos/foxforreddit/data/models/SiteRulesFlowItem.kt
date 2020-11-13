package io.github.gusandrianos.foxforreddit.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SiteRulesFlowItem(
        @field:SerializedName("reasonTextToShow")
        val reasonTextToShow: String? = null,

        @field:SerializedName("reasonText")
        val reasonText: String? = null,

        @field:SerializedName("nextStepReasons")
        val nextStepReasons: List<NextStepReasonsItem?>? = null,

        @field:SerializedName("nextStepHeader")
        val nextStepHeader: String? = null
) : Parcelable
