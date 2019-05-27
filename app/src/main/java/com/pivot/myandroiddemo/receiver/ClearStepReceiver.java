package com.pivot.myandroiddemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pivot.myandroiddemo.service.StepCounterService;
import com.pivot.myandroiddemo.util.UserDbUtils;
import com.zcolin.frame.util.SPUtil;

public class ClearStepReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(StepCounterService.alarmSaveService)){//每天23时59分59秒清空步数
            UserDbUtils dbUtils = new UserDbUtils(context);
            dbUtils.updateTodayStep(SPUtil.getString("user", ""), 0);
        }
    }
}
