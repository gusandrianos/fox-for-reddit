package io.github.gusandrianos.foxforreddit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;

public class TokenViewModel extends AndroidViewModel {
    private TokenRepository mTokenRepository;
    private MutableLiveData<Token> mToken;

    public TokenViewModel(@NonNull Application application, TokenRepository tokenRepository) {
        super(application);
        mTokenRepository = tokenRepository;
        mTokenRepository.setApplication(application);
    }

    public LiveData<Token> getToken() {
        return mTokenRepository.getToken();
    }

    public LiveData<Token> getToken(String code, String redirectURI) {
        return mTokenRepository.getToken(code, redirectURI);
    }

    public LiveData<List<Token>> getCachedToken() {
        return mTokenRepository.getCachedToken();
    }
}
