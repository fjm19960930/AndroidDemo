package com.pivot.myandroiddemo.retrofit;

import com.pivot.myandroiddemo.entity.NewsReply;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("toutiao/index?")
    Call<NewsReply> getNewsData(@Query("type") String type, @Query("key") String key);
}
