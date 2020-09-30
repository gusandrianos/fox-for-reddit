package io.github.gusandrianos.foxforreddit.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.network.OAuthToken;
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenRepository {
    private static TokenRepository instance;
    OAuthToken tokenRequest = RetrofitService.getTokenRequestInstance();
    private MutableLiveData<Token> data = new MutableLiveData<>();


    private TokenRepository() {
    }

    public static TokenRepository getInstance() {
        if (instance == null) {
            instance = new TokenRepository();
            Log.i("INSTANCE", "created new instance");
        } else {
            Log.i("INSTANCE", "passed same instance");
        }
        return instance;
    }

    public LiveData<Token> getToken() {
        if (data.getValue() != null) {
            return data;
        }
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
                    data.setValue(response.body());
                } catch (NullPointerException e) {
                    Log.i("Userless_Token: ", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
        return data;
    }
}
