package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Thing
import io.github.gusandrianos.foxforreddit.data.repositories.UserRepository

class UserViewModel(private val mUserRepository: UserRepository, private val mApplication: Application) : ViewModel() {
    private var user: LiveData<Data>? = null
    private var me: LiveData<Data>? = null
    private var trophies: LiveData<List<Thing>>? = null


    fun getUser(username: String): LiveData<Data> {
        if (user != null)
            return user!!
        user = mUserRepository.getUser(username, mApplication)
        return user!!
    }

    fun getMe(): LiveData<Data> {
        if (me != null)
            return me!!
        me = mUserRepository.getMe(mApplication)
        return me!!
    }

    fun getTrophies(application: Application, username: String): LiveData<List<Thing>> {
        if (trophies != null)
            return trophies!!
        trophies = mUserRepository.getTrophies(application, username)
        return trophies!!
    }
}