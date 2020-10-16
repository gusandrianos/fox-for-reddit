package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.gusandrianos.foxforreddit.data.models.User
import io.github.gusandrianos.foxforreddit.data.models.trophies.TrophiesItem
import io.github.gusandrianos.foxforreddit.data.repositories.UserRepository

class UserViewModel(private val mUserRepository: UserRepository, private val mApplication: Application) : ViewModel() {
    private var user: LiveData<User>? = null
    private var me: LiveData<User>? = null
    private var trophies: LiveData<List<TrophiesItem>>? = null


    fun getUser(username: String): LiveData<User> {
        if (user != null)
            return user!!
        user = mUserRepository.getUser(username, mApplication)
        return user!!
    }

    fun getMe(): LiveData<User> {
        if (me != null)
            return me!!
        me = mUserRepository.getMe(mApplication)
        return me!!
    }

    fun getTrophies(application: Application, username: String): LiveData<List<TrophiesItem>> {
        if (trophies != null)
            return trophies!!
        trophies = mUserRepository.getTrophies(application, username)
        return trophies!!
    }
}