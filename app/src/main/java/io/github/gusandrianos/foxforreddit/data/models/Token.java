package io.github.gusandrianos.foxforreddit.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "token_table")
public class Token {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "access_token")
    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @ColumnInfo(name = "token_type")
    @SerializedName("token_type")
    @Expose
    private String tokenType;

    @ColumnInfo(name = "expires_in")
    @SerializedName("expires_in")
    @Expose
    private String expiresIn;

    @ColumnInfo(name = "scope")
    @SerializedName("scope")
    @Expose
    private String scope;

    @ColumnInfo(name = "refresh_token")
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    @ColumnInfo(name = "expiration_timestamp")
    private long expirationTimestamp;

    public Token(String accessToken, String tokenType, String expiresIn, String scope, String refreshToken) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.refreshToken = refreshToken;
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

    public String getRefreshToken() { return refreshToken; }

    public long getExpirationTimestamp() { return expirationTimestamp; }

    public void setExpirationTimestamp(long expirationTimestamp) { this.expirationTimestamp = expirationTimestamp; }

    public void setAccessToken(@NonNull String accessToken) { this.accessToken = accessToken; }
}
