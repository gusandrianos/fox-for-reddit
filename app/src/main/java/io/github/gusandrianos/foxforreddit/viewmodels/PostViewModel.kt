package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.gusandrianos.foxforreddit.data.models.CommentListing
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Token
import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.MoreChildren
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository

class PostViewModel(private val mPostRepository: PostRepository) : ViewModel() {
    var posts: LiveData<PagingData<Data>>? = null

    fun getPosts(subreddit: String, filter: String, application: Application): LiveData<PagingData<Data>> {
        if (posts != null) {
            return posts!!;
        }
        posts = mPostRepository.getPosts(subreddit, filter, application).cachedIn(viewModelScope)
        return posts!!
    }

    fun votePost(dir: String, id: String, token: Token) {
        mPostRepository.votePost(dir, id, token)
    }

    fun getSinglePost(permalink: String, application: Application): LiveData<CommentListing> {
        return mPostRepository.getSinglePost(permalink, application)
    }

    fun getMoreChildren(linkId: String, children: String, token: Token): LiveData<MoreChildren> {
        Log.i("VIEWMODEL", "getMoreChildren: " + mPostRepository.getMoreChildren(linkId, children, token).toString())
        return mPostRepository.getMoreChildren(linkId, children, token)
    }
}