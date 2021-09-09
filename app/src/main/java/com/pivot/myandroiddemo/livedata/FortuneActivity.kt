package com.pivot.myandroiddemo.livedata

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.pivot.chart.chart.THBarChart
import com.pivot.chart.entity.BaseChartViewEntity
import com.pivot.chart.entity.ChartValueItemEntity
import com.pivot.myandroiddemo.R
import com.pivot.myandroiddemo.databinding.ActivityFortuneBinding
import com.pivot.myandroiddemo.livedata.adapter.PopupAdapter
import com.pivot.myandroiddemo.livedata.viewmodel.FortuneViewModel
import kotlinx.android.synthetic.main.activity_kotlin_main.*

class FortuneActivity : AppCompatActivity() {
    private lateinit var mDataBinding: ActivityFortuneBinding
    private lateinit var mFortuneViewModel: FortuneViewModel
    private lateinit var mPopWindow: PopupWindow
    private  var mIsPopWindowShow: Boolean = false
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_fortune)

        mDataBinding.fortuneToolbar.title = "LiveData"
        mDataBinding.fortuneToolbar.setBackgroundColor(Color.parseColor("#3385FF"))
        //使用ToolBar替换系统自带的ActionBar
        setSupportActionBar(tb_header)
        mDataBinding.fortuneToolbar.setNavigationIcon(R.drawable.gui_icon_arrow_back)
        //设置导航键的点击事件必须在setSupportActionBar之后，否则不起作用
        mDataBinding.fortuneToolbar.setNavigationOnClickListener {
            finish()
        }

        mFortuneViewModel = ViewModelProvider(this).get(FortuneViewModel::class.java)
        subScribe()
        
        mDataBinding.fortuneName.setOnClickListener { 
            if (mIsPopWindowShow) {
                mPopWindow.dismiss()
                mIsPopWindowShow = false
                rotate(mDataBinding.fortuneIcon, 180f, 0f)
            } else {
                showPopupView()
                mIsPopWindowShow = true
                rotate(mDataBinding.fortuneIcon, 0f, 180f)
            }
        }
        mDataBinding.fortuneFind.setOnClickListener {
            if (mIsPopWindowShow) {
                mPopWindow.dismiss()
                mIsPopWindowShow = false
                rotate(mDataBinding.fortuneIcon, 180f, 0f)
            }
            mDataBinding.fortuneProgress.visibility = View.VISIBLE
            mFortuneViewModel.getFortuneData(mDataBinding.fortuneName.text.toString())
        }
    }
    
    private fun showPopupView() {
        val contentView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        mPopWindow = PopupWindow(contentView);
        mPopWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mPopWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.popup_recycler_view)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        val data = mutableListOf("摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", 
                "狮子座", "处女座", "天蝎座", "天秤座", "射手座")
        val popupAdapter = PopupAdapter(this)
        popupAdapter.setData(data)
        popupAdapter.setItemClickListener { 
            mDataBinding.fortuneName.text = data[it]
            mPopWindow.dismiss()
            mIsPopWindowShow = false
            rotate(mDataBinding.fortuneIcon, 180f, 0f)
        }
        recyclerView.adapter = popupAdapter
        mPopWindow.showAsDropDown(mDataBinding.fortuneName, 0 ,20)
    }
    
    private fun subScribe() {
        mFortuneViewModel.mDayFortuneLiveData.observe(this, { entity ->
            mDataBinding.fortuneProgress.visibility = View.GONE
            mDataBinding.fortuneColor.text = "幸运色：${entity?.color}"
            mDataBinding.fortuneNumber.text = "幸运数字：${entity?.number.toString()}"
            mDataBinding.fortuneConstellation.text = "速配星座：${entity?.QFriend}"
            mDataBinding.fortuneSummarize.text = "概述：${entity?.summary}"

            //柱状图数据初始化
            val data: MutableList<ChartValueItemEntity> = ArrayList()
            data.add(ChartValueItemEntity(mutableListOf(entity?.all?.toFloat(), entity?.love?.toFloat(),
                    entity?.work?.toFloat(), entity?.health?.toFloat(), entity?.money?.toFloat()),
                    DefaultValueFormatter(0), 0, 12, "幸运指数", true))
            val chartEntity = BaseChartViewEntity()
            chartEntity.chart = THBarChart.instance(BarChart(this@FortuneActivity))
                    .setListBarValue(data)
                    .setListLabel(mutableListOf("综合", "爱情", "事业", "健康", "财运"))
                    .setTextColor(Color.BLACK)
                    .effect()
            mDataBinding.fortuneChart.clear()
            mDataBinding.fortuneChart.initData(mutableListOf(chartEntity))
        })
    }

    private fun rotate(imageView: ImageView, startOf: Float, endOf: Float) {
        val anim = ObjectAnimator.ofFloat(imageView, "rotation", startOf, endOf)
        if (anim.isRunning) {
            anim.end()
        }
        anim.duration = 300
        anim.start()
    }
}