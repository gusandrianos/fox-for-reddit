package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepositoryKotlin

@Suppress("UNCHECKED_CAST")
class PostViewModelFactory(private val mPostRepository: PostRepositoryKotlin) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostViewModel(mPostRepository) as T
    }
}