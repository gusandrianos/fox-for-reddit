package io.github.gusandrianos.foxforreddit.data.network

import io.github.gusandrianos.foxforreddit.data.models.Listing
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditAPI {
    /**
     * @param subreddit
     * Optional, references the subreddit to load posts from
     * @param filter
     * Possible values: Hot, New, Rising, Random.
     * @return
     * List of Posts
     */
    @GET("{subreddit}/{filter}")
    suspend fun getPostList(
            @Path("subreddit") subreddit: String,
            @Path("filter") filter: String,
            @Query("after") after: String,
            @Query("count") count: Int,
            @Header("Authorization") bearer: String
    ): Listing

    @GET("{subreddit}/{filter}")
    fun getPosts(
            @Path("subreddit") subreddit: String?,
            @Path("filter") filter: String?,
            @Query("after") after: String?,
            @Header("Authorization") bearer: String?
    ): Call<Listing>
}