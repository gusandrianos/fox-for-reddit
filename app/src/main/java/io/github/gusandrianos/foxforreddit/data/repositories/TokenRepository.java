package io.github.gusandrianos.foxforreddit.data.repositories;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.github.gusandrianos.foxforreddit.data.db.TokenDao;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.network.OAuthToken;
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Response;

// TODO: Rewrite in Kotlin
public class TokenRepository {
    private static TokenRepository instance;
    OAuthToken tokenRequest = RetrofitService.getTokenRequestInstance();
    private TokenDao mTokenDao;
    Token mToken;

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

    public Token getNewToken(@NotNull TokenDao tokenDao, String code, String redirectURI) {
        if (mToken != null && (code.isEmpty() || redirectURI.isEmpty()))
            return mToken;

        if (mTokenDao == null)
            mTokenDao = tokenDao;

        Call<Token> token;
        String clientID = "n1R0bc_lPPTtVg";
        String password = "";
        if (code.isEmpty() || redirectURI.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            token = tokenRequest.getUserlessToken(Credentials.basic(clientID, password), "https://oauth.reddit.com/grants/installed_client", uuid);
        } else {
            token = tokenRequest.getAuthorizedUserToken(Credentials.basic(clientID, password), "authorization_code", code, redirectURI);
        }

        ExecutorService service = Executors.newSingleThreadExecutor();

        class RetroTask implements Callable<Response<Token>> {
            @Override
            public Response<Token> call() throws Exception {
                return token.execute();
            }
        }

        Future<Response<Token>> responseFuture = service.submit(new RetroTask());

        try {
            Response<Token> response = responseFuture.get();
            if (response.isSuccessful()) {
                Token responseToken = response.body();
                responseToken.setExpirationTimestamp(Instant.now().getEpochSecond() + Long.parseLong(responseToken.getExpiresIn()));
                mToken = responseToken;
                setCachedToken(mToken);
                logToken(mToken);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return mToken;
    }

    public Token refreshToken() {
        if (mToken.getRefreshToken() == null) {
            mToken = null;
            return mToken;
        }

        Log.i("Token Refresh", "Token Dao created");

        Call<Token> token;
        String clientID = "n1R0bc_lPPTtVg";
        String password = "";

        token = tokenRequest.refreshAuthorizedUserToken(Credentials.basic(clientID, password), "refresh_token", mToken.getRefreshToken());

        ExecutorService service = Executors.newSingleThreadExecutor();

        class RetroTask implements Callable<Response<Token>> {
            @Override
            public Response<Token> call() throws Exception {
                return token.execute();
            }
        }

        Future<Response<Token>> responseFuture = service.submit(new RetroTask());

        try {
            Response<Token> response = responseFuture.get();
            if (response.isSuccessful()) {
                Token responseToken = response.body();
                mToken.setExpirationTimestamp(Instant.now().getEpochSecond() + Long.parseLong(responseToken.getExpiresIn()));
                mToken.setAccessToken(responseToken.getAccessToken());
                setCachedToken(mToken);
                logToken(mToken);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return mToken;
    }

    public Token getNewToken() {
        return getNewToken(mTokenDao, "", "");
    }

    public Token getCachedToken() {

        ExecutorService service = Executors.newSingleThreadExecutor();

        class SelectTask implements Callable<List<Token>> {
            @Override
            public List<Token> call() {
                return mTokenDao.getToken();
            }
        }

        Future<List<Token>> resultFuture = service.submit(new SelectTask());
        try {
            List<Token> result = resultFuture.get();
            if (result.size() > 0) {
                mToken = result.get(0);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return mToken;
    }

    public Token getToken(TokenDao tokenDao) {
        mTokenDao = tokenDao;
        if (mToken != null) {

            if (!mToken.hasExpired())
                return mToken;
            else {
                if (refreshToken() != null)
                    return mToken;
                else if (getNewToken() != null) {
                    return mToken;
                }
            }
        }

        if (getCachedToken() != null) {
            if (!mToken.hasExpired()) {
                return mToken;
            } else {
                if (refreshToken() != null) {
                    return mToken;
                }
            }
        }

        getNewToken();

        return mToken;
    }

    public void setCachedToken(Token token) {
        Executors.newSingleThreadExecutor().execute(() -> {
            mTokenDao.delete();
            mTokenDao.insert(token);
        });
    }

    public void logOut() {
        Executors.newSingleThreadExecutor().execute(() -> mTokenDao.delete());
        mToken = null;
    }

    public void logToken(Token token) {
        Log.i("Token: ", token.getAccessToken());
        Log.i("Type: ", token.getTokenType());
        Log.i("Expires: ", token.getExpiresIn());
        Log.i("Scope: ", token.getScope());
        Log.i("Timestamp: ", String.valueOf(token.getExpirationTimestamp()));
    }
}
