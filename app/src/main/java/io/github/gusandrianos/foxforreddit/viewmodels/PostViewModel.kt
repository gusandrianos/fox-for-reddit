package io.github.gusandrianos.foxforreddit.viewmodels

import android.app.Application
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

    fun getPosts(subreddit: String, filter: String, time: String, application: Application)
        : LiveData<PagingData<Data>> {
        if (posts != null)
            return posts!!

        posts = Pager(
            config = PagingConfig(pageSize = 25, prefetchDistance = 25,
                enablePlaceholders = false),
            pagingSourceFactory = {
                mPostRepository.getPosts(subreddit, filter, time, application)
            }
        ).liveData.cachedIn(viewModelScope)

        return posts!!
    }

    fun deleteCached() {
        posts = null
    }

    fun votePost(dir: String, id: String, application: Application) {
        mPostRepository.votePost(dir, id, application)
    }

    fun getSinglePost(permalink: String, application: Application): LiveData<SinglePost> {
        return mPostRepository.getSinglePost(permalink, application)
    }

    fun getSinglePostComments(permalink: String, application: Application):
        LiveData<CommentListing> {
        return mPostRepository.getSinglePostComments(permalink, application)
    }

    fun getMoreChildren(linkId: String, children: String, application: Application)
        : LiveData<MoreChildren> {
        return mPostRepository.getMoreChildren(linkId, children, application)
    }

    fun submitText(type: String, subreddit: String, title: String, url: String, text: String,
                   nsfw: Boolean, spoiler: Boolean, flair_id: String, flair_text: String,
                   application: Application): LiveData<SubmitResponse> {
        return mPostRepository.submitText(type, subreddit, title, url, text, nsfw, spoiler,
            flair_id, flair_text, application)
    }

    fun savePost(id: String, application: Application): LiveData<Boolean> {
        return mPostRepository.savePost(id, application)
    }

    fun unSavePost(id: String, application: Application): LiveData<Boolean> {
        return mPostRepository.unSavePost(id, application)
    }

    fun hidePost(id: String, application: Application): LiveData<Boolean> {
        return mPostRepository.hidePost(id, application)
    }

    fun unHidePost(id: String, application: Application): LiveData<Boolean> {
        return mPostRepository.unHidePost(id, application)
    }

    fun reportPost(thing_id: String, reason: String, application: Application): LiveData<Boolean> {
        return mPostRepository.reportPost(thing_id, reason, application)
    }

    fun selectFlair(subreddit: String, link: String, templateId: String,
                    application: Application): LiveData<Boolean> {
        return mPostRepository.selectFlair(subreddit, link, templateId, application)
    }

    fun markNSFW(id: String, isNSFW: Boolean, application: Application): LiveData<Boolean> {
        return mPostRepository.markNSFW(id, isNSFW, application)
    }

    fun markSpoiler(id: String, isSpoiler: Boolean, application: Application): LiveData<Boolean> {
        return mPostRepository.markSpoiler(id, isSpoiler, application)
    }

    fun deleteSubmission(id: String, application: Application): LiveData<Boolean> {
        return mPostRepository.deleteSubmission(id, application)
    }

    fun editSubmission(text: String, thing_id: String, application: Application)
        : LiveData<Boolean> {
        return mPostRepository.editSubmission(text, thing_id, application)
    }
}