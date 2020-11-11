package io.github.gusandrianos.foxforreddit.data.repositories

import androidx.paging.PagingSource
import io.github.gusandrianos.foxforreddit.Constants.MODE_POST
import io.github.gusandrianos.foxforreddit.Constants.MODE_SEARCH_RESULTS
import io.github.gusandrianos.foxforreddit.Constants.MODE_SUBREDDIT
import io.github.gusandrianos.foxforreddit.Constants.STARTER_PAGE
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Listing
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService
import retrofit2.HttpException
import java.io.IOException


class RedditPagingSource() : PagingSource<String, Data>() {

    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()
    private lateinit var mSubreddit: String
    private lateinit var mFilter: String
    private lateinit var mTime: String
    private lateinit var mBearer: String
    private lateinit var mLocation: String
    private lateinit var mQuery: String
    private var mRestrict_sr: Boolean = false
    private lateinit var mType: String

    private var MODE = 0

    constructor (subreddit: String, filter: String, time: String, bearer: String) : this() {
        mSubreddit = subreddit
        mFilter = filter
        mTime = time
        mBearer = bearer
        MODE = MODE_POST
    }

    constructor (where: String, bearer: String) : this() {
        mBearer = bearer
        mLocation = where
        MODE = MODE_SUBREDDIT
    }

    constructor(query: String, sort: String, time: String, restrict_sr: Boolean, type: String, subreddit: String, bearer: String) : this() {
        mSubreddit = subreddit
        mQuery = query
        mFilter = sort
        mTime = time
        mRestrict_sr = restrict_sr
        mType = type
        mBearer = bearer
        MODE = MODE_SEARCH_RESULTS
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Data> {
        val position = params.key ?: STARTER_PAGE

        return try {
            lateinit var response: Listing
            if (MODE == MODE_POST)
                response = redditAPI.getPostList(mSubreddit, mFilter, position, params.loadSize, mTime, mBearer)
            else if (MODE == MODE_SUBREDDIT)
                response = redditAPI.getSubreddits(mBearer, mLocation, position, params.loadSize)
            else if (MODE == MODE_SEARCH_RESULTS)
                response = redditAPI.searchResults(mBearer, mSubreddit, mQuery, mFilter, mTime, mRestrict_sr, mType, params.loadSize, position)
            val items = response.data!!.children?.map { it.data!! } ?: emptyList()

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