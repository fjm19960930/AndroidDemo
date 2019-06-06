package com.pivot.myandroiddemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.TypedValue;

import com.pivot.myandroiddemo.adpter.ChildPagerAdapter;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.fragment.MainFragment;
import com.pivot.myandroiddemo.fragment.MapFragment;
import com.pivot.myandroiddemo.fragment.NewsFragment;
import com.pivot.myandroiddemo.fragment.VideoFragment;
import com.pivot.myandroiddemo.service.StepCounterService;
import com.pivot.myandroiddemo.util.ScaleInOutTransformer;
import com.pivot.myandroiddemo.util.SystemUtils;
import com.zcolin.frame.permission.PermissionHelper;
import com.zcolin.frame.permission.PermissionsResultAction;
import com.zcolin.frame.util.AppUtil;
import com.zcolin.frame.util.DisplayUtil;
import com.zcolin.frame.util.SPUtil;
import com.zcolin.frame.util.ToastUtil;
import com.zcolin.gui.ZTabView;
import com.zcolin.gui.ZViewPager;

import java.util.ArrayList;

/**
 * @author ASUS
 */
@ActivityParam(isShowToolBar = false, isImmerse = false)
public class MainActivity extends BaseActivity {

    private boolean isExit;                             //双击退出程序标识
    private ZViewPager mViewPager;
    private ZTabView mTabView;
    private ArrayList<Fragment> listFragment = new ArrayList<>();
    private Intent step_service;//计步服务
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SPUtil.putString("user", getIntent().getStringExtra("username"));
        permissionApply();

        mViewPager = findViewById(R.id.view_pager);
        mTabView = findViewById(R.id.view_tab);
        listFragment.add(MainFragment.newInstance());
        listFragment.add(MapFragment.newInstance());
        listFragment.add(NewsFragment.newInstance());
        listFragment.add(VideoFragment.newInstance());
        initView();

        ToastUtil.toastLong(SystemUtils.isServiceRunning(mActivity, "com.pivot.myandroiddemo.service.StepCounterService")+"");
        if (!SystemUtils.isServiceRunning(mActivity, "com.pivot.myandroiddemo.service.StepCounterService")) {
            step_service = new Intent(mActivity, StepCounterService.class);
            mActivity.startService(step_service);
        }
    }
    
    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    private void initView() {
        mViewPager.setOffscreenPageLimit(4);//ViewPager预加载页面的数量不设置默认是0（0和1的效果一样）
        mViewPager.setCanScroll(false);//禁止滑动
        mViewPager.setPageTransformer(true, new ScaleInOutTransformer());
        ChildPagerAdapter childPagerAdapter = new ChildPagerAdapter(getSupportFragmentManager(), listFragment, null);
        mViewPager.setAdapter(childPagerAdapter);

        mTabView.addZTab(getNewTab("首页", R.mipmap.tab_icon_main_normal, R.mipmap.tab_icon_main_high));
        mTabView.addZTab(getNewTab("地图", R.mipmap.tab_icon_map_normal, R.mipmap.tab_icon_map_high));
        mTabView.addZTab(getNewTab("新闻", R.mipmap.tab_icon_news_normal, R.mipmap.tab_icon_news_high));
        mTabView.addZTab(getNewTab("测试", R.mipmap.tab_icon_video_normal, R.mipmap.tab_icon_video_high));
        
        mTabView.addZTabListener((arg0, index) -> {
            mTabView.selectTab(index);
            mViewPager.setCurrentItem(index, false);//false则不允许滑动跳转只能点击tab按钮进行跳转
            return false;
        });
    }

    /**
     * 初始化单个tab按钮
     */
    private ZTabView.ZTab getNewTab(String str, int icon, int iconSelected) {
        float textSize = getResources().getDimension(R.dimen.text_size_micro);
        ZTabView.ZTab tab = mTabView.getNewIconTab(icon, iconSelected, str);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tab.setTextColor(getResources().getColorStateList(R.color.main_tab_text_color_selector));
        int padding = DisplayUtil.dip2px(this, 3);
        tab.setPadding(padding, padding, padding, padding);
        return tab;
    }

    /**
     * 初始化权限请求
     */
    private void permissionApply() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, 
                Manifest.permission.ACCESS_COARSE_LOCATION, 
                Manifest.permission.WRITE_EXTERNAL_STORAGE, 
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA};
        PermissionHelper.requestPermission(mActivity, permissions, new PermissionsResultAction() {
            @Override
            public void onGranted() {}//权限请求成功后的操作

            @Override
            public void onDenied(String permission) {}//权限请求失败后的操作
        });
    }

    /**
     * 双击退出
     */
    private void exitBy2Click() {
        if (!isExit) {
            isExit = true; // 准备退出
            ToastUtil.toastShort("再按一次退出应用");
            new Handler().postDelayed(() -> {
                isExit = false; // 取消退出
            }, 3000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            AppUtil.quitSystem();
        }
    }
}
