package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

data class RulesBundle(

	@field:SerializedName("site_rules")
	val siteRules: List<String?>? = null,

	@field:SerializedName("rules")
	val rules: List<RulesItem?>? = null,

	@field:SerializedName("site_rules_flow")
	val siteRulesFlow: List<SiteRulesFlowItem?>? = null
)