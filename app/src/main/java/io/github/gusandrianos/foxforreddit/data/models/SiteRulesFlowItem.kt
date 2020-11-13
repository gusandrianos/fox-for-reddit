package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

data class SiteRulesFlowItem(

	@field:SerializedName("reasonTextToShow")
	val reasonTextToShow: String? = null,

	@field:SerializedName("reasonText")
	val reasonText: String? = null,

	@field:SerializedName("nextStepReasons")
	val nextStepReasons: List<NextStepReasonsItem?>? = null,

	@field:SerializedName("nextStepHeader")
	val nextStepHeader: String? = null
)