package io.github.gusandrianos.foxforreddit.utilities

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.libRG.CustomTextView
import com.stfalcon.imageviewer.StfalconImageViewer
import io.github.gusandrianos.foxforreddit.Constants
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Flair
import io.github.gusandrianos.foxforreddit.data.models.RichtextItem
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
        mainActivity.foxSharedViewModel.previousDestination = mainActivity.bottomNavView.selectedItemId
        mainActivity.logInOnReddit()
    }

    fun getRawImageURI(imageURI: String): String {
        return imageURI.split("\\?".toRegex()).toTypedArray()[0]
    }

    fun getPostType(item: Data): Int {
        if (item.name!!.startsWith("t1_"))
            return Constants.COMMENT

        if (item.post_hint == null) {
            if (item.pollData != null)    //IF it is poll THEN must have poll data
                return Constants.POLL
            if (item.media != null)       //IF it's not the above THEN: IF it is rich/hosted:video THEN must have Media
                return Constants.VIDEO
            if (item.url!!.contains("https://i.") || item.isGallery == true)  //IF it's nothing from the above THEN: IF it is image THEN contains https://i. (not sure)
                return Constants.IMAGE
            return if (item.domain!!.contains("self."))  //IF it's nothing from the above THEN: IF it is self THEN contains domain with self. (not sure)
                Constants.SELF
            else Constants.LINK
        }
        if (item.post_hint.contains("self"))
            return Constants.SELF
        if (item.post_hint.contains("image"))
            return Constants.IMAGE
        if (item.post_hint.contains("link"))
            return Constants.LINK
        if (item.post_hint.contains("video"))
            return Constants.VIDEO
        return if (item.post_hint.contains("poll"))
            Constants.POLL
        else
            Constants.SELF    //If all the above do not feet, return SELF which is the "safest" type for binding
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

    fun upVoteColor(data: Data, upVoteBtn: ImageButton, downVoteBtn: ImageButton, score: TextView, activity: MainActivity) {
        if (data.likes == null || !(data.likes as Boolean)) {
            upVoteBtn.setImageResource(R.drawable.ic_round_arrow_upward_24_orange)
            downVoteBtn.setImageResource(R.drawable.ic_round_arrow_downward_24)
            score.setTextColor(ContextCompat.getColor(activity.applicationContext, android.R.color.holo_orange_dark))
        } else {
            upVoteBtn.setImageResource(R.drawable.ic_round_arrow_upward_24)
            score.setTextColor(ContextCompat.getColor(activity.applicationContext, android.R.color.tab_indicator_text))
        }
    }

    fun downVoteColor(data: Data, upVoteBtn: ImageButton, downVoteBtn: ImageButton, score: TextView, activity: MainActivity) {
        if (data.likes == null || (data.likes as Boolean)) {
            upVoteBtn.setImageResource(R.drawable.ic_round_arrow_upward_24)
            downVoteBtn.setImageResource(R.drawable.ic_round_arrow_downward_24_blue)
            score.setTextColor(ContextCompat.getColor(activity.applicationContext, android.R.color.holo_blue_dark))
        } else {
            downVoteBtn.setImageResource(R.drawable.ic_round_arrow_downward_24)
            score.setTextColor(ContextCompat.getColor(activity.applicationContext, android.R.color.tab_indicator_text))
        }
    }

    fun upVoteModel(viewModel: PostViewModel, application: Application, data: Data) {
        if (data.likes == null || !(data.likes as Boolean)) {          //If down or no voted
            viewModel.votePost("1", data.name!!, application)     //then send up vote
            data.likes = true
        } else {                                                      //else (up voted)
            viewModel.votePost("0", data.name!!, application)    //send no vote
            data.likes = null
        }
    }

    fun downVoteModel(viewModel: PostViewModel, application: Application, data: Data) {
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
        return if (formattedNumber.length > 4) formattedNumber.replace("\\.[0-9]+".toRegex(), "").trim() else formattedNumber.trim()
    }

    fun makeFlair(type: String?, richtext: List<RichtextItem>?, text: String?, textColor: String?,
                  backgroundColor: String?, view: CustomTextView) {

        // TODO: Handle emoji in flair text
        if (type.equals("richtext")
                && !richtext.isNullOrEmpty()) {
            for (richTextItem in richtext) {
                if (richTextItem.type.equals("text")) {
                    view.text = richTextItem.text.trim()
                }
            }
        } else {
            view.text = text
        }

        if (textColor.equals("light"))
            view.setTextColor(Color.parseColor("#FFFFFF"))
        if (!backgroundColor.isNullOrEmpty()) {
            view.setBackgroundColor(Color.parseColor(backgroundColor))
            view.setBorderColor(Color.parseColor(backgroundColor))
        }
    }
}
