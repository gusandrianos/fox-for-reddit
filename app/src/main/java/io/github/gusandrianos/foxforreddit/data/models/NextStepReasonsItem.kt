package io.github.gusandrianos.foxforreddit.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NextStepReasonsItem(
        @field:SerializedName("reasonTextToShow")
        val reasonTextToShow: String? = null,

        @field:SerializedName("reasonText")
        val reasonText: String? = null,

        @field:SerializedName("canSpecifyUsernames")
        val canSpecifyUsernames: Boolean? = null,

        @field:SerializedName("oneUsername")
        val oneUsername: Boolean? = null,

        @field:SerializedName("usernamesInputTitle")
        val usernamesInputTitle: String? = null,

        @field:SerializedName("requestCrisisSupport")
        val requestCrisisSupport: Boolean? = null,

        @field:SerializedName("complaintButtonText")
        val complaintButtonText: String? = null,

        @field:SerializedName("complaintPageTitle")
        val complaintPageTitle: String? = null,

        @field:SerializedName("complaintPrompt")
        val complaintPrompt: String? = null,

        @field:SerializedName("fileComplaint")
        val fileComplaint: Boolean? = null,

        @field:SerializedName("complaintUrl")
        val complaintUrl: String? = null,

        @field:SerializedName("nextStepReasons")
        val nextStepReasons: List<NextStepReasonsItem?>? = null,

        @field:SerializedName("nextStepHeader")
        val nextStepHeader: String? = null
) : Parcelable
