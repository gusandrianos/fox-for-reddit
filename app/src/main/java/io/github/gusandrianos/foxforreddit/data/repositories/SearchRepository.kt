package io.github.gusandrianos.foxforreddit.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gusandrianos.foxforreddit.data.models.Listing
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.getBearer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val mTokenRepository: TokenRepository,
    private val redditAPI: RedditAPI
) {

    fun searchTopSubreddits(query: String, includeOver18: Boolean, includeProfiles: Boolean): LiveData<Listing> {
        val searchTopSubreddits = MutableLiveData<Listing>()
        val bearer = getBearer(mTokenRepository)
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

    fun searchResults(
        query: String,
        sort: String,
        time: String,
        restrict_sr: Boolean,
        type: String,
        subreddit: String
    ): RedditPagingSource {
        return RedditPagingSource(query, sort, time, restrict_sr, type, subreddit, getBearer(mTokenRepository), redditAPI)
    }
}
