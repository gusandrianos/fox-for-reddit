package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.gusandrianos.foxforreddit.data.models.Post
import io.github.gusandrianos.foxforreddit.data.models.Token
import io.github.gusandrianos.foxforreddit.data.models.generatedComments.comments.Comments
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository

class PostViewModel(private val mPostRepository: PostRepository) : ViewModel() {
    var posts: LiveData<PagingData<Post>>? = null
    var mComments: LiveData<Comments>? = null

    fun getPosts(subreddit: String, filter: String, token: Token): LiveData<PagingData<Post>> {
        if (posts != null) {
            return posts!!;
        }
        posts = mPostRepository.getPosts(subreddit, filter, token).cachedIn(viewModelScope)
        return posts!!
    }

    fun votePost(dir: String, id: String, token: Token){
        mPostRepository.votePost(dir, id, token)
    }

    fun getSinglePost(subreddit: String, commentID: String, article: String, token: Token): LiveData<Comments>{
        return mPostRepository.getSinglePost(subreddit, commentID, article, token)
    }
}