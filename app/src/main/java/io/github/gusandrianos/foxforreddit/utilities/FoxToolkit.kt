package io.github.gusandrianos.foxforreddit.utilities

import android.app.Application
import android.content.Intent
import android.net.Uri
import io.github.gusandrianos.foxforreddit.Constants
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel

object FoxToolkit {
    fun getBearer(application: Application): String {
        val token = InjectorUtils.getInstance().provideTokenRepository().getToken(application)
        return " " + token.tokenType + " " + token.accessToken
    }

    fun getRawImageURI(imageURI: String): String {
        return imageURI.split("\\?".toRegex()).toTypedArray()[0]
    }

    fun getTypeOfImage(data: Data): Int {
        return if (data.isGallery != null && data.isGallery)
            Constants.IS_GALLERY
        else if (data.urlOverriddenByDest!!.endsWith(".gif"))
            Constants.IS_GIF
        else
            Constants.IS_IMAGE
    }

    fun getTypeOfVideo(data: Data): Int {
        return if (data.preview != null && data.preview.redditVideoPreview == null && !data.isVideo)
            Constants.UNPLAYABLE_VIDEO
        else
            Constants.PLAYABLE_VIDEO
    }

    fun upVote(viewModel: PostViewModel, application: Application, data: Data) {
        if (data.likes == null || !(data.likes as Boolean)) {          //If down or no voted
            viewModel.votePost("1", data.name!!, application)     //then send up vote
            data.likes = true
        } else {                                                      //else (up voted)
            viewModel.votePost("0", data.name!!, application)    //send no vote
            data.likes = null;
        }
    }

    fun downVote(viewModel: PostViewModel, application: Application, data: Data) {
        if (data.likes == null || (data.likes as Boolean)) {     //If up or no voted
            viewModel.votePost("-1", data.name!!, application) //then send down vote
            data.likes = false
        } else {                                                   //else (down voted)
            viewModel.votePost("0", data.name!!, application) //send no vote
            data.likes = null
        }
    }

    fun shareLink(data: Data): Intent{
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = Constants.REDDIT_URL + data.permalink
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        return sharingIntent
    }

    fun visitLink(data: Data): Intent{
        val url: String = data.url!!
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        return intent
    }

    fun getTimeOfVideo(time: Int): String? {
        val min = time / 60
        val sec = time - min * 60
        val minStr = if (min < 10) "0$min" else min.toString()
        val secStr = if (sec < 10) "0$sec" else sec.toString()
        return "$minStr:$secStr"
    }
}