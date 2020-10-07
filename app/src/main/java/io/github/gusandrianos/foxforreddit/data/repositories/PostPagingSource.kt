package io.github.gusandrianos.foxforreddit.data.repositories

import androidx.paging.PagingSource
import io.github.gusandrianos.foxforreddit.data.models.Post
import io.github.gusandrianos.foxforreddit.data.models.Token
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import retrofit2.HttpException
import java.io.IOException


class PostPagingSource(
        private val mSubreddit: String,
        private val mFilter: String,
        private val mToken: Token
) : PagingSource<String, Post>() {
    private val STARTER_PAGE = "";
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    private var mBearer = " " + mToken.tokenType + " " + mToken.accessToken

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        val position = params.key ?: STARTER_PAGE

        return try {
            val response = redditAPI.getPostList(mSubreddit, mFilter, position, params.loadSize, mBearer)
            val items = response.treeData.children?.map { it.post } ?: emptyList();

            LoadResult.Page(
                    data = items,
                    prevKey = null,
                    nextKey = if (items.isEmpty()) null else response.treeData?.after
            )
        } catch (exception: IOException) {  //Maybe there is no Internet Connection when try to make request
            LoadResult.Error(exception)
        } catch (exception: HttpException) { //Maybe something went wrong on the server (ex. no data, or not authorized)
            LoadResult.Error(exception)
        }
    }
}