package io.github.gusandrianos.foxforreddit.data.network;

import io.github.gusandrianos.foxforreddit.data.models.Token;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/*
    Models some methods from the Reddit API.
    The functionality of each one is thoroughly described here
    https://www.reddit.com/dev/api/
 */
public interface OAuthToken {
    @FormUrlEncoded
    @POST("/api/v1/access_token")
    Call<Token> getUserlessToken(
            @Header("Authorization") String credentials,
            @Field("grant_type") String grantType,
            @Field("device_id") String deviceID
    );

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    Call<Token> getAuthorizedUserToken(
            @Header("Authorization") String credentials,
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("redirect_uri") String redirectURI
    );

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    Call<Token> refreshAuthorizedUserToken(
            @Header("Authorization") String credentials,
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken
    );
}
