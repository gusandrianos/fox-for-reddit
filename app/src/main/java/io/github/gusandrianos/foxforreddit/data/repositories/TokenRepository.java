package io.github.gusandrianos.foxforreddit.data.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

import io.github.gusandrianos.foxforreddit.data.db.FoxDatabase;
import io.github.gusandrianos.foxforreddit.data.db.TokenDao;
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
    private TokenDao tokenDao;
    private Application application;
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

    public void setApplication(Application application) {
        this.application = application;
    }

    public void initDB() {
        if (application != null) {
            FoxDatabase foxDatabase = FoxDatabase.getInstance(application);
            tokenDao = foxDatabase.tokenDao();
        }
    }

    public LiveData<Token> getToken(String code, String redirectURI) {
        if (data.getValue() != null && (code.isEmpty() || redirectURI.isEmpty()))
            return data;

        if (tokenDao == null) {
            initDB();
        }

        Call<Token> token;
        String clientID = "n1R0bc_lPPTtVg";
        String password = "";
        if (code.isEmpty() || redirectURI.isEmpty()) {
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
                    Log.i("Token: ", responseToken.getAccessToken());
                    Log.i("Type: ", responseToken.getTokenType());
                    Log.i("Expires: ", responseToken.getExpiresIn());
                    Log.i("Scope: ", responseToken.getScope());
                    data.setValue(responseToken);
                    setCachedToken(responseToken);
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

    public void setCachedToken(Token token) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                tokenDao.delete();
                tokenDao.insert(token);
            }
        });
    }

    public LiveData<List<Token>> getCachedToken() {
        if (tokenDao == null) {
            initDB();
        }
        return tokenDao.getToken();
    }
}
