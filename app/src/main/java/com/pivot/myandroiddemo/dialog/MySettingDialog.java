package com.pivot.myandroiddemo.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.activity.MineDetailsActivity;
import com.pivot.myandroiddemo.adpter.WeatherListAdapter;
import com.pivot.myandroiddemo.biz.WeatherMgr;
import com.pivot.myandroiddemo.entity.WeatherFutureEntity;
import com.zcolin.frame.util.SPUtil;
import com.zcolin.frame.util.ScreenUtil;
import com.zcolin.gui.ZDialog;

/**
 * 首页左侧弹出设置列表
 */
public class MySettingDialog extends ZDialog {

    private int barHeight = 0;
    private Activity activity;
    private Intent mIntent;
    private WeatherListAdapter weatherListAdapter;//天气列表适配器
    private String city;

    public MySettingDialog(Context context) {
        this(context, 0);
    }

    public MySettingDialog(Context context, int height) {
        super(context, R.layout.main_my_setting_dialog, R.style.style_dialog);
        this.activity = (Activity) context;
        this.mIntent = ((Activity) context).getIntent();
        this.weatherListAdapter = new WeatherListAdapter(activity);
//        if (SystemUtils.checkDeviceHasNavigationBar(activity)) {//判断手机有无虚拟Home栏
//            this.barHeight = (height + 36) * 2 + 1;
//        } else {
//            this.barHeight = (height + 36) * 2 + 1 - SystemUtils.getVirtualBarHeigh(activity);
//        }
        initDialog();
    }

    private void initDialog() {
        setGravity(Gravity.LEFT);
        setAnim(R.style.style_dialog_left_to_right);
        setDialogBackground(R.color.white);
        setLayout(ScreenUtil.getScreenWidth(getContext()) / 3 * 2, ScreenUtil.getScreenHeight(activity) - barHeight);

        ImageView ivUserHead = findViewById(R.id.iv_main_setting_user_head);
        TextView tvUserName = findViewById(R.id.tv_main_setting_username);
        TextView tvUserDesc = findViewById(R.id.tv_main_setting_user_desc);
        TextView tvCity = findViewById(R.id.tv_main_setting_city);
        tvUserName.setText(mIntent.getStringExtra("username"));
        if (SPUtil.contains("headImgUrl")) {//设置头像
            String headImgUrl = SPUtil.getString("headImgUrl", "");
            Bitmap bitmap = BitmapFactory.decodeFile(headImgUrl);
            RoundedBitmapDrawable shopDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), bitmap);//构造圆形图片
            shopDrawable.setCornerRadius(Integer.MAX_VALUE);
            shopDrawable.setCircular(true);
            ivUserHead.setBackgroundDrawable(shopDrawable);
        }
        ivUserHead.setOnClickListener(v -> {//跳转至个人信息主页
            Intent intent = new Intent(activity, MineDetailsActivity.class);
            intent.putExtra("username", tvUserName.getText());
            intent.putExtra("userDesc", tvUserDesc.getText());
            activity.startActivity(intent);
            dismiss();
        });
        requestWeatherData();//获取当前城市并请求天气数据
        tvCity.setText(city);
        RecyclerView weatherList = findViewById(R.id.main_my_setting_weather_list);
        weatherList.setAdapter(weatherListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        weatherList.setLayoutManager(layoutManager);//设置为横向列表
    }

    /**
     * 请求天气数据
     */
    private void requestWeatherData() {
        city = SPUtil.getString("locationCity", "").replace("市", "");
        WeatherMgr.requestWeatherInfo(city, new WeatherMgr.OnGetWeatherFinishListener() {
            @Override
            public void onSuccess(WeatherFutureEntity weather) {
                weatherListAdapter.setDatas(weather.future);
            }

            @Override
            public void onError(int code, String errorMsg) {
                System.out.println(errorMsg);
            }
        });
    }

//    /**
//     * 判断虚拟功能键是否显示
//     */
//    private boolean isNavigationBarShow() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            Display display = activity.getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            Point realSize = new Point();
//            display.getSize(size);
//            display.getRealSize(realSize);
//            return realSize.y != size.y;
//        } else {
//            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
//            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
//            if (menu || back) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//    }
}
