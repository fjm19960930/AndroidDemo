package com.pivot.myandroiddemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.pivot.myandroiddemo.service.StepCounterService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
//            Intent i = new Intent(context, LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
            Intent service = new Intent(context, StepCounterService.class);
            context.startService(service);
            Toast.makeText(context, "系统已开启QQQQQqweqwwewewqe", Toast.LENGTH_LONG).show();
        }
    }
}
