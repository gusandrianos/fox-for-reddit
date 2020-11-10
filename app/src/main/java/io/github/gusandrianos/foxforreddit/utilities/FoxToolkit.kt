package io.github.gusandrianos.foxforreddit.utilities

import android.app.Application
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import io.github.gusandrianos.foxforreddit.Constants
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.ui.MainActivity
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.pow

object FoxToolkit {
    fun getBearer(application: Application): String {
        val token = InjectorUtils.getInstance().provideTokenRepository().getToken(application)
        return " " + token.tokenType + " " + token.accessToken
    }

    fun isAuthorized(application: Application): Boolean {
        val token = InjectorUtils.getInstance().provideTokenRepository().getToken(application)
        return !token.refreshToken.isNullOrEmpty()
    }

    fun promptLogIn(mainActivity: MainActivity) {
        mainActivity.destinationBeforeLoginAttempt = mainActivity.bottomNavView.selectedItemId
        mainActivity.loadLogInWebpage()
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
        return if ((data.preview != null && data.preview.redditVideoPreview == null && !data.isVideo)
                || (data.preview == null && !data.isVideo))
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

    fun shareLink(data: Data): Intent {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = Constants.REDDIT_URL + data.permalink
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        return sharingIntent
    }

    fun getTimeOfVideo(time: Long): String? {
        val min = time / 60
        val sec = time - min * 60
        val minStr = if (min < 10) "0$min" else min.toString()
        val secStr = if (sec < 10) "0$sec" else sec.toString()
        return "$minStr:$secStr"
    }

    fun fullscreenImage(post: Data, context: Context) {
        val images = ArrayList<String?>()

        if (getTypeOfImage(post) == Constants.IS_GALLERY) {
            if (post.galleryData != null) {
                for (galleryItem in post.galleryData.items!!) {
                    val url = "https://i.redd.it/" + galleryItem.mediaId + ".jpg"
                    images.add(url)
                }
            }
        } else {
            images.add(post.urlOverriddenByDest)
        }

        StfalconImageViewer.Builder<String>(context, images) { imageView, imageUrl -> Glide.with(context).load(imageUrl).into(imageView) }.show()
    }

    fun formatValue(number: Double): String {
        var value = number
        if (value == 0.0) {
            return 0.toString()
        }
        val suffix = " kmbt"
        var formattedNumber = ""
        val formatter: NumberFormat = DecimalFormat("#,###.#")
        val power: Int = StrictMath.log10(value).toInt()
        value /= 10.0.pow(power / 3 * 3.toDouble())
        formattedNumber = formatter.format(value)
        formattedNumber += suffix[power / 3]
        return if (formattedNumber.length > 4) formattedNumber.replace("\\.[0-9]+".toRegex(), "") else formattedNumber
    }
}
