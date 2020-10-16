package io.github.gusandrianos.foxforreddit.data.network

import io.github.gusandrianos.foxforreddit.data.models.Listing
import io.github.gusandrianos.foxforreddit.data.models.User
import io.github.gusandrianos.foxforreddit.data.models.UserResponse
import io.github.gusandrianos.foxforreddit.data.models.trophies.TrophiesResponse
import retrofit2.Call
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("/api/vote")
    fun votePost(
            @Header("Authorization") bearer: String?,
            @Field("dir") dir: String?,
            @Field("id") id: String?,
            @Field("rank") rank: Int?
    ): Call<Void>

    @GET("user/{username}/about")
    fun getUser(
            @Path("username") username: String,
            @Header("Authorization") bearer: String?,
    ): Call<UserResponse>

    @GET("api/v1/me")
    fun getMe(
            @Header("Authorization") bearer: String,
    ): Call<User>

    @GET("api/v1/user/{username}/trophies")
    fun getTrophies(
            @Header("Authorization") bearer: String,
            @Path("username") username: String
    ): Call<TrophiesResponse>
}