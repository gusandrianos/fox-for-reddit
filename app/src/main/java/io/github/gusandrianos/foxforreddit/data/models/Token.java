package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    private String mAccessToken;
    @SerializedName("token_type")
    private String mTokenType;
    @SerializedName("expires_in")
    private String mExpiresIn;
    @SerializedName("scope")
    private String mScope;

    public Token(String AccessToken, String TokenType, String ExpiresIn, String Scope) {
        mAccessToken = AccessToken;
        mTokenType = TokenType;
        mExpiresIn = ExpiresIn;
        mScope = Scope;
    }

    public String getmAccessToken() {
        return mAccessToken;
    }

    public String getmTokenType() {
        return mTokenType;
    }

    public String getmExpiresIn() {
        return mExpiresIn;
    }

    public String getmScope() {
        return mScope;
    }
}
