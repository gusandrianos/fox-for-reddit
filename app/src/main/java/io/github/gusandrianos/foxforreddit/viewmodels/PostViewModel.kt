package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.gusandrianos.foxforreddit.data.models.CommentListing
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.MoreChildren
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository

class PostViewModel(private val mPostRepository: PostRepository) : ViewModel() {
    var posts: LiveData<PagingData<Data>>? = null

    fun getPosts(subreddit: String, filter: String, time: String, application: Application): LiveData<PagingData<Data>> {
        if (posts != null)
            return posts!!;
        posts = mPostRepository.getPosts(subreddit, filter, time, application).cachedIn(viewModelScope)
        return posts!!
    }

    fun votePost(dir: String, id: String, application: Application) {
        mPostRepository.votePost(dir, id, application)
    }

    fun getSinglePost(permalink: String, application: Application): LiveData<CommentListing> {
        return mPostRepository.getSinglePost(permalink, application)
    }

    fun getMoreChildren(linkId: String, children: String, application: Application): LiveData<MoreChildren> {
        return mPostRepository.getMoreChildren(linkId, children, application)
    }
}