package io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren

import com.google.gson.annotations.SerializedName

class Json {
    @SerializedName("data")
    val data: DataThings? = null

    @SerializedName("errors")
    val errors: List<Any>? = null
}