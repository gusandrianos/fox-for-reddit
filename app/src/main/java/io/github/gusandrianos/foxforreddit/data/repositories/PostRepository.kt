package io.github.gusandrianos.foxforreddit.data.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.gusandrianos.foxforreddit.data.models.*
import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.MoreChildren
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.getBearer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/*
    Singleton class used as an MVVM repository for Posts
 */
object PostRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    private val commentsData = MutableLiveData<CommentListing>()

    /*
        Used for passing a RedditPagingSource Object to the ViewModel
     */
    fun getPosts(subreddit: String, filter: String, time: String, application: Application): RedditPagingSource {
        return RedditPagingSource(subreddit, filter, time, getBearer(application))
    }

    /*
        Used for Voting a Post/Comment
     */
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

    /*
        Gets both the post and comment data of a single post
     */
    fun getSinglePost(permalink: String, application: Application): LiveData<SinglePost> {
        val singlePostData = MutableLiveData<SinglePost>()
        val bearer = getBearer(application)
        val singlePost = redditAPI.getSinglePost(bearer, permalink)
        singlePost.enqueue(object : Callback<List<Any>> {
            override fun onResponse(call: Call<List<Any>>, response: Response<List<Any>>) {
                if (response.isSuccessful) {
                    val gson = Gson()

                    val postType = object : TypeToken<Thing?>() {}.type
                    val commentsType = object : TypeToken<CommentListing?>() {}.type
                    val post = gson.fromJson<Thing>(gson.toJsonTree(response.body()!![0])
                            .asJsonObject, postType)
                    val comments = gson.fromJson<CommentListing>(gson.toJsonTree(response.body()
                    !![1]).asJsonObject, commentsType)

                    singlePostData.value = SinglePost(post, comments)
                }
                Log.i("SinglePost", "onResponse: ${response.message()}")
            }

            override fun onFailure(call: Call<List<Any>>, t: Throwable) {
            }
        })
        return singlePostData
    }

    /*
        Gets the comment data of a single post
     */
    fun getSinglePostComments(permalink: String, application: Application):
            LiveData<CommentListing> {
        val bearer = getBearer(application)
        val singlePost = redditAPI.getSinglePost(bearer, permalink)
        singlePost.enqueue(object : Callback<List<Any>> {
            override fun onResponse(call: Call<List<Any>>, response: Response<List<Any>>) {
                if (response.isSuccessful) {
                    val commentsType = object : TypeToken<CommentListing?>() {}.type
                    val gson = Gson()
                    val comments = gson.fromJson<CommentListing>(gson.toJsonTree(response.body()
                    !![1]).asJsonObject, commentsType)
                    commentsData.value = comments
                }
                Log.i("SinglePost", "onResponse: ${response.message()}")
            }

            override fun onFailure(call: Call<List<Any>>, t: Throwable) {
            }
        })
        return commentsData
    }

    /*
        Gets the hidden children of a comment or more comments from a single post
     */
    fun getMoreChildren(linkId: String, children: String, application: Application):
            LiveData<MoreChildren> {
        val dataMoreChildren = MutableLiveData<MoreChildren>()
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

    /*
        Submits a new selftext post
     */
    fun submitText(type: String, subreddit: String, title: String, url: String, text: String,
                   nsfw: Boolean, spoiler: Boolean, flair_id: String, flair_text: String,
                   application: Application): LiveData<SubmitResponse> {
        val submissionData = MutableLiveData<SubmitResponse>()
        val bearer = getBearer(application)
        val submit = redditAPI.submitText(bearer, type, subreddit, title, url, text, nsfw,
                spoiler, flair_id, flair_text, "json", true)
        submit.enqueue(object : Callback<SubmitResponse> {
            override fun onResponse(call: Call<SubmitResponse>, response:
            Response<SubmitResponse>) {
                Log.i("postResponse", "onResponse: ${response.body()}")
                if (response.isSuccessful)
                    submissionData.value = response.body()
            }

            override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
            }
        })
        return submissionData
    }

    /*
        Saves a post
     */
    fun savePost(id: String, application: Application): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val save = redditAPI.savePost(bearer, id)
        save.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                success.value = false
            }
        })
        return success
    }

    /*
        Unsaves a post
     */
    fun unSavePost(id: String, application: Application): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val unSave = redditAPI.unSavePost(bearer, id)
        unSave.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                success.value = false
            }
        })
        return success
    }

    /*
        Hides a post
     */
    fun hidePost(id: String, application: Application): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val hide = redditAPI.hidePost(bearer, id)
        hide.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                success.value = false
            }
        })
        return success
    }

    /*
        Unhides a post
     */
    fun unHidePost(id: String, application: Application): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val unHide = redditAPI.unHidePost(bearer, id)
        unHide.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                success.value = false
            }
        })
        return success
    }

    /*
       Reports a post
    */
    fun reportPost(thing_id: String, reason: String, application: Application): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val report = redditAPI.reportPost(bearer, thing_id, reason)
        report.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                success.value = false
            }
        })
        return success
    }

    /*
       Selects a flair (when editing or submitting a new post)
    */
    fun selectFlair(subreddit: String, link: String, templateId: String,
                    application: Application): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val selectFlair = redditAPI.selectFlair(bearer, subreddit, "json", link,
                templateId)

        selectFlair.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                success.value = false
            }
        })
        return success
    }

    /*
        Toggles NSFW for a post
     */
    fun markNSFW(id: String, isNSFW: Boolean, application: Application): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val markNSFW = if (isNSFW)
            redditAPI.unmarkNSFW(bearer, id)
        else
            redditAPI.markNSFW(bearer, id)

        markNSFW.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }

        })
        return success
    }

    /*
        Toggles spoiler for a post
     */
    fun markSpoiler(id: String, isSpoiler: Boolean, application: Application): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val markSpoiler = if (isSpoiler)
            redditAPI.unmarkSpoiler(bearer, id)
        else
            redditAPI.markSpoiler(bearer, id)

        markSpoiler.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }

        })
        return success
    }

    /*
        Deletes a post
     */
    fun deleteSubmission(id: String, application: Application): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val deleteSubmission = redditAPI.deleteSubmission(bearer, id)

        deleteSubmission.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }

        })
        return success
    }

    /*
        Edits a post
     */
    fun editSubmission(text: String, thing_id: String, application: Application)
            : LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(application)
        val editSubmission = redditAPI.editSubmission(bearer, "json", text, thing_id)

        editSubmission.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }

        })
        return success
    }
}
