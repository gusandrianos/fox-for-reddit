package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Thing
import io.github.gusandrianos.foxforreddit.data.models.UserPrefs
import io.github.gusandrianos.foxforreddit.data.repositories.UserRepository

class UserViewModel @ViewModelInject constructor(private val mUserRepository: UserRepository) : ViewModel() {
    private var subreddits: LiveData<PagingData<Data>>? = null
    private var userPrefs: LiveData<UserPrefs>? = null
    private var messages: LiveData<PagingData<Data>>? = null

    fun getUser(username: String): LiveData<Data> {
        return mUserRepository.getUser(username)
    }

    fun getMe(): LiveData<Data> {
        return mUserRepository.getMe()
    }

    fun getTrophies(username: String): LiveData<List<Thing>> {
        return mUserRepository.getTrophies(username)
    }

    fun getSubreddits(location: String): LiveData<PagingData<Data>> {
        if (subreddits != null)
            return subreddits!!

        subreddits = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                mUserRepository.getSubreddits(location)
            }
        ).liveData.cachedIn(viewModelScope)

        return subreddits!!
    }

    fun getPrefs(): LiveData<UserPrefs> {
        userPrefs = mUserRepository.getPrefs()
        return userPrefs!!
    }

    fun getMessagesWhere(where: String): LiveData<PagingData<Data>> {
        if (messages != null)
            return messages!!
        messages = mUserRepository.getMessagesWhere(where).cachedIn(viewModelScope)
        return messages!!
    }

    fun blockUser(accountId: String, name: String): LiveData<Boolean> {
        return mUserRepository.blockUser(accountId, name)
    }

    fun deleteCached() {
        messages = null
    }

    fun messageCompose(toUser: String, subject: String, text: String): LiveData<Boolean?> {
        return mUserRepository.messageCompose(toUser, subject, text)
    }

    fun commentCompose(thing_id: String, text: String): LiveData<Boolean> {
        return mUserRepository.commentCompose(thing_id, text)
    }

    fun deleteMsg(id: String): LiveData<Boolean> {
        return mUserRepository.deleteMsg(id)
    }
}
