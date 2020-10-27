package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.repositories.SubredditRepository

class SubredditViewModel(private val mSubredditRepository: SubredditRepository) : ViewModel() {
    private var subreddit: LiveData<Data>? = null

    fun getSubreddit(subredditName: String, application: Application): LiveData<Data> {
        if (subreddit != null)
            return subreddit!!
        subreddit = mSubredditRepository.getSubreddit(subredditName, application)
        return subreddit!!
    }

    fun toggleSubscribed(action: Int, subredditName: String, application: Application): LiveData<Boolean> {
        return mSubredditRepository.toggleSubscribed(action, subredditName, application)
    }
}
