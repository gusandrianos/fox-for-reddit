package io.github.gusandrianos.foxforreddit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;

public class TokenViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TokenRepository mTokenRepository;
    private Application mApplication;

    public TokenViewModelFactory(Application application, TokenRepository tokenRepository) {
        mTokenRepository = tokenRepository;
        mApplication = application;
    }

    @SuppressWarnings("unchecked") //suppress return
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return ((T) new TokenViewModel(mApplication, mTokenRepository));
    }
}
