package com.pivot.myandroiddemo.util;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.pivot.myandroiddemo.activity.ProxyActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class HookUtil {

    static final String TARGET_INTENT = "target_intent";

    // 使用代理的Activity替换需要启动的未注册的Activity
    public static void hookAMS() {
        try {
            Class<?> clazz = Class.forName("android.app.ActivityTaskManager");
            Field singletonField = clazz.getDeclaredField("IActivityTaskManagerSingleton");

            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);

            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            Method getMethod = singletonClass.getMethod("get");
            Object mInstance = getMethod.invoke(singleton);

            Class<?> IActivityTaskManagerClass = Class.forName("android.app.IActivityTaskManager");
            Object mInstanceProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{IActivityTaskManagerClass}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if ("startActivity".equals(method.getName())) {
                                int index = -1;
                                for (int i = 0; i < args.length; i++) {// 获取Intent参数在args数组中的下标
                                    if (args[i] instanceof Intent) {
                                        index = i;
                                        break;
                                    }
                                }
                                if (index > -1) {
                                    Intent param = (Intent) args[index];
                                    if (param.getComponent().getClassName().equals("com.demo.plugintest.MainActivity")) {
                                        Intent proxyIntent = new Intent();// 生成代理proxyIntent
                                        // 设置启动注册过的ProxyActivity
                                        proxyIntent.setClassName("com.pivot.myandroiddemo", ProxyActivity.class.getName());
                                        Intent intent = (Intent) args[index];
                                        proxyIntent.putExtra(TARGET_INTENT, intent);// 原始Intent作为参数保存到代理Intent中
                                        args[index] = proxyIntent;// 使用proxyIntent替换数组中的Intent
                                    }
                                }
                            }
                            return method.invoke(mInstance, args);
                        }
                    });
            //用IActivityTaskManager的代理对象去替换系统中的IActivityTaskManager对象
            mInstanceField.set(singleton, mInstanceProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 需要启动的未注册的Activity 替换回来  ProxyActivity
    public static void hookHandler() {
        try {
            Class<?> clazz = Class.forName("android.app.ActivityThread");

            Field activityThreadField = clazz.getDeclaredField("sCurrentActivityThread");
            activityThreadField.setAccessible(true);
            Object activityThread = activityThreadField.get(null);//静态变量直接get就行

            Field mHField = clazz.getDeclaredField("mH");
            mHField.setAccessible(true);
            final Handler mH = (Handler) mHField.get(activityThread);//获取ActivityThread的mH对象

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");//获取mH的mCallback属性
            mCallbackField.setAccessible(true);

            Handler.Callback callback = new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == 159) { //EXECUTE_TRANSACTION
                        // msg.obj = ClientTransaction
                        try {
                            // 获取 List<ClientTransactionItem> mActivityCallbacks 对象
                            Field mActivityCallbacksField = msg.obj.getClass().getDeclaredField("mActivityCallbacks");
                            mActivityCallbacksField.setAccessible(true);
                            List mActivityCallbacks = (List) mActivityCallbacksField.get(msg.obj);
                            for (int i = 0; i < mActivityCallbacks.size(); i++) {
                                // 如果是 LaunchActivityItem，则获取该类中的 mIntent 值，即 proxyIntent
                                if (mActivityCallbacks.get(i).getClass().getName()
                                        .equals("android.app.servertransaction.LaunchActivityItem")) {
                                    Object launchActivityItem = mActivityCallbacks.get(i);
                                    Field mIntentField = launchActivityItem.getClass().getDeclaredField("mIntent");
                                    mIntentField.setAccessible(true);
                                    Intent proxyIntent = (Intent) mIntentField.get(launchActivityItem);
                                    // 获取启动插件的 Intent，并替换回来
                                    Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
                                    if (intent != null) {
                                        mIntentField.set(launchActivityItem, intent);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
            };
            mCallbackField.set(mH, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}