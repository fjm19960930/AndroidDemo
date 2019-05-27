package com.pivot.myandroiddemo.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.activity.CameraActivity;
import com.pivot.myandroiddemo.base.BaseFragment;
import com.pivot.myandroiddemo.base.ZClick;
import com.zcolin.frame.util.ActivityUtil;
import com.zcolin.gui.ZSpinner;

import java.util.Arrays;

/**
 * @author ASUS
 */
public class VideoFragment extends BaseFragment {
    
    public static VideoFragment newInstance() {
        Bundle args = new Bundle();
        VideoFragment fragment = new VideoFragment();
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
        return R.layout.fragment_video;
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void createView(@Nullable Bundle savedInstanceState) {
        super.createView(savedInstanceState);

        Button btnXx = getView(R.id.btnXiaXian);
        Button btnCamera = getView(R.id.btnCamera);
        Button btnAddress = getView(R.id.btnAddress);
        Button btnGlide = getView(R.id.btn_glide);
        ImageView ivGlideDemo = getView(R.id.iv_glide_demo);
        ZSpinner zspinner = getView(R.id.z_spinner);
        zspinner.initData(Arrays.asList("sdas", "123"), "123");
        btnXx.setOnClickListener(v -> {//点击之后强制下线，重新登录
            Intent intent = new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
            mActivity.sendBroadcast(intent);
        });
//        btnCamera.setOnClickListener(v -> ActivityUtil.startActivity(mActivity, CameraActivity.class));
        btnAddress.setOnClickListener(v -> readContacts());
        
        btnGlide.setOnClickListener(v -> {
            //Glide使用示例
//            String url = "http://p1.pstatp.com/large/166200019850062839d3";//动图
            String url = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg";
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.ic_loading)
                    .error(R.mipmap.ic_warn)//加载错误时显示
                    .fallback(R.mipmap.main_img_banner3)//当url为空时显示
                    .override(800, 600);//指定图片size
//                    .diskCacheStrategy(DiskCacheStrategy.NONE);//禁用缓存
            Glide.with(mActivity)
                    .load(url)
                    .apply(options)
                    .into(ivGlideDemo);
        });
    }

    /**
     * 以注解的方式设置按钮的点击响应
     */
    @ZClick(R.id.btnCamera)
    private void btnCameraClick() {
        ActivityUtil.startActivity(mActivity, CameraActivity.class);
    }

    /**
     * 读取手机通讯录
     */
    private void readContacts() {
        Cursor cursor = null;
        try {
            cursor = mActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    System.out.println(name + ":" + number);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
