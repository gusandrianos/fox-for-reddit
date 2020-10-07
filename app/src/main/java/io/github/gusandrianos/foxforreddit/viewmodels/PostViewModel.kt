package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import io.github.gusandrianos.foxforreddit.data.models.Listing
import io.github.gusandrianos.foxforreddit.data.models.Post
import io.github.gusandrianos.foxforreddit.data.models.Token
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepositoryKotlin

class PostViewModel(private val mPostRepository: PostRepositoryKotlin) : ViewModel() {

    fun getPosts(subreddit: String, filter: String, token: Token): LiveData<PagingData<Post>> {
        return mPostRepository.getSearchPosts(subreddit, filter, token)
    }


}