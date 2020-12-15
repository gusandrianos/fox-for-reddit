package io.github.gusandrianos.foxforreddit.utilities;

import javax.inject.Inject;

import io.github.gusandrianos.foxforreddit.data.db.TokenDao;
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository;
import io.github.gusandrianos.foxforreddit.data.repositories.SearchRepository;
import io.github.gusandrianos.foxforreddit.data.repositories.SubredditRepository;
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;
import io.github.gusandrianos.foxforreddit.data.repositories.UserRepository;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.SearchViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

public class InjectorUtils {

    private static InjectorUtils instance;
    TokenDao mTokenDao;

    public static InjectorUtils getInstance(TokenDao tokenDao) {
        if (instance == null) {
            instance = new InjectorUtils();
            instance.mTokenDao = tokenDao;
        }
        return instance;
    }

    private InjectorUtils() {

    }

    public TokenRepository provideTokenRepository() {
        return TokenRepository.getInstance();
    }

    public SearchViewModelFactory provideSearchViewModelFactory() {
        SearchRepository searchRepository = new SearchRepository(mTokenDao);
        return new SearchViewModelFactory(searchRepository);
    }
}
