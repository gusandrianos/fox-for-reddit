package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.gusandrianos.foxforreddit.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val mUserRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(mUserRepository) as T
    }
}