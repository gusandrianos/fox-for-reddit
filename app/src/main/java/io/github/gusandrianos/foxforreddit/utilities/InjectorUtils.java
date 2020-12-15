package io.github.gusandrianos.foxforreddit.utilities;

import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;

public class InjectorUtils {

    private static InjectorUtils instance;

    private InjectorUtils() {

    }

    public static InjectorUtils getInstance() {
        if (instance == null) {
            instance = new InjectorUtils();
        }
        return instance;
    }

    public TokenRepository provideTokenRepository() {
        return TokenRepository.getInstance();
    }
}
