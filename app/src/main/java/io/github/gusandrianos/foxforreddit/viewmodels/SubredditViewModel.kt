package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Flair
import io.github.gusandrianos.foxforreddit.data.models.ModeratorsList
import io.github.gusandrianos.foxforreddit.data.models.RulesBundle
import io.github.gusandrianos.foxforreddit.data.repositories.SubredditRepository
import javax.inject.Singleton

@Singleton
class SubredditViewModel @ViewModelInject constructor(private val mSubredditRepository: SubredditRepository) : ViewModel() {

    fun getSubreddit(subredditName: String): LiveData<Data> {
        return mSubredditRepository.getSubreddit(subredditName)
    }

    fun getSubredditWiki(subredditName: String): LiveData<Data> {
        return mSubredditRepository.getSubredditWiki(subredditName)
    }

    fun getSubredditRules(subredditName: String): LiveData<RulesBundle> {
        return mSubredditRepository.getSubredditRules(subredditName)
    }

    fun getSubredditModerators(subredditName: String): LiveData<ModeratorsList> {
        return mSubredditRepository.getSubredditModerators(subredditName)
    }

    fun toggleSubscribed(action: Int, subredditName: String): LiveData<Boolean> {
        return mSubredditRepository.toggleSubscribed(action, subredditName)
    }

    fun getSubredditLinkFlair(subredditName: String): LiveData<List<Flair>> {
        return mSubredditRepository.getSubredditLinkFlair(subredditName)
    }
}
