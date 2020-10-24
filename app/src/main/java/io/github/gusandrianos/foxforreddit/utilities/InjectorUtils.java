package io.github.gusandrianos.foxforreddit.utilities;

import android.util.Log;

import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository;
import io.github.gusandrianos.foxforreddit.data.repositories.SubredditRepository;
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;
import io.github.gusandrianos.foxforreddit.data.repositories.UserRepository;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

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
        PostRepository postRepository = PostRepository.INSTANCE;
        return new PostViewModelFactory(postRepository);
    }

    public TokenRepository provideTokenRepository() {
        return TokenRepository.getInstance();
    }

    public UserViewModelFactory provideUserViewModelFactory() {
        UserRepository userRepository = UserRepository.INSTANCE;
        return new UserViewModelFactory(userRepository);
    }

    public SubredditViewModelFactory provideSubredditViewModelFactory() {
        SubredditRepository subredditRepository = SubredditRepository.INSTANCE;
        return new SubredditViewModelFactory(subredditRepository);
    }
}

