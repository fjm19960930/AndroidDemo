package com.pivot.myandroiddemo.livedata

import com.pivot.myandroiddemo.livedata.bean.DayFortuneBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FortuneService {
    @GET("constellation/getAll?")
    fun getFortuneData(@Query("consName") consName: String?, @Query("type") dateType: String?, @Query("key") key: String?): Call<DayFortuneBean>
}