package com.vouchr.michelle.vouchrtwitter;

import com.vouchr.michelle.vouchrtwitter.models.SearchResponse;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Michelle Luo on 2018-05-11
 * */

public interface TwitterAPI {
    @POST("oauth2/token?grant_type=client_credentials")
    Call<OAuthToken> authorize(@Header("Authorization") String authorization,
                               @Header("Content-Type") String contentType);

    @GET("1.1/search/tweets.json")
    Call<SearchResponse> search(@Header("Authorization") String token,
                                @Query("q") String search);

    @GET("/1.1/search/tweets.json")
    Call<SearchResponse> page(@Header("Authorization") String token,
                              @Query("q") String search,
                              @Query("max_id") Long maxId,
                              @Query("since_id") Long sinceId);
}
