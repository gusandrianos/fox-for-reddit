package io.github.gusandrianos.foxforreddit.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.gusandrianos.foxforreddit.data.models.CommentListing
import io.github.gusandrianos.foxforreddit.data.models.Thing
import io.github.gusandrianos.foxforreddit.data.models.Token
import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.MoreChildren
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PostRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    private val commentsData = MutableLiveData<CommentListing>()
    private val dataMoreChildren = MutableLiveData<MoreChildren>()

    fun getPosts(subreddit: String, filter: String, application: Application) =
            Pager(
                    config = PagingConfig(pageSize = 25, prefetchDistance = 25, enablePlaceholders = false),
                    pagingSourceFactory = { PostPagingSource(subreddit, filter, getBearer(application)) }
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

    fun getSinglePost(subreddit: String, commentID: String, article: String, token: Token): LiveData<CommentListing> {
        val bearer = " " + token.tokenType + " " + token.accessToken
        val singlePost = redditAPI.getSinglePost(subreddit, commentID, article, bearer)
        singlePost.enqueue(object : Callback<List<Any>> {
            override fun onResponse(call: Call<List<Any>>, response: Response<List<Any>>) {
                if (response.isSuccessful) {
                    val commentsType = object : TypeToken<CommentListing?>() {}.type
                    val gson = Gson()
                    val comments = gson.fromJson<CommentListing>(gson.toJsonTree(response.body()!![1]).asJsonObject, commentsType)
                    commentsData.value = comments
                }
            }

            override fun onFailure(call: Call<List<Any>>, t: Throwable) {
            }
        })
        return commentsData
    }

    fun getMoreChildren(linkId: String, children: String, token: Token): LiveData<MoreChildren> {
        val bearer = " " + token.tokenType + " " + token.accessToken
        val moreChildren = redditAPI.getMoreChildren(bearer, linkId, children, "json")
        moreChildren.enqueue(object : Callback<MoreChildren> {
            override fun onResponse(call: Call<MoreChildren>, response: Response<MoreChildren>) {
                if (response.isSuccessful) {
                    dataMoreChildren.value = response.body()
                }
            }

            override fun onFailure(call: Call<MoreChildren>, t: Throwable) {
            }
        })
        return dataMoreChildren
    }

    private fun getBearer(application: Application): String {
        val token = InjectorUtils.getInstance().provideTokenRepository(application).token
        return " " + token.tokenType + " " + token.accessToken
    }
}