package io.github.gusandrianos.foxforreddit.data.repositories

import androidx.paging.PagingSource
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import retrofit2.HttpException
import java.io.IOException

class SubredditListPagingSource(
        private val mWhere: String,
        private val mBearer: String
) : PagingSource<String, Data>() {
    private val STARTER_PAGE = ""
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Data> {
        val position = params.key ?: STARTER_PAGE

        return try {
            val response = redditAPI.getSubreddits(mBearer, mWhere, position, params.loadSize)
            val items = response.data.children?.map { it.data } ?: emptyList()

            LoadResult.Page(
                    data = items,
                    prevKey = null,
                    nextKey = if (items.isEmpty()) null else response.data?.after
            )
        } catch (exception: IOException) {  //Maybe there is no Internet Connection when try to make request
            LoadResult.Error(exception)
        } catch (exception: HttpException) { //Maybe something went wrong on the server (ex. no data, or not authorized)
            LoadResult.Error(exception)
        }
    }
}