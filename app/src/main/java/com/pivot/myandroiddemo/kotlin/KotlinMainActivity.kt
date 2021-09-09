package com.pivot.myandroiddemo.kotlin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pivot.myandroiddemo.R
import com.pivot.myandroiddemo.databinding.ActivityKotlinMainBinding
import com.pivot.myandroiddemo.livedata.FortuneActivity
import com.pivot.myandroiddemo.retrofit.RetrofitDemoActivity
import kotlinx.android.synthetic.main.activity_kotlin_main.*
import java.util.*
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
//            getNetTime()
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

        dataBinding.btnRetrofitTest.setOnClickListener {
            val i = Intent(this@KotlinMainActivity, RetrofitDemoActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(i)
        }

        dataBinding.btnRetrofitTest.setOnClickListener {
            val i = Intent(this@KotlinMainActivity, RetrofitDemoActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(i)
        }
        
        dataBinding.btnLiveData.setOnClickListener {
            val i = Intent(this@KotlinMainActivity, FortuneActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(i)
        }
    }

//    private fun getNetTime() {
//        var c = Calendar.getInstance();
//        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        var mYear = c.get(Calendar.YEAR);//年
//        var mMonth = c.get(Calendar.MONTH) + 1;//月
//        var mDay = c.get(Calendar.DAY_OF_MONTH);//日
//        var mHour = c.get(Calendar.HOUR_OF_DAY);//24小时格式    HOUR(12小时格式)
//        var  mMinute = c.get(Calendar.MINUTE);//分
//        var  mSecond = c.get(Calendar.SECOND);//秒
//        var time="$mYear-$mMonth-$mDay $mHour:$mMinute:$mSecond"
//        Toast.makeText(this, "当前网络时间为: \n$time", Toast.LENGTH_SHORT).show()
//    }
}