package io.github.gusandrianos.foxforreddit.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class MoreChildrenList : Parcelable {

    var moreChildrenList: List<String>? = null
}
