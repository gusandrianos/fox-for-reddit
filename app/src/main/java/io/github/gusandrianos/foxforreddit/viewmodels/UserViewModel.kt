package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Thing
import io.github.gusandrianos.foxforreddit.data.models.UserPrefs
import io.github.gusandrianos.foxforreddit.data.repositories.UserRepository
import org.json.JSONObject
import org.json.JSONStringer

class UserViewModel(private val mUserRepository: UserRepository) : ViewModel() {
    private var subreddits: LiveData<PagingData<Data>>? = null
    private var userPrefs: LiveData<UserPrefs>? = null

    fun getUser(application: Application, username: String): LiveData<Data> {
        return mUserRepository.getUser(username, application)
    }

    fun getMe(application: Application): LiveData<Data> {
        return mUserRepository.getMe(application)
    }

    fun getTrophies(application: Application, username: String): LiveData<List<Thing>> {
        return mUserRepository.getTrophies(application, username)
    }

    fun getSubreddits(application: Application, location: String): LiveData<PagingData<Data>> {
        if (subreddits != null)
            return subreddits!!
        subreddits = mUserRepository.getSubreddits(application, location).cachedIn(viewModelScope)
        return subreddits!!
    }

    fun getPrefs(application: Application): LiveData<UserPrefs> {
        userPrefs = mUserRepository.getPrefs(application)
        return userPrefs!!
    }
}
