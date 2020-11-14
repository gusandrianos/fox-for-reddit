package io.github.gusandrianos.foxforreddit.data.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.gusandrianos.foxforreddit.data.models.CommentListing
import io.github.gusandrianos.foxforreddit.data.models.SubmitResponse
import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.MoreChildren
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.getBearer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PostRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    private val commentsData = MutableLiveData<CommentListing>()
    private val dataMoreChildren = MutableLiveData<MoreChildren>()
    private val submissionData = MutableLiveData<SubmitResponse>()

    fun getPosts(subreddit: String, filter: String, time: String, application: Application) =
            Pager(
                    config = PagingConfig(pageSize = 25, prefetchDistance = 25, enablePlaceholders = false),
                    pagingSourceFactory = { RedditPagingSource(subreddit, filter, time, getBearer(application)) }
            ).liveData

    fun votePost(dir: String, id: String, application: Application) {
        val bearer = getBearer(application)
        val vote = redditAPI.votePost(bearer, dir, id, 123)
        vote.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }

    fun getSinglePost(permalink: String, application: Application): LiveData<CommentListing> {
        val bearer = getBearer(application)
        val singlePost = redditAPI.getSinglePost(bearer, permalink)
        singlePost.enqueue(object : Callback<List<Any>> {
            override fun onResponse(call: Call<List<Any>>, response: Response<List<Any>>) {
                if (response.isSuccessful) {
                    val commentsType = object : TypeToken<CommentListing?>() {}.type
                    val gson = Gson()
                    val comments = gson.fromJson<CommentListing>(gson.toJsonTree(response.body()!![1]).asJsonObject, commentsType)
                    commentsData.value = comments
                }
                Log.i("SinglePost", "onResponse: ${response.message()}")
            }

            override fun onFailure(call: Call<List<Any>>, t: Throwable) {
            }
        })
        return commentsData
    }

    fun getMoreChildren(linkId: String, children: String, application: Application): LiveData<MoreChildren> {
        val bearer = getBearer(application)
        val moreChildren = redditAPI.getMoreChildren(bearer, linkId, children, "json")
        moreChildren.enqueue(object : Callback<MoreChildren> {
            override fun onResponse(call: Call<MoreChildren>, response: Response<MoreChildren>) {
                if (response.isSuccessful)
                    dataMoreChildren.value = response.body()
            }

            override fun onFailure(call: Call<MoreChildren>, t: Throwable) {
            }
        })
        return dataMoreChildren
    }

    fun submitText(type: String, subreddit: String, title: String, url: String, text: String, nsfw: Boolean, spoiler: Boolean, application: Application): LiveData<SubmitResponse> {
        val bearer = getBearer(application)
        val submit = redditAPI.submitText(bearer, type, subreddit, title, url, text, nsfw, spoiler, "json", true)
        submit.enqueue(object : Callback<SubmitResponse> {
            override fun onResponse(call: Call<SubmitResponse>, response: Response<SubmitResponse>) {
                Log.i("postResponse", "onResponse: ${response.body()}")
                if (response.isSuccessful)
                    submissionData.value = response.body()
            }

            override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
            }
        })
        return submissionData
    }

    fun savePost(id: String, application: Application): LiveData<Boolean> {
        val succeed = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val save = redditAPI.savePost(bearer, id)
        save.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                succeed.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                succeed.value = false
            }
        })
        return succeed
    }

    fun unSavePost(id: String, application: Application): LiveData<Boolean> {
        val succeed = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val unSave = redditAPI.unSavePost(bearer, id)
        unSave.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                succeed.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                succeed.value = false
            }
        })
        return succeed
    }

    fun hidePost(id: String, application: Application): LiveData<Boolean> {
        val succeed = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val hide = redditAPI.hidePost(bearer, id)
        hide.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                succeed.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                succeed.value = false
            }
        })
        return succeed
    }

    fun unHidePost(id: String, application: Application): LiveData<Boolean> {
        val succeed = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val unHide = redditAPI.unHidePost(bearer, id)
        unHide.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                succeed.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                succeed.value = false
            }
        })
        return succeed
    }

    fun reportPost(thing_id: String, reason: String, application: Application): LiveData<Boolean> {
        val succeed = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val report = redditAPI.reportPost(bearer, thing_id, reason)
        report.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                succeed.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                succeed.value = false
            }
        })
        return succeed
    }
}