package com.pivot.myandroiddemo.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;

import com.pivot.myandroiddemo.IMyAidl;
import com.pivot.myandroiddemo.Person;
import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.base.ZClick;

import java.util.List;
import java.util.Random;

/**
 * aidlDemo 客户端
 */
public class MyAidlActivity extends BaseActivity {
    private IMyAidl mAidl;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接后拿到 Binder，转换成 AIDL，在不同进程会返回个代理
            mAidl = IMyAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
        }
    };
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_aidl);

        mTvResult = findViewById(R.id.tv_aidl);
        
        Intent intent1 = new Intent(getApplicationContext(), MyAidlService.class);
        bindService(intent1, mConnection, BIND_AUTO_CREATE);
    }
    
    @ZClick(R.id.btn_aidl)
    private void addPerson() {
        Random random = new Random();
        Person person = new Person(random.nextInt(10) + "");

        try {
            mAidl.addPerson(person);
            List<Person> personList = mAidl.getPersonList();
            mTvResult.setText(personList.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
