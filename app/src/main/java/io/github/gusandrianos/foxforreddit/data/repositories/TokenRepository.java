package io.github.gusandrianos.foxforreddit.data.repositories;

import android.app.Application;
import android.util.Log;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.github.gusandrianos.foxforreddit.data.db.FoxDatabase;
import io.github.gusandrianos.foxforreddit.data.db.TokenDao;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.network.OAuthToken;
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Response;

public class TokenRepository {
    private static TokenRepository instance;
    OAuthToken tokenRequest = RetrofitService.getTokenRequestInstance();
    private TokenDao tokenDao;
    private Application application;
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

    public void setApplication(Application application) {
        this.application = application;
    }

    public void initDB() {
        if (application != null) {
            FoxDatabase foxDatabase = FoxDatabase.getInstance(application);
            tokenDao = foxDatabase.tokenDao();
        }
    }

    public Token getNewToken(String code, String redirectURI) {
        if (mToken != null && (code.isEmpty() || redirectURI.isEmpty()))
            return mToken;

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
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mToken;
    }

    public Token refreshToken() {
        if (tokenDao == null) {
            initDB();
        }

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
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mToken;
    }

    public Token getNewToken() {
        return getNewToken("", "");
    }

    public Token getCachedToken() {
        if (tokenDao == null) {
            initDB();
        }

        ExecutorService service = Executors.newSingleThreadExecutor();

        class SelectTask implements Callable<List<Token>> {
            @Override
            public List<Token> call() throws Exception {
                return tokenDao.getToken();
            }
        }

        Future<List<Token>> resultFuture = service.submit(new SelectTask());
        try {
            List<Token> result = resultFuture.get();
            if (result.size() > 0) {
                Token cachedToken = result.get(0);
                mToken = cachedToken;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mToken;
    }

    public Token getToken() {
        if (mToken != null) {
            return mToken;
        }
        if (getCachedToken() != null) {
            long now = Instant.now().getEpochSecond();
            long expiration = mToken.getExpirationTimestamp();
            if (expiration - now > 0) {
                return mToken;
            } else {
                if (refreshToken() != null) {
                    return mToken;
                }
            }
        }
        if (getNewToken() != null) {
            return mToken;
        }

        return mToken;
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

    public void logToken(Token token) {
        Log.i("Token: ", token.getAccessToken());
        Log.i("Type: ", token.getTokenType());
        Log.i("Expires: ", token.getExpiresIn());
        Log.i("Scope: ", token.getScope());
        Log.i("Timestamp: ", String.valueOf(token.getExpirationTimestamp()));
    }
}
