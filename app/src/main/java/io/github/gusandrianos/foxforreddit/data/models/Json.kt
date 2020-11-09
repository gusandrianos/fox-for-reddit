package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

data class Json(
        @field:SerializedName("data")
        val submitData: SubmitData? = null,

        @field:SerializedName("errors")
        val errors: List<Any?>? = null
)