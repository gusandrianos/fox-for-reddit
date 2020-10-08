package io.github.gusandrianos.foxforreddit.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import io.github.gusandrianos.foxforreddit.data.models.Token

object PostRepository {
    fun getPosts(subreddit: String, filter: String, token: Token) =
            Pager(
                    config = PagingConfig(pageSize = 25, prefetchDistance = 100, enablePlaceholders = false),
                    pagingSourceFactory = { PostPagingSource(subreddit, filter, token) }
            ).liveData
}