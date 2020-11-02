package io.github.gusandrianos.foxforreddit.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Thing : Parcelable{
    @SerializedName("data")
    val data: Data? = null

    @SerializedName("kind")
    val kind: String? = null
}