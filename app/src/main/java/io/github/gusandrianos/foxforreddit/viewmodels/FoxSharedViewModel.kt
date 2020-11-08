package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FoxSharedViewModel : ViewModel() {
    var currentUserUsername: String = ""
    var currentSubreddit: String = ""
    private var subredditChoice: MutableLiveData<String> = MutableLiveData()
    var viewingSelf: Boolean = false
    var previousDestination: Int = 0

    fun getSubredditChoice(): LiveData<String> {
        if (subredditChoice.value.isNullOrEmpty())
            subredditChoice.value = currentSubreddit
        return subredditChoice
    }

    fun setSubredditChoice(subreddit: String) {
        subredditChoice.value = subreddit
    }

    fun clearComposeData() {
        subredditChoice.value = ""
    }
}