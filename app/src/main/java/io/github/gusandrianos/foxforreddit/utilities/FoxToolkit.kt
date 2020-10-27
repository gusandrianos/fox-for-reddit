package io.github.gusandrianos.foxforreddit.utilities

import android.app.Application

object FoxToolkit {
    fun getBearer(application: Application): String {
        val token = InjectorUtils.getInstance().provideTokenRepository().getToken(application)
        return " " + token.tokenType + " " + token.accessToken
    }

    fun getRawImageURI(imageURI: String): String {
        return imageURI.split("\\?".toRegex()).toTypedArray()[0]
    }

    fun getIsGif(imageUri: String): Boolean {
        return imageUri.endsWith(".gif")
    }
}