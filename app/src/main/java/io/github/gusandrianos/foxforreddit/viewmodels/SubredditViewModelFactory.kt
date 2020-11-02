package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.gusandrianos.foxforreddit.data.repositories.SubredditRepository

@Suppress("UNCHECKED_CAST")
class SubredditViewModelFactory(private val mSubredditRepository: SubredditRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SubredditViewModel(mSubredditRepository) as T
    }
}