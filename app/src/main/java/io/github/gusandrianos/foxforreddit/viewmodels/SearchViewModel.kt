package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Listing
import io.github.gusandrianos.foxforreddit.data.repositories.SearchRepository
import javax.inject.Singleton

@Singleton
class SearchViewModel @ViewModelInject constructor(private val mSearchRepository: SearchRepository) : ViewModel() {
    private var searchPost: LiveData<PagingData<Data>>? = null

    fun searchTopSubreddits(query: String, includeOver18: Boolean, includeProfiles: Boolean): LiveData<Listing> {
        return mSearchRepository.searchTopSubreddits(query, includeOver18, includeProfiles)
    }

    fun searchResults(query: String, sort: String, time: String, restrict_sr: Boolean, type: String, subreddit: String): LiveData<PagingData<Data>> {
        if (searchPost != null)
            return searchPost!!

        searchPost = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                mSearchRepository.searchResults(query, sort, time, restrict_sr, type, subreddit)
            }
        ).liveData.cachedIn(viewModelScope)

        return searchPost!!
    }

    fun deleteCached() {
        searchPost = null
    }
}
