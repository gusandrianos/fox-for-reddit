package io.github.gusandrianos.foxforreddit.data.network;

import com.google.gson.JsonObject;

import java.util.List;

import io.github.gusandrianos.foxforreddit.data.models.Listing;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface RedditAPI {
    /**
     * @param subreddit
     * Optional, references the subreddit to load posts from
     * @param filter
     * Possible values: Hot, New, Rising, Random.
     * @return
     * List of Posts
     */
    @GET("{subreddit}/{filter}")
    Call<Listing> getPosts(
            @Path("subreddit") String subreddit,
            @Path("filter") String filter,
            @Header("Authorization") String bearer
    );
}
