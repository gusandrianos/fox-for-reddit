package io.github.gusandrianos.foxforreddit.data.repositories;

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gusandrianos.foxforreddit.data.models.User
import io.github.gusandrianos.foxforreddit.data.models.UserResponse
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    private var user: MutableLiveData<User> = MutableLiveData()
    private var me: MutableLiveData<User> = MutableLiveData()

    fun getUser(username: String, application: Application): LiveData<User> {
        val token = InjectorUtils.getInstance().provideTokenRepository(application).token
        val bearer = " " + token.tokenType + " " + token.accessToken
        val about = redditAPI.getUser(username, bearer)
        about.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    user.value = response.body()?.user
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.i("USER REPOSITORY", "getUser() onFailure: ${t.message}")
            }
        })
        return user
    }

    fun getMe(application: Application): LiveData<User> {
        val token = InjectorUtils.getInstance().provideTokenRepository(application).token
        val bearer = " " + token.tokenType + " " + token.accessToken
        val about = redditAPI.getMe(bearer)
        about.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    me.value = response.body()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i("USER REPOSITORY", "getMe() onFailure: ${t.message}")
            }
        })
        return me
    }
}
