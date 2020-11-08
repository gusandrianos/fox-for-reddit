package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FoxSharedViewModel : ViewModel() {
    var currentUserUsername: String = ""
    var currentSubreddit: String = ""
    var subredditChoice: MutableLiveData<String> = MutableLiveData()
    var viewingSelf: Boolean = false
    var destinationBeforeLoginAttempt: Int = 0

    fun getSubredditChoice(): LiveData<String> {
        return subredditChoice
    }

    fun setSubredditChoice(subreddit: String) {
        subredditChoice.value = subreddit
    }

    fun clearComposeData() {
        subredditChoice.value = "u_${currentUserUsername}"
    }
}