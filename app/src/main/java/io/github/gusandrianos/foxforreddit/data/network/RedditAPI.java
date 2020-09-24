package io.github.gusandrianos.foxforreddit.data.network;

import java.util.List;

import io.github.gusandrianos.foxforreddit.data.models.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RedditAPI {
    @GET("{subreddit}/hot")
    Call<List<Post>> getHotPosts(@Path("subreddit") String subreddit);
}
