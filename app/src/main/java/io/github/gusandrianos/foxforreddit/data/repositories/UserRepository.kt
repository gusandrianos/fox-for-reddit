package io.github.gusandrianos.foxforreddit.data.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Thing
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    private var user: MutableLiveData<Data> = MutableLiveData()
    private var me: MutableLiveData<Data> = MutableLiveData()
    private var trophies: MutableLiveData<List<Thing>> = MutableLiveData()

    fun getUser(username: String, application: Application): LiveData<Data> {
        val bearer = getBearer(application)
        val about = redditAPI.getUser(username, bearer)
        about.enqueue(object : Callback<Thing> {
            override fun onResponse(call: Call<Thing>, response: Response<Thing>) {
                if (response.isSuccessful) {
                    user.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<Thing>, t: Throwable) {
                Log.i("USER REPOSITORY", "getUser() onFailure: ${t.message}")
            }
        })
        return user
    }


    fun getMe(application: Application): LiveData<Data> {
        val bearer = getBearer(application)
        val about = redditAPI.getMe(bearer)
        about.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    me.value = response.body()
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Log.i("USER REPOSITORY", "getMe() onFailure: ${t.message}")
            }
        })
        return me
    }

    fun getTrophies(application: Application, username: String): LiveData<List<Thing>> {
        val bearer = getBearer(application)
        val trophiesRequest = redditAPI.getTrophies(bearer, username)
        trophiesRequest.enqueue(object : Callback<Thing> {
            override fun onResponse(call: Call<Thing>, response: Response<Thing>) {
                if (response.isSuccessful) {
                    trophies.value = response.body()?.data?.trophies
                }
            }

            override fun onFailure(call: Call<Thing>, t: Throwable) {
                Log.i("USER REPOSITORY", "getTrophies() onFailure: ${t.message}")
            }
        })
        return trophies
    }

    fun getSubreddits(application: Application, where: String) =
            Pager(
                    config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                    pagingSourceFactory = { PostPagingSource(where, getBearer(application)) }
            ).liveData

    private fun getBearer(application: Application): String {
        val token = InjectorUtils.getInstance().provideTokenRepository(application).token
        return " " + token.tokenType + " " + token.accessToken
    }
}
