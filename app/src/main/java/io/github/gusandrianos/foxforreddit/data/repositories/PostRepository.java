package io.github.gusandrianos.foxforreddit.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
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
public class PostRepository {

    private static PostRepository instance;
    private ArrayList<Post> dataSet;
    private MutableLiveData<List<Post>> data = new MutableLiveData<>();
    RedditAPI redditAPI = RetrofitService.getRedditAPIInstance();

    public static PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
            Log.i("INSTANCE", "created new instance");
        } else {
            Log.i("INSTANCE", "passed same instance");
        }
        return instance;
    }

    public LiveData<List<Post>> getPosts(Token token, String subreddit, String filter) {

        String BEARER = " " + token.getmTokenType() + " " + token.getmAccessToken();
        Log.i("Brearer", BEARER);

        Call<Listing> listing = redditAPI.getPosts(subreddit, filter, BEARER);


        listing.enqueue(new Callback<Listing>() {
            @Override
            public void onResponse(Call<Listing> call, Response<Listing> response) {
                dataSet = new ArrayList<>();  //ToDo check if it is correct
                for (ChildrenItem child : response.body().getTreeData().getChildren()) {
                    dataSet.add(child.getPost());
                }

                data.setValue(dataSet); //When we get a response the mutableLiveData will add the posts
                Log.d("res", response.body().toString());
            }

            @Override
            public void onFailure(Call<Listing> call, Throwable t) {

                Log.d("error", t.getMessage());

            }
        });

        return data;
    }

    public LiveData<List<Post>> getPosts(Token token) {
        return getPosts(token, "", "");
    }
}
