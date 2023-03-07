package com.pivot.myandroiddemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.LinearLayout;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.activity.TextInputDemoActivity;
import com.pivot.myandroiddemo.aidldemo.MyAidlActivity;
import com.pivot.myandroiddemo.base.BaseFragment;
import com.pivot.myandroiddemo.kotlin.KotlinMainActivity;
import com.pivot.myandroiddemo.scrollbar.ScrollBarActivity;
import com.pivot.myandroiddemo.test.bezier.BezierActivity;
import com.pivot.myandroiddemo.test.demoanimation.AnimationDemoActivity;
import com.pivot.myandroiddemo.test.demodraw.DrawDemoActivity;
import com.pivot.myandroiddemo.test.demoview.ViewDemoActivity;
import com.pivot.myandroiddemo.test.evaluator.EvaluatorActivity;
import com.pivot.myandroiddemo.test.eventbus.EventBusFirstActivity;
import com.pivot.myandroiddemo.test.indexside.IndexSideActivity;
import com.pivot.myandroiddemo.test.recyclerview.RecyclerDemoActivity;
import com.pivot.myandroiddemo.test.redpoint.RedPointActivity;
import com.pivot.myandroiddemo.test.rx.RxJavaDemoActivity;
import com.pivot.myandroiddemo.test.shimmertext.ShimmerTextView;
import com.pivot.myandroiddemo.test.tablelayout.TabLayoutDemoActivity;
import com.pivot.myandroiddemo.util.ConstUtil;
import com.pivot.myandroiddemo.util.PluginUtils;
import com.zcolin.frame.util.ToastUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author ASUS
 */
public class TestFragment extends BaseFragment {

    private volatile static TestFragment instance = null;

    private LinearLayout rootLayout;
    private ShimmerTextView tv;

    public static TestFragment newInstance() {
        if (instance == null) {
            synchronized (TestFragment.class) {
                if (instance == null) {
                    Bundle args = new Bundle();
                    instance = new TestFragment();
                    instance.setArguments(args);
                    return instance;
                }
            }
        }
        return instance;
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
        return R.layout.fragment_video;
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {
    }
    
    public void setTv() {
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tv.setText("123");
    }
    
    @Override
    protected void createView(@Nullable Bundle savedInstanceState) {
        super.createView(savedInstanceState);

        rootLayout = getView(R.id.test_root);
        tv = getView(R.id.tv);
        getView(R.id.btn_test_indexSide).setOnClickListener(v -> startActivity(new Intent(mActivity, IndexSideActivity.class)));
        getView(R.id.btn_test_bezier).setOnClickListener(v -> startActivity(new Intent(mActivity, BezierActivity.class)));
        getView(R.id.btn_test_evaluator).setOnClickListener(v -> startActivity(new Intent(mActivity, EvaluatorActivity.class)));
        getView(R.id.btn_test_red_point).setOnClickListener(v -> startActivity(new Intent(mActivity, RedPointActivity.class)));
        getView(R.id.btn_recycler).setOnClickListener(v -> startActivity(new Intent(mActivity, RecyclerDemoActivity.class)));
        getView(R.id.btn_ripper).setOnClickListener(v -> showSnackbar());
//        getView(R.id.btn_ripper).setOnClickListener(v -> setTv());
        getView(R.id.btn_exit).setOnClickListener(v -> {//点击之后强制下线，重新登录
            Intent intent = new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
            mActivity.sendBroadcast(intent);
        });
        getView(R.id.btn_text_input).setOnClickListener(v -> startActivity(new Intent(mActivity, TextInputDemoActivity.class)));
        getView(R.id.btn_tab_layout).setOnClickListener(v -> startActivity(new Intent(mActivity, TabLayoutDemoActivity.class)));
        getView(R.id.btn_aidl).setOnClickListener(v -> startActivity(new Intent(mActivity, MyAidlActivity.class)));
        getView(R.id.btn_eventbus).setOnClickListener(v -> startActivity(new Intent(mActivity, EventBusFirstActivity.class)));
        getView(R.id.btn_animation_test).setOnClickListener(v -> startActivity(new Intent(mActivity, AnimationDemoActivity.class)));
        getView(R.id.btn_draw_test).setOnClickListener(v -> startActivity(new Intent(mActivity, DrawDemoActivity.class)));
        getView(R.id.btn_view_test).setOnClickListener(v -> startActivity(new Intent(mActivity, ViewDemoActivity.class)));
        getView(R.id.btn_scroll_bar_test).setOnClickListener(v -> startActivity(new Intent(mActivity, ScrollBarActivity.class)));
        getView(R.id.btn_kotlin_test).setOnClickListener(v -> startActivity(new Intent(mActivity, KotlinMainActivity.class)));
        getView(R.id.btn_rx_java_test).setOnClickListener(v -> startActivity(new Intent(mActivity, RxJavaDemoActivity.class)));
        getView(R.id.btn_plugin).setOnClickListener(v -> {
            try {
                Class<?> pluginClazz = Class.forName("com.demo.plugintest.Test");
                Constructor constructor = pluginClazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();//因为kotlin的object是单例模式，所以不先实例化是会报错的
                Method show = pluginClazz.getDeclaredMethod("showSomething", Context.class);
                show.invoke(instance, getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
//        Button btnCamera = getView(R.id.btnCamera);
//        Button btnAddress = getView(R.id.btnAddress);
//        Button btnGlide = getView(R.id.btn_glide);
//        ImageView ivGlideDemo = getView(R.id.iv_glide_demo);
//        ZSpinner zspinner = getView(R.id.z_spinner);
//        zspinner.initData(Arrays.asList("sdas", "123"), "123");
////        btnCamera.setOnClickListener(v -> ActivityUtil.startActivity(mActivity, CameraActivity.class));
//        btnAddress.setOnClickListener(v -> readContacts());
//        
//        btnGlide.setOnClickListener(v -> {
//            //Glide使用示例
////            String url = "http://p1.pstatp.com/large/166200019850062839d3";//动图
//            String url = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg";
//            RequestOptions options = new RequestOptions()
//                    .placeholder(R.mipmap.ic_loading)
//                    .error(R.mipmap.ic_warn)//加载错误时显示
//                    .fallback(R.mipmap.main_img_banner3)//当url为空时显示
//                    .override(800, 600);//指定图片size
////                    .diskCacheStrategy(DiskCacheStrategy.NONE);//禁用缓存
//            Glide.with(mActivity)
//                    .load(url)
//                    .apply(options)
//                    .into(ivGlideDemo);
//        });
    }

    private void showSnackbar() {
        Snackbar.make(rootLayout, "标题", Snackbar.LENGTH_LONG).setAction("点击事件", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toastLong("123");
            }
        }).setDuration(Snackbar.LENGTH_LONG).show();
    }
    /**
     * 以注解的方式设置按钮的点击响应
     */
//    @ZClick(R.id.btnCamera)
//    private void btnCameraClick() {
//        ActivityUtil.startActivity(mActivity, CameraActivity.class);
//    }

    /**
     * 读取手机通讯录
     */
//    private void readContacts() {
//        Cursor cursor = null;
//        try {
//            cursor = mActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    System.out.println(name + ":" + number);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }
}
