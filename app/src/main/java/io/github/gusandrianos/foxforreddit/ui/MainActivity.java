package io.github.gusandrianos.foxforreddit.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import foxApiWrapper.lib.RedditRequest;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.Listing;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.network.OAuthToken;
import io.github.gusandrianos.foxforreddit.data.network.RedditAPI;
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView bt;
    int LAUNCH_SECOND_ACTIVITY = 1;
    String authString = "n1R0bc_lPPTtVg" + ":";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView result = findViewById(R.id.result);
        OAuthToken tokenRequest = RetrofitService.getTokenRequestInstance();
        RedditAPI redditAPI = RetrofitService.getRedditAPIInstance();

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
                        result.setMovementMethod(new ScrollingMovementMethod());
                        result.setText("");

                        for (ChildrenItem child : response.body().getTreeData().getChildren()) {
                            result.append("r/" + child.getPost().getSubreddit() + "\n");
                            result.append("Posted by u/" + child.getPost().getAuthor() + "\n");
                            result.append(child.getPost().getTitle() + "\n");
                            String date = (String) android.text.format.DateUtils.getRelativeTimeSpanString(child.getPost().getCreatedUtc()*1000);
                            result.append(date + "\n");
                            result.append(child.getPost().getScore() + "\n\n");
                        }
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
    }

    public void loadWebPage(View view) {
        Intent load = new Intent(this, Main2Activity.class);
        load.putExtra("URL", "https://www.reddit.com/api/v1/authorize.compact?client_id=n1R0bc_lPPTtVg&response_type=code&state=ggfgfgfgga&redirect_uri=https://gusandrianos.github.io/login&duration=temporary&scope=identity,mysubreddits");
        startActivityForResult(load, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                String[] inputs = result.split("\\?")[1].split("&");
                String code = inputs[1].split("=")[1];
                String state = inputs[0].split("=")[1];
                Toast.makeText(this, state + " " + code, Toast.LENGTH_LONG).show();

                loadWebPage(code);
            }
        }
    }

    void loadWebPage(String c) {
        RedditRequest okHttpHandler = new RedditRequest();
        okHttpHandler.execute("https://www.reddit.com/api/v1/access_token", authString, c);
    }

    public void me(View view) {
        RedditRequest okHttpHandler = new RedditRequest();
        okHttpHandler.execute("https://oauth.reddit.com/api/v1/me/karma");
    }
}
