package io.github.gusandrianos.foxforreddit.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Listing
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.getBearer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SearchRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    private val searchTopSubreddits = MutableLiveData<Listing>()

    fun searchTopSubreddits(query: String, includeOver18: Boolean, includeProfiles: Boolean, application: Application): LiveData<Listing> {
        val bearer = getBearer(application)
        val search = redditAPI.searchTopSubreddits(bearer, query, includeOver18, includeProfiles, true)
        search.enqueue(object : Callback<Listing> {
            override fun onResponse(call: Call<Listing>, response: Response<Listing>) {
                searchTopSubreddits.value = response.body()
            }

            override fun onFailure(call: Call<Listing>, t: Throwable) {
            }
        })
        return searchTopSubreddits
    }

    fun searchResults(query: String, sort: String, time: String, restrict_sr: Boolean, type: String, subreddit: String, application: Application): LiveData<PagingData<Data>> =
            Pager(
                    config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                    pagingSourceFactory = { RedditPagingSource(query, sort, time, restrict_sr, type, subreddit, getBearer(application)) }
            ).liveData
}
