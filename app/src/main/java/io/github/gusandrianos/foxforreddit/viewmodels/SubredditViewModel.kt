package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.repositories.SubredditRepository

class SubredditViewModel(private val mSubredditRepository: SubredditRepository) : ViewModel() {

    fun getSubreddit(subredditName: String, application: Application): LiveData<Data> {
        return mSubredditRepository.getSubreddit(subredditName, application)
    }

    fun getSubredditWiki(subredditName: String, application: Application): LiveData<Data> {
        return mSubredditRepository.getSubredditWiki(subredditName, application)
    }

    fun toggleSubscribed(action: Int, subredditName: String, application: Application): LiveData<Boolean> {
        return mSubredditRepository.toggleSubscribed(action, subredditName, application)
    }
}
