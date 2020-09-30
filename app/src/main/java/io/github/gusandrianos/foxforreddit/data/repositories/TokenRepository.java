package io.github.gusandrianos.foxforreddit.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.UUID;

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

    public LiveData<Token> getToken(String code, String redirectURI) {
        if (data.getValue() != null)
            return data;

        Call<Token> token;
        String clientID = "n1R0bc_lPPTtVg";
        String password = "";
        if (code.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            token = tokenRequest.getUserlessToken(Credentials.basic(clientID, password), "https://oauth.reddit.com/grants/installed_client", uuid);
        } else {
            token = tokenRequest.getAuthorizedUserToken(Credentials.basic(clientID, password), "authorization_code", code, redirectURI);
        }
        token.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                try {
                    Token responseToken = response.body();
                    Log.i("Token: ", responseToken.getmAccessToken());
                    Log.i("Type: ", responseToken.getmTokenType());
                    Log.i("Expires: ", responseToken.getmExpiresIn());
                    Log.i("Scope: ", responseToken.getmScope());
                    data.setValue(responseToken);
                } catch (NullPointerException e) {
                    Log.i("Token: ", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
        return data;
    }

    public LiveData<Token> getToken() {
        return getToken("", "");
    }
}
