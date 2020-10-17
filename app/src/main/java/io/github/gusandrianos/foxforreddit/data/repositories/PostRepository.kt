package io.github.gusandrianos.foxforreddit.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.google.gson.Gson
import com.google.gson.JsonArray
import io.github.gusandrianos.foxforreddit.data.models.Token
import io.github.gusandrianos.foxforreddit.data.models.generatedComments.comments.Comments
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PostRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    private val dataComments = MutableLiveData<Comments>()

    fun getPosts(subreddit: String, filter: String, token: Token) =
            Pager(
                    config = PagingConfig(pageSize = 25, prefetchDistance = 25, enablePlaceholders = false),
                    pagingSourceFactory = { PostPagingSource(subreddit, filter, token) }
            ).liveData

    fun votePost(dir: String, id: String, token: Token) {
        val bearer = " " + token.tokenType + " " + token.accessToken
        val vote = redditAPI.votePost(bearer, dir, id, 123)
        vote.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                //ToDo votePost onResponse
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                //ToDo votePost onFailure
            }
        })
    }

    fun getSinglePost(subreddit: String, commentID: String, article: String, token: Token): LiveData<Comments> {
        val bearer = " " + token.tokenType + " " + token.accessToken
        val singlePost = redditAPI.getSinglePost(subreddit, commentID, article, bearer)
        singlePost.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val jsonArray = JSONArray(response.body().toString())
                    val comment: Comments = Gson().fromJson(jsonArray.getJSONObject(1).toString(), Comments::class.java)
                    dataComments.value = comment
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.i("SinglePost", "onFailure: " + t.message)
            }

        })
        return dataComments
    }
}