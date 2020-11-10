package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Listing
import io.github.gusandrianos.foxforreddit.data.repositories.SearchRepository

class SearchViewModel(private val mSearchRepository: SearchRepository) : ViewModel() {
    private var searchPost: LiveData<PagingData<Data>>? = null

    fun searchTopSubreddits(query: String, includeOver18: Boolean, includeProfiles: Boolean, application: Application): LiveData<Listing> {
        return mSearchRepository.searchTopSubreddits(query, includeOver18, includeProfiles, application)
    }

    fun searchResults(query: String, sort: String, time: String, restrict_sr: Boolean, type: String, subreddit: String, application: Application): LiveData<PagingData<Data>> {
        if (searchPost != null)
            return searchPost!!;
        searchPost = mSearchRepository.searchResults(query, sort, time, restrict_sr, type, subreddit, application).cachedIn(viewModelScope)
        return searchPost!!
    }

    fun deleteCached() {
        searchPost = null
    }
}
