package com.pivot.myandroiddemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.pivot.chart.chart.THLineChart;
import com.pivot.chart.chart.THPieChart;
import com.pivot.chart.entity.BaseChartViewEntity;
import com.pivot.chart.entity.ChartValueItemEntity;
import com.pivot.chart.view.BaseChartView;
import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseFragment;
import com.pivot.myandroiddemo.base.FragmentParam;
import com.pivot.myandroiddemo.dialog.MySettingDialog;
import com.pivot.myandroiddemo.service.StepCounterService;
import com.pivot.myandroiddemo.util.StepDetector;
import com.pivot.myandroiddemo.util.UserDbUtils;
import com.zcolin.frame.util.SPUtil;
import com.zcolin.gui.ZBanner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 首页Fragment
 */
@FragmentParam(isShowToolBar = true)
public class MainFragment extends BaseFragment {

    private static final int STEP_PROGRESS = 2;//显示步数信息
    private int aimsStep = 6000;//目标步数

    private UserDbUtils dbUtils;
    //    private Thread get_step_thread; // 定义线程对象
    private boolean isThread = true;//子线程标志位
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case STEP_PROGRESS:
//                    if (currentStep != 0) {
//                        StepDetector.CURRENT_STEP = currentStep + StepDetector.CURRENT_STEP;
//                        currentStep = 0;
//                    }
                    if (StepDetector.CURRENT_STEP == 0) {
                        StepDetector.CURRENT_STEP = currentStep;
                    }
                    initPieData(StepDetector.CURRENT_STEP);
                    tvStepRemain.setText("距今日目标还差" + (aimsStep - StepDetector.CURRENT_STEP) + "步");
                    System.out.println("子线程1");
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private TextView tvStepRemain;
    private TextView tvProgress;
    private TextView tvCalorie;
    private TextView tvDistance;
    private int currentStep;
    private ExecutorService executorService;//线程池对象

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getRootViewLayId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onToolBarLeftBtnClick() {
        new MySettingDialog(mActivity, getToolbar().getHeight()).show();
    }

    @Override
    protected void createView(@Nullable Bundle savedInstanceState) {
        super.createView(savedInstanceState);
        setToolbarTitle("我的主页", Color.WHITE);
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setToolbarLeftBtnBackground(R.mipmap.main_icon_mine);

        dbUtils = new UserDbUtils(getContext());
        currentStep = dbUtils.queryTodayStep(SPUtil.getString("user", ""));

//        initPieData(0);
        initLineData();
        initBannerData();

        tvStepRemain = getView(R.id.tv_main_step_remain);
        tvProgress = getView(R.id.tv_main_progress);
        tvCalorie = getView(R.id.tv_main_calorie);
        tvDistance = getView(R.id.tv_main_distance);

        updateView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbUtils.updateTodayStep(SPUtil.getString("user", ""), StepDetector.CURRENT_STEP);
        isThread = false;
//        handler.removeCallbacks(get_step_thread);
//        get_step_thread = null;
        executorService.shutdown();
    }

    /**
     * 初始化步数环形图
     */
    private void initPieData(int step) {
        List<Float> listPieValue = new ArrayList<>();
        List<String> labelList = new ArrayList<>();
        List<String> markerText = new ArrayList<>();
        labelList.add("已完成");
        labelList.add("未完成");
        listPieValue.add((float) step);
        listPieValue.add((float) (aimsStep - step));
        for (int i = 0; i < listPieValue.size(); i++) {
            markerText.add(listPieValue.get(i).intValue() + "步");
        }

        BaseChartView baseChartView = getView(R.id.chart_step_count);
        List<BaseChartViewEntity> chartViewEntities = new ArrayList<>();
        BaseChartViewEntity chartEntity = new BaseChartViewEntity();
        chartEntity.chart = THPieChart.instance(new PieChart(mActivity))
                .setListPieValue(listPieValue, null)//设置饼图数据 必要
                .setCircleLabelList(labelList)//设置条目及图例名称列表 必要
                .setShowLegend(false)
                .setCustomText(true)
                .setHoleRadius(72f)
                .setInitAnimation(false)
                .setMarkerText(markerText)//设置自定义弹出框文本集合，isCustomText为true该属性才生效
                .setExtraOffset(0, 0, 6, 0)//设置饼图偏移 非必要
                .setCenterText("今日" + listPieValue.get(0).intValue() + "步")//设置中心文本 非必要
                .setListColor(Arrays.asList(Color.parseColor("#499DD7"), Color.parseColor("#c3c3c3")))
                .effect();//最后调用此方法使所有设置生效 必要
        chartViewEntities.add(chartEntity);
        baseChartView.clear();
        baseChartView.initData(chartViewEntities);

        DecimalFormat f = new DecimalFormat("####.##");
        tvProgress.setText(f.format(((double) step) / aimsStep * 100) + "%");
        tvCalorie.setText(f.format(step * 0.7 * 0.001 * 70 * 1.036) + "");
        tvDistance.setText(f.format(step * 0.7 * 0.001) + "");
    }

