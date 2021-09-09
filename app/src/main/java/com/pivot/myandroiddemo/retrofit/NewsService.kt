package com.pivot.myandroiddemo.retrofit

import com.pivot.myandroiddemo.entity.NewsReply
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("toutiao/index?")
    fun getNewsData(@Query("type") type: String?, @Query("key") key: String?): Call<NewsReply>
}