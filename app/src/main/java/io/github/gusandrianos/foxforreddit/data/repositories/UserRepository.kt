package io.github.gusandrianos.foxforreddit.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.google.gson.JsonObject
import io.github.gusandrianos.foxforreddit.Constants
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Thing
import io.github.gusandrianos.foxforreddit.data.models.UserPrefs
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.getBearer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val mTokenRepository: TokenRepository,
    private val redditAPI: RedditAPI
) {

    private var user: MutableLiveData<Data> = MutableLiveData()
    private var me: MutableLiveData<Data> = MutableLiveData()
    private var trophies: MutableLiveData<List<Thing>> = MutableLiveData()
    private var userPrefs: MutableLiveData<UserPrefs> = MutableLiveData()

    fun getUser(username: String): LiveData<Data> {
        val bearer = getBearer(mTokenRepository)
        val about = redditAPI.getUser(username, bearer)
        about.enqueue(object : Callback<Thing> {
            override fun onResponse(call: Call<Thing>, response: Response<Thing>) {
                if (response.isSuccessful)
                    user.value = response.body()?.data
            }

            override fun onFailure(call: Call<Thing>, t: Throwable) {
                Log.i("USER REPOSITORY", "getUser() onFailure: ${t.message}")
            }
        })
        return user
    }

    fun getMe(): LiveData<Data> {
        val bearer = getBearer(mTokenRepository)
        val about = redditAPI.getMe(bearer)
        about.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful)
                    me.value = response.body()
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Log.i("USER REPOSITORY", "getMe() onFailure: ${t.message}")
            }
        })
        return me
    }

    fun getTrophies(username: String): LiveData<List<Thing>> {
        val bearer = getBearer(mTokenRepository)
        val trophiesRequest = redditAPI.getTrophies(bearer, username)
        trophiesRequest.enqueue(object : Callback<Thing> {
            override fun onResponse(call: Call<Thing>, response: Response<Thing>) {
                if (response.isSuccessful)
                    trophies.value = response.body()?.data?.trophies
            }

            override fun onFailure(call: Call<Thing>, t: Throwable) {
                Log.i("USER REPOSITORY", "getTrophies() onFailure: ${t.message}")
            }
        })
        return trophies
    }

    fun getSubreddits(location: String): RedditPagingSource {
        return RedditPagingSource(location, getBearer(mTokenRepository), Constants.MODE_SUBREDDIT, redditAPI)
    }

    fun getPrefs(): LiveData<UserPrefs> {
        val bearer = getBearer(mTokenRepository)
        val prefsRequest = redditAPI.getPrefs(bearer)
        prefsRequest.enqueue((object : Callback<UserPrefs> {
            override fun onResponse(call: Call<UserPrefs>, response: Response<UserPrefs>) {
                userPrefs.value = response.body()
            }

            override fun onFailure(call: Call<UserPrefs>, t: Throwable) {
                Log.i("PREFS", "onFailure: ${t.message}")
            }
        }))
        return userPrefs
    }

    fun getMessagesWhere(where: String) =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                RedditPagingSource(
                    where,
                    getBearer(mTokenRepository),
                    Constants.MODE_MESSAGES,
                    redditAPI
                )
            }
        ).liveData

    fun blockUser(accountId: String, name: String): LiveData<Boolean> {
        val status = MutableLiveData<Boolean>()
        val bearer = getBearer(mTokenRepository)
        val blockRequest = redditAPI.blockUser(bearer, accountId, "json", name)
        blockRequest.enqueue((object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                status.value = response.isSuccessful
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i("blockUser", "onFailure: ${t.message}")
            }
        }))
        return status
    }

    fun messageCompose(toUser: String, subject: String, text: String): LiveData<Boolean?> {
        val success = MutableLiveData<Boolean?>()
        val bearer = getBearer(mTokenRepository)
        val sendMessage = redditAPI.messageCompose(bearer, toUser, subject, text, "json")
        sendMessage.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful)
                    if (response.body().toString().contains("USER_DOESNT_EXIST"))
                        success.value = null
                    else
                        success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                success.value = false
            }
        })
        return success
    }

    fun commentCompose(thing_id: String, text: String): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(mTokenRepository)
        val sendComment = redditAPI.commentCompose(bearer, thing_id, text, "json")
        sendComment.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                success.value = false
            }

        })
        return success
    }

    fun deleteMsg(id: String): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        val bearer = getBearer(mTokenRepository)
        val deleteMessage = redditAPI.deleteMsg(bearer, id)
        deleteMessage.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                success.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                success.value = false
            }
        })
        return success
    }
}
