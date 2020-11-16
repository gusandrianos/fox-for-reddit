package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.gusandrianos.foxforreddit.data.models.Flair

class FoxSharedViewModel : ViewModel() {
    var currentUserUsername: String = ""
    var currentSubreddit: String = ""
    private var subredditChoice: MutableLiveData<String> = MutableLiveData()
    var viewingSelf: Boolean = false
    var previousDestination: Int = 0
    var isNSFW: Boolean = false
    var isSpoiler: Boolean = false
    var includeOver18: Boolean = false
    var currentFlair: Flair? = null
    private var flairChoice: MutableLiveData<Flair?> = MutableLiveData()

    fun getSubredditChoice(): LiveData<String> {
        subredditChoice.value = currentSubreddit
        return subredditChoice
    }

    fun getFlairChoice(): LiveData<Flair?> {
        flairChoice.value = currentFlair
        return flairChoice
    }

    fun clearComposeData() {
        currentFlair = null
        flairChoice.value = null
        subredditChoice.value = ""
        currentSubreddit = ""
        isNSFW = false
        isSpoiler = false
    }
}
