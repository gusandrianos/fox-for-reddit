package io.github.gusandrianos.foxforreddit.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;

public class TokenViewModel extends ViewModel {
    private TokenRepository mTokenRepository;
    private MutableLiveData<Token> mToken;

    public TokenViewModel(TokenRepository tokenRepository) {
        mTokenRepository = tokenRepository;
    }

    public LiveData<Token> getToken() {
        return mTokenRepository.getToken();
    }

    public LiveData<Token> getToken(String code, String redirectURI) {
        return mTokenRepository.getToken(code, redirectURI);
    }
}
