package io.github.gusandrianos.foxforreddit.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import io.github.gusandrianos.foxforreddit.data.models.Listing;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI;
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Singleton pattern
 */
public class PostRepositoryJava {
    private static PostRepositoryJava instance;
    private ArrayList<Post> dataSet;
    private MutableLiveData<Listing> data = new MutableLiveData<>();
    RedditAPI redditAPI = RetrofitService.getRedditAPIInstance();

    public static PostRepositoryJava getInstance() {
        if (instance == null) {
            instance = new PostRepositoryJava();
            Log.i("INSTANCE", "created new instance");
        } else {
            Log.i("INSTANCE", "passed same instance");
        }
        return instance;
    }

    public LiveData<Listing> getPosts(String subreddit, String filter, String after, Token token) {
        String BEARER = " " + token.getTokenType() + " " + token.getAccessToken();
        Log.i("Brearer", BEARER);

        Call<Listing> listing = redditAPI.getPosts(subreddit, filter, after, BEARER);

        listing.enqueue(new Callback<Listing>() {
            @Override
            public void onResponse(Call<Listing> call, Response<Listing> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body()); //When we get a response the mutableLiveData will add the posts
                    Log.d("res", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Listing> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
        return data;
    }

    public LiveData<Listing> getPosts(String after, Token token) {
        return getPosts("", "", after, token);
    }
}
