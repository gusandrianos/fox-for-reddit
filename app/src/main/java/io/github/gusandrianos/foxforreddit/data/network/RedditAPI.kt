package io.github.gusandrianos.foxforreddit.data.network

import com.google.gson.JsonObject
import io.github.gusandrianos.foxforreddit.data.models.*

import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.MoreChildren
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
            @Header("Authorization") bearer: String
    ): Call<Thing>

    @GET("api/v1/me")
    fun getMe(
            @Header("Authorization") bearer: String
    ): Call<Data>

    @GET("/api/v1/me/prefs")
    fun getPrefs(
            @Header("Authorization") bearer: String
    ): Call<UserPrefs>

    @GET("/message/{where}")
    suspend fun getMessagesWhere(
            @Header("Authorization") bearer: String,
            @Path("where") where: String,
            @Query("after") after: String,
            @Query("count") count: Int,
    ): Listing

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

    @GET("{subreddit_prefixed}/wiki/index")
    fun getSubredditWiki(
            @Header("Authorization") bearer: String,
            @Path("subreddit_prefixed") subreddit: String
    ): Call<Thing>

    @GET("{subreddit_prefixed}/about/rules")
    fun getSubredditRules(
            @Header("Authorization") bearer: String,
            @Path("subreddit_prefixed") subreddit: String
    ): Call<RulesBundle>


    @GET("{subreddit_prefixed}/about/moderators")
    fun getSubredditModerators(
            @Header("Authorization") bearer: String,
            @Path("subreddit_prefixed") subreddit: String
    ): Call<ModeratorsResponse>

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

    @GET("{subreddit}/search")
    suspend fun searchResults(
            @Header("Authorization") bearer: String,
            @Path("subreddit") subreddit: String,
            @Query("q") query: String,
            @Query("sort") sort: String,
            @Query("t") time: String,
            @Query("restrict_sr") restrict_sr: Boolean,
            @Query("type") type: String,
            @Query("count") count: Int,
            @Query("after") after: String
    ): Listing

    @FormUrlEncoded
    @POST("api/submit")
    fun submitText(
            @Header("Authorization") bearer: String,
            @Field("kind") kind: String,
            @Field("sr") subreddit: String,
            @Field("title") title: String,
            @Field("url") url: String,
            @Field("text") text: String,
            @Field("nsfw") nsfw: Boolean,
            @Field("spoiler") spoiler: Boolean,
            @Field("flair_id") flair_id: String,
            @Field("flair_text") flair_text: String,
            @Field("api_type") apiType: String,
            @Field("resubmit") resubmit: Boolean
    ): Call<SubmitResponse>

    @FormUrlEncoded
    @POST("/api/save")
    fun savePost(
            @Header("Authorization") bearer: String,
            @Field("id") id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/unsave")
    fun unSavePost(
            @Header("Authorization") bearer: String,
            @Field("id") id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/hide")
    fun hidePost(
            @Header("Authorization") bearer: String,
            @Field("id") id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/unhide")
    fun unHidePost(
            @Header("Authorization") bearer: String,
            @Field("id") id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/report")
    fun reportPost(
            @Header("Authorization") bearer: String,
            @Field("thing_id") thing_id: String,
            @Field("reason") reason: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("api/block_user")
    fun blockUser(
            @Header("Authorization") bearer: String,
            @Field("account_id") account_id: String,
            @Field("api_type") api_type: String,
            @Field("name") name: String
    ): Call<JsonObject>

    @GET("{subreddit}/api/link_flair_v2")
    fun getSubredditLinkFlair(
            @Header("Authorization") bearer: String,
            @Path("subreddit") subreddit: String
    ): Call<List<Flair>>

    @FormUrlEncoded
    @POST("{subreddit}/api/selectflair")
    fun selectFlair(
            @Header("Authorization") bearer: String,
            @Path("subreddit") subreddit: String,
            @Field("api_type") apiType: String,
            @Field("link") link: String,
            @Field("flair_template_id") templateId: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/marknsfw")
    fun markNSFW(
            @Header("Authorization") bearer: String,
            @Field("id") id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/unmarknsfw")
    fun unmarkNSFW(
            @Header("Authorization") bearer: String,
            @Field("id") id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/spoiler")
    fun markSpoiler(
            @Header("Authorization") bearer: String,
            @Field("id") id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/unspoiler")
    fun unmarkSpoiler(
            @Header("Authorization") bearer: String,
            @Field("id") id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/del")
    fun deleteSubmission(
            @Header("Authorization") bearer: String,
            @Field("id") id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/editusertext")
    fun editSubmission(
            @Header("Authorization") bearer: String,
            @Field("api_type") apiType: String,
            @Field("text") text: String,
            @Field("thing_id") thing_id: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("/api/compose")
    fun messageCompose(
            @Header("Authorization") bearer: String,
            @Field("to") toUser: String,
            @Field("subject") subject: String,
            @Field("text") text: String,
            @Field("api_type") api_type: String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("api/comment")
    fun commentCompose(
            @Header("Authorization") bearer: String,
            @Field("thing_id") thing_id: String,
            @Field("text") text: String,
            @Field("api_type") api_type: String
    ): Call<JsonObject>
}
