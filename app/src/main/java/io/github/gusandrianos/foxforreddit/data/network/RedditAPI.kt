package io.github.gusandrianos.foxforreddit.data.network

import com.google.gson.JsonArray
import io.github.gusandrianos.foxforreddit.data.models.Listing
import io.github.gusandrianos.foxforreddit.data.models.Data
import io.github.gusandrianos.foxforreddit.data.models.Thing

import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.MoreChildren
import org.json.JSONObject
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
            @Query("t") time: String,
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
    ): Call<Thing>

    @GET("api/v1/me")
    fun getMe(
            @Header("Authorization") bearer: String,
    ): Call<Data>

    @GET("{permalink}")
    fun getSinglePost(
            @Header("Authorization") bearer: String,
            @Path("permalink") permalink: String
    ): Call<List<Any>>

    @GET("api/morechildren")
    fun getMoreChildren(
            @Header("Authorization") bearer: String,
            @Query("link_id") linkId: String,
            @Query("children") children: String,
            @Query("api_type") apiType: String
    ): Call<MoreChildren>

    @GET("api/v1/user/{username}/trophies")
    fun getTrophies(
            @Header("Authorization") bearer: String,
            @Path("username") username: String
    ): Call<Thing>

    @GET("subreddits/{location}")
    suspend fun getSubreddits(
            @Header("Authorization") bearer: String,
            @Path("location") where: String,
            @Query("after") after: String,
            @Query("count") count: Int
    ): Listing

    @GET("{subreddit_prefixed}/about")
    fun getSubreddit(
            @Header("Authorization") bearer: String,
            @Path("subreddit_prefixed") subreddit: String
    ): Call<Thing>

    @FormUrlEncoded
    @POST("api/subscribe")
    fun toggleSubscribe(
            @Header("Authorization") bearer: String,
            @Field("action") action: String,
            @Field("sr_name") sr_name: String
    ): Call<Void>

    @GET("api/subreddit_autocomplete_v2")
    fun searchTopSubreddits(
            @Header("Authorization") bearer: String,
            @Query("query") query: String,
            @Query("include_over_18") includeOver18: Boolean,
            @Query("include_profiles") includeProfiles: Boolean,
            @Query("typeahead_active") typeahead_active: Boolean
    ): Call<Listing>
}
