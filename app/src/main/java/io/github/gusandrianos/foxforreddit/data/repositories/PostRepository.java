package io.github.gusandrianos.foxforreddit.data.repositories;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.Listing;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.network.OAuthToken;
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI;
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService;
import okhttp3.Credentials;
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
    OAuthToken tokenRequest = RetrofitService.getTokenRequestInstance();
    RedditAPI redditAPI = RetrofitService.getRedditAPIInstance();

    public static PostRepository getInstance(){
        if (instance == null){
            instance = new PostRepository();
            Log.i("INSTANCE", "created new instance");
        }
        else{Log.i("INSTANCE", "passed same instance");}
        return instance;
    }

    public LiveData<List<Post>> getPosts(){

//        OAuthToken tokenRequest = RetrofitService.getTokenRequestInstance();
//        RedditAPI redditAPI = RetrofitService.getRedditAPIInstance();
        Call<Token> userlessToken = tokenRequest.getUserlessToken(Credentials.basic("n1R0bc_lPPTtVg", ""), "https://oauth.reddit.com/grants/installed_client", "DO_NOT_TRACK_THIS_DEVICE");
        userlessToken.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                try {
                    Token token = response.body();
                    Log.i("Userless_Token: ", token.getmAccessToken());
                    Log.i("Userless_Type: ", token.getmTokenType());
                    Log.i("Userless_Expires: ", token.getmExpiresIn());
                    Log.i("Userless_Scope: ", token.getmScope());

                } catch (NullPointerException e) {
                    Log.i("Userless_Token: ", e.getMessage());
                }

                Log.i("TOKEN", response.body().getmAccessToken());
                String BEARER = " " + response.body().getmTokenType() + " " + response.body().getmAccessToken();
                Log.i("Brearer", BEARER);

                Call<Listing> listing = redditAPI.getPosts("", "hot", BEARER);


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
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
        return data;
    }
}
