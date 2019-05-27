package com.pivot.myandroiddemo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.pivot.myandroiddemo.receiver.ClearStepReceiver;
import com.pivot.myandroiddemo.util.StepDetector;
import com.pivot.myandroiddemo.util.UserDbUtils;
import com.zcolin.frame.util.SPUtil;

import java.util.Calendar;
import java.util.TimeZone;

public class StepCounterService extends Service {
    public static final String alarmSaveService = "mrkj.healthylife.SETALARM";
    private static final String TAG = "StepCounterService";
    public static Boolean FLAG = false;// 服务运行标志

    private SensorManager mSensorManager;// 传感器服务
    public StepDetector detector;// 传感器监听对象

    private PowerManager mPowerManager;// 电源管理服务
    private PowerManager.WakeLock mWakeLock;// 屏幕灯
    private AlarmManager alarmManager;//闹钟管理器
    private PendingIntent pendingIntent;//延迟意图
    private Calendar calendar;//日期
    private Intent intent;//意图
    
    private UserDbUtils dbUtils;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("StepCounterService后台服务开始");
        FLAG = true;// 标记为服务正在运行

        // 创建监听器类，实例化监听对象
        detector = new StepDetector(this);//实例化传感器对象
        detector.walk = 1;//设置步数从一开始
        // 获取传感器的服务，初始化传感器
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        // 注册传感器，注册监听器
        mSensorManager.registerListener(detector,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        // 电源管理服务
        mPowerManager = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
        //保持设备状态
        mWakeLock.acquire();


        //设置一个定时服务
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);//时
        calendar.set(Calendar.MINUTE, 59);//分
        calendar.set(Calendar.SECOND, 59);//秒
        calendar.set(Calendar.MILLISECOND, 0);//毫秒
        intent = new Intent(this, ClearStepReceiver.class);//发送广播的意图
        intent.setAction(alarmSaveService);//设置Action
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //设置定时器
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //	1.
    // START_NOT_STICKY：当Service因为内存不足而被系统kill后，
    // 接下来未来的某个时间内，即使系统内存足够可用，系统也不会尝试重新创建此Service。
    // 除非程序中Client明确再次调用startService(...)启动此Service。
    //
    //	2.
    // START_STICKY：当Service因为内存不足而被系统kill后，接下来未来的某个时间内，
    // 当系统内存足够可用的情况下，系统将会尝试重新创建此Service，一旦创建成功后将回调onStartCommand(...)方法
    // ，但其中的Intent将是null，pendingintent除外。
    //
    //	3.
    // START_REDELIVER_INTENT：与START_STICKY唯一不同的是，回调onStartCommand(...)方法时，
    // 其中的Intent将是非空，将是最后一次调用startService(...)中的intent。
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FLAG = false;// 服务停止
        dbUtils = new UserDbUtils(getBaseContext());
        dbUtils.updateTodayStep(SPUtil.getString("user", ""), StepDetector.CURRENT_STEP);
        Log.e(TAG, "后台服务停止");
        if (detector != null) {
            //取消对所有传感器的监听
            mSensorManager.unregisterListener(detector);
        }

        if (mWakeLock != null) {
            //释放唤醒资源
            mWakeLock.release();
        }
    }
}
