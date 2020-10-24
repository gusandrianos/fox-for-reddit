package io.github.gusandrianos.foxforreddit.data.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Thing
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit.getBearer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SubredditRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    val subreddit: MutableLiveData<Data> = MutableLiveData()

    fun getSubreddit(subredditName: String, application: Application): LiveData<Data> {
        val bearer = getBearer(application)
        val about = redditAPI.getSubreddit(bearer, subredditName)
        about.enqueue(object : Callback<Thing> {
            override fun onResponse(call: Call<Thing>, response: Response<Thing>) {
                if (response.isSuccessful)
                    subreddit.value = response.body()?.data
            }

            override fun onFailure(call: Call<Thing>, t: Throwable) {
                Log.i("SUBREDDIT REPOSITORY", "getSubreddit() onFailure: ${t.message}")
            }
        })
        return subreddit
    }
}
