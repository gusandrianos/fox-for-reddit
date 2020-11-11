package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

data class SubmitResponse(
        @field:SerializedName("json")
        val json: Json? = null
)