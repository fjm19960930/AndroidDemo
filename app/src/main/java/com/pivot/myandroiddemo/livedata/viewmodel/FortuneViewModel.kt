package com.pivot.myandroiddemo.livedata.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pivot.myandroiddemo.livedata.FortuneService
import com.pivot.myandroiddemo.livedata.bean.DayFortuneBean
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class FortuneViewModel: ViewModel() {
    val mDayFortuneLiveData = MutableLiveData<DayFortuneBean>()

    fun getFortuneData(fortuneData: String) {
        val retrofit = Retrofit.Builder()
                .client(OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor { chain ->
                            val request = chain.request()
                            val t1 = System.nanoTime()
                            Log.e("fjm", "Sending request ${request.url()}-- ${chain.connection()}-- ${request.headers()}")
                            val response = chain.proceed(request)
                            val t2 = System.nanoTime()
                            Log.e("fjm", "Sending request ${response.request().url()}-- ${t2 - t1}-- ${response.headers()}")
                            response
                        }
                        .build())
                .baseUrl("https://web.juhe.cn/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val fortuneService = retrofit.create(FortuneService::class.java)
        val call = fortuneService.getFortuneData(fortuneData, "today", "369093477f666b57bd58f50c531f683e")
        call.enqueue(object : Callback<DayFortuneBean> {
            override fun onResponse(call: Call<DayFortuneBean>, response: Response<DayFortuneBean>) {
                mDayFortuneLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<DayFortuneBean>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}