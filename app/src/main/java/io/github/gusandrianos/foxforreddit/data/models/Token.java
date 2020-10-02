package io.github.gusandrianos.foxforreddit.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "token_table")
public class Token {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "access_token")
    @SerializedName("access_token")
    private String accessToken;

    @ColumnInfo(name = "token_type")
    @SerializedName("token_type")
    private String tokenType;

    @ColumnInfo(name = "expires_in")
    @SerializedName("expires_in")
    private String expiresIn;

    @ColumnInfo(name = "scope")
    @SerializedName("scope")
    private String scope;
    
    public Token(String accessToken, String tokenType, String expiresIn, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }

    @NonNull
    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }
}
