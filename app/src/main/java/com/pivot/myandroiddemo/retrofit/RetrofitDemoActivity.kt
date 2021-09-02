package com.pivot.myandroiddemo.retrofit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pivot.myandroiddemo.R
import com.pivot.myandroiddemo.entity.NewsReply
import kotlinx.android.synthetic.main.activity_retrofit_demo.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_demo)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)//添加默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true)//设置返回键可用

        val retrofit = Retrofit.Builder()
                .client(OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .build())
                .baseUrl("https://v.juhe.cn/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val newsService = retrofit.create(NewsService::class.java)
        val call = newsService.getNewsData("top", "eb835aee3b2c44168b6aabbc549b9eba")
        call.enqueue(object : Callback<NewsReply> {
            override fun onResponse(call: Call<NewsReply>, response: Response<NewsReply>) {
                val entity = response.body()!!.result
                if (entity.data.isNotEmpty()) {
                    val adapter = NewsRetrofitAdapter(this@RetrofitDemoActivity)
                    adapter.setDatas(entity.data)
                    recycler_view.adapter = adapter
                }
            }

            override fun onFailure(call: Call<NewsReply>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}