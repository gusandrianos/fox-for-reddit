package io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren

import com.google.gson.annotations.SerializedName
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem

class DataThings {

    @SerializedName("things")
    val children: List<ChildrenItem>? = null
}
