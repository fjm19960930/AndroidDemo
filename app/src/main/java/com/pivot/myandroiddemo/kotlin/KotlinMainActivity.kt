package com.pivot.myandroiddemo.kotlin

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.pivot.myandroiddemo.R
import com.pivot.myandroiddemo.databinding.ActivityKotlinMainBinding
import kotlinx.android.synthetic.main.activity_kotlin_main.*
import java.util.concurrent.*

class KotlinMainActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityKotlinMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_kotlin_main)
        dataBinding.apply { 
            tv1 = "binding1"
            tv1Click = View.OnClickListener { 
                tv1 = "binding1_click"
            }
        }
        dataBinding.tbHeader.title = "一个标题"
        dataBinding.tbHeader.subtitle = "一个副标题"
        dataBinding.tbHeader.setBackgroundColor(Color.parseColor("#3385FF"))
        
        //使用ToolBar替换系统自带的ActionBar
        setSupportActionBar(tb_header)
        dataBinding.tbHeader.setNavigationIcon(R.drawable.gui_icon_arrow_back)
        //设置导航键的点击事件必须在setSupportActionBar之后，否则不起作用
        dataBinding.tbHeader.setNavigationOnClickListener { 
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

        dataBinding.btnDemoCoroutine.setOnClickListener {
            val i = Intent(this@KotlinMainActivity, CoroutineDemoActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(i)
        }
    }
}