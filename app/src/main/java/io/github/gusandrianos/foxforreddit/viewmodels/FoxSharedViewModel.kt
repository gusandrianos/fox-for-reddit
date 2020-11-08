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
            subredditChoice.value = currentSubreddit
        return subredditChoice
    }

    fun clearComposeData() {
        subredditChoice.value = ""
        currentSubreddit = ""
    }
}