    /**
     * 初始化线图数据
     */
    private void initLineData() {
        List<ChartValueItemEntity> datas = new ArrayList<>();
        List<String> xLabelList = new ArrayList<>();
        List<Float> listValue = new ArrayList<>();
        xLabelList.add("04-19");
        xLabelList.add("04-20");
        xLabelList.add("04-21");
        xLabelList.add("04-22");
        xLabelList.add("04-23");
        listValue.add(6362f);
        listValue.add(9091f);
        listValue.add(5229f);
        listValue.add(11933f);
        listValue.add(10542f);
        datas.add(new ChartValueItemEntity(listValue, new DefaultValueFormatter(0), Color.BLUE, 12, true, "", R.drawable.line_fill_color));

        BaseChartView baseChartView = getView(R.id.chart_main_line);
        List<BaseChartViewEntity> chartViewEntities = new ArrayList<>();
        BaseChartViewEntity chartEntity = new BaseChartViewEntity();
        chartEntity.chart = THLineChart.instance(new LineChart(mActivity))
                .setListLineValue(datas)//设置线图数据 必要
                .setListLabel(xLabelList)//设置x轴坐标文本 必要
                .setTextColor(Color.parseColor("#3385FF"))//设置x、y轴以及图例文本字体颜色 必要
                .setTitle("近期步数变化")//设置图表标题 非必要
                .setTitleTextColor(Color.parseColor("#3385FF"))
                .setTitleTextSize(18)//设置标题文本字体大小 非必要
                .setLabelTextSize(10)//设置x、y轴文本字体大小 非必要
                .setxLabelNum(8)//设置x轴标签超过一定数量就设为可滑动 非必要
                .setMode(LineDataSet.Mode.CUBIC_BEZIER)//设置线图模式，包括折线、曲线、阶梯、波浪,默认为折线 非必要
                .setTouchEnable(true)//设置整张图表是否可点击 非必要
                .setShowAxisLeft(false)
                .setShowCircle(true)//设置是否绘制圆点 非必要
                .setShowLegend(false)//设置是否显示图例 非必要
                .setShowMarkerView(false)//设置点击条目是否显示提示框 非必要
                .setMarkerDigit(0)//设置提示框保留的小数位数 非必要
                .effect();//最后调用此方法使所有设置生效 必要

        chartViewEntities.add(chartEntity);
        baseChartView.clear();
        baseChartView.initData(chartViewEntities);
    }

    /**
     * 初始化轮播图数据
     */
    private void initBannerData() {
        List<Integer> listUrl = new ArrayList<>();
        listUrl.add(R.mipmap.main_img_banner1);
        listUrl.add(R.mipmap.main_img_banner2);
        listUrl.add(R.mipmap.main_img_banner3);
        listUrl.add(R.mipmap.main_img_banner4);
        listUrl.add(R.mipmap.main_img_banner5);
        ZBanner banner = getView(R.id.main_banner);
        banner.setBannerStyle(ZBanner.CIRCLE_INDICATOR)
                .setIndicatorGravity(ZBanner.CENTER)
                .setDelayTime(4000)
                .setImages(listUrl)
                .startAutoPlay();
    }

    private void updateView() {
//        if (get_step_thread == null) {
//            ExecutorService executorService = Executors.newFixedThreadPool(5);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        executorService = new ThreadPoolExecutor(5, 5, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);
//            get_step_thread = new Thread() {// 子线程用于监听当前步数的变化
//                @Override
//                public void run() {
//                    super.run();
//                    while (isThread) {
//                        try {
////                            Log.e("进入子线程","进入方法体！");
//                            Thread.sleep(1000);//每个一秒发送一条信息给UI线程
//                            if (StepCounterService.FLAG) {
//                                handler.sendEmptyMessage(STEP_PROGRESS);// 通知主线程
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                        
//                }
//            };
        executorService.execute(new Runnable() {
            @Override
            public void run() {
//                    super.run();
                while (isThread) {
                    try {
                        Thread.sleep(1000);//每隔一秒发送一条信息给UI线程
                        if (StepCounterService.FLAG) {
                            handler.sendEmptyMessage(STEP_PROGRESS);// 通知主线程
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//            get_step_thread.start();
//        }
    }
}
