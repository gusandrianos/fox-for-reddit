package io.github.gusandrianos.foxforreddit.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;

public class TokenViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TokenRepository mTokenRepository;

    public TokenViewModelFactory(TokenRepository tokenRepository) {
        mTokenRepository = tokenRepository;
    }

    @SuppressWarnings("unchecked") //suppress return
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return ((T) new TokenViewModel(mTokenRepository));
    }
}
