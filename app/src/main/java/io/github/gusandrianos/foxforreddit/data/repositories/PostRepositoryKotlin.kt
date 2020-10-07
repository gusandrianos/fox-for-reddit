package io.github.gusandrianos.foxforreddit.data.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import io.github.gusandrianos.foxforreddit.data.models.Token

object PostRepositoryKotlin{
    public val pageArray: ArrayList<String> = ArrayList()

    fun getSearchPosts(subreddit: String, filter: String, token: Token) =
            Pager(
                    config = PagingConfig(
                            pageSize = 25,
                            maxSize = 100,
                            enablePlaceholders = false
                    ),
                    pagingSourceFactory = { PostPagingSource(subreddit, filter, token) }
            ).liveData
}