package com.pivot.myandroiddemo.application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.pivot.myandroiddemo.util.ConstUtil;
import com.pivot.myandroiddemo.util.HookUtil;
import com.pivot.myandroiddemo.util.PluginUtils;
import com.zcolin.frame.app.BaseApp;

import java.util.Objects;


/**
 * Created by ASUS on 2019/4/18.
 */

public class MainApplication extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext   
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        try {//将插件的apk从asset目录下写入到缓存路径中
            ConstUtil.copyAssetAndWrite(Objects.requireNonNull(this), "plugintest-debug.apk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        PluginUtils.INSTANCE.loadClass(this);
        HookUtil.hookAMS();
        HookUtil.hookHandler();
//        PluginUtils.INSTANCE.hookAMS();
//        PluginUtils.INSTANCE.ss();
    }

}
