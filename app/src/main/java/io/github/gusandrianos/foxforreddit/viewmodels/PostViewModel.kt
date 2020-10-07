package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import io.github.gusandrianos.foxforreddit.data.models.Post
import io.github.gusandrianos.foxforreddit.data.models.Token
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository

class PostViewModel(private val mPostRepository: PostRepository) : ViewModel() {
    fun getPosts(subreddit: String, filter: String, token: Token): LiveData<PagingData<Post>> {
        return mPostRepository.getPosts(subreddit, filter, token)
    }
}