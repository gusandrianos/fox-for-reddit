package io.github.gusandrianos.foxforreddit.utilities;

import android.app.Application;
import android.util.Log;

import io.github.gusandrianos.foxforreddit.data.repositories.PostRepositoryJava;
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepositoryKotlin;
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;

public class InjectorUtils {

    private static InjectorUtils instance;

    public static InjectorUtils getInstance() {
        if (instance == null) {
            instance = new InjectorUtils();
            Log.i("INSTANCE-INJECTOR", "created new instance");
        } else {
            Log.i("INSTANCE-INJECTOR", "passed same instance");
        }
        return instance;
    }

    private InjectorUtils() {

    }

    public PostViewModelFactory providePostViewModelFactory() {
        PostRepositoryKotlin postRepository = PostRepositoryKotlin.INSTANCE;
        return new PostViewModelFactory(postRepository);
    }

    public TokenRepository provideTokenRepository(Application application) {
        TokenRepository tokenRepository = TokenRepository.getInstance();
        tokenRepository.setApplication(application);
        return tokenRepository;
    }
}

