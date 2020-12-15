package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import io.github.gusandrianos.foxforreddit.data.models.CommentListing
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.SinglePost
import io.github.gusandrianos.foxforreddit.data.models.SubmitResponse
import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.MoreChildren
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository
import javax.inject.Singleton

@Singleton
class PostViewModel @ViewModelInject constructor(private val mPostRepository: PostRepository) : ViewModel() {
    var posts: LiveData<PagingData<Data>>? = null

    fun getPosts(subreddit: String, filter: String, time: String)
        : LiveData<PagingData<Data>> {
        if (posts != null)
            return posts!!

        posts = Pager(
            config = PagingConfig(pageSize = 25, prefetchDistance = 25,
                enablePlaceholders = false),
            pagingSourceFactory = {
                mPostRepository.getPosts(subreddit, filter, time)
            }
        ).liveData.cachedIn(viewModelScope)

        return posts!!
    }

    fun deleteCached() {
        posts = null
    }

    fun votePost(dir: String, id: String) {
        mPostRepository.votePost(dir, id)
    }

    fun getSinglePost(permalink: String): LiveData<SinglePost> {
        return mPostRepository.getSinglePost(permalink)
    }

    fun getSinglePostComments(permalink: String):
        LiveData<CommentListing> {
        return mPostRepository.getSinglePostComments(permalink)
    }

    fun getMoreChildren(linkId: String, children: String)
        : LiveData<MoreChildren> {
        return mPostRepository.getMoreChildren(linkId, children)
    }

    fun submitText(type: String, subreddit: String, title: String, url: String, text: String,
                   nsfw: Boolean, spoiler: Boolean, flair_id: String, flair_text: String): LiveData<SubmitResponse> {
        return mPostRepository.submitText(type, subreddit, title, url, text, nsfw, spoiler,
            flair_id, flair_text)
    }

    fun savePost(id: String): LiveData<Boolean> {
        return mPostRepository.savePost(id)
    }

    fun unSavePost(id: String): LiveData<Boolean> {
        return mPostRepository.unSavePost(id)
    }

    fun hidePost(id: String): LiveData<Boolean> {
        return mPostRepository.hidePost(id)
    }

    fun unHidePost(id: String): LiveData<Boolean> {
        return mPostRepository.unHidePost(id)
    }

    fun reportPost(thing_id: String, reason: String): LiveData<Boolean> {
        return mPostRepository.reportPost(thing_id, reason)
    }

    fun selectFlair(subreddit: String, link: String, templateId: String): LiveData<Boolean> {
        return mPostRepository.selectFlair(subreddit, link, templateId)
    }

    fun markNSFW(id: String, isNSFW: Boolean): LiveData<Boolean> {
        return mPostRepository.markNSFW(id, isNSFW)
    }

    fun markSpoiler(id: String, isSpoiler: Boolean): LiveData<Boolean> {
        return mPostRepository.markSpoiler(id, isSpoiler)
    }

    fun deleteSubmission(id: String): LiveData<Boolean> {
        return mPostRepository.deleteSubmission(id)
    }

    fun editSubmission(text: String, thing_id: String)
        : LiveData<Boolean> {
        return mPostRepository.editSubmission(text, thing_id)
    }
}