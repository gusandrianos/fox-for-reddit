package io.github.gusandrianos.foxforreddit.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import io.github.gusandrianos.foxforreddit.data.models.Token
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PostRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()

    fun getPosts(subreddit: String, filter: String, token: Token) =
            Pager(
                    config = PagingConfig(pageSize = 25, prefetchDistance = 25, enablePlaceholders = false),
                    pagingSourceFactory = { PostPagingSource(subreddit, filter, token) }
            ).liveData

    fun votePost(dir: String, id: String, token: Token) {
        val bearer = " " + token.tokenType + " " + token.accessToken
        var vote = redditAPI.votePost(bearer, dir, id, 123)
        vote.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                //ToDo votePost onResponse
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                //ToDo votePost onFailure
            }
        })
    }
}