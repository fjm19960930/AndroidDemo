package com.pivot.myandroiddemo.kotlin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import com.pivot.myandroiddemo.R
import kotlinx.android.synthetic.main.activity_kotlin_main.*
import java.util.concurrent.*

class KotlinMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_main)
        tb_header.title = "一个标题"
        tb_header.subtitle = "一个副标题"
        tb_header.setBackgroundColor(Color.parseColor("#3385FF"))
        
        //使用ToolBar替换系统自带的ActionBar
        setSupportActionBar(tb_header)
        tb_header.setNavigationIcon(R.drawable.gui_icon_arrow_back)
        //设置导航键的点击事件必须在setSupportActionBar之后，否则不起作用
        tb_header.setNavigationOnClickListener { 
            finish()
        }
        
//        var command = Runnable { 
//            SystemClock.setCurrentTimeMillis(2000)
//        }
//        var fixedThreadPool: ExecutorService = Executors.newFixedThreadPool(4)
//        fixedThreadPool.execute(command)
//        
//        var cachedThreadPool: ExecutorService = Executors.newCachedThreadPool()
//        cachedThreadPool.execute(command)
//
//        var scheduledThreadPool: ScheduledExecutorService = Executors.newScheduledThreadPool(4)
//        //每隔1000ms执行一次
//        scheduledThreadPool.schedule(command, 1000, TimeUnit.MILLISECONDS)
//        //延时10ms后每隔1000ms执行一次
//        scheduledThreadPool.scheduleAtFixedRate(command, 10, 1000, TimeUnit.MILLISECONDS)
//
//        var singleThreadExecutor: ExecutorService = Executors.newSingleThreadExecutor()
//        singleThreadExecutor.execute(command)
        
        btn_demo_coroutine.setOnClickListener {
            val i = Intent(this@KotlinMainActivity, CoroutineDemoActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(i)
        }
    }
}