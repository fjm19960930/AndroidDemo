package com.pivot.myandroiddemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.util.TakePhotoUtils;
import com.zcolin.frame.util.SPUtil;
import com.zcolin.frame.util.ToastUtil;
import com.zcolin.gui.ZPopupMenu;

import java.util.Arrays;
import java.util.List;

/**
 * 个人信息详情页
 */
@ActivityParam(isShowToolBar = false)
public class MineDetailsActivity extends BaseActivity {

    private TakePhotoUtils takePhotoUtils;
    private ImageView ivHeadImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_details);

        takePhotoUtils = new TakePhotoUtils(mActivity);
        findViewById(R.id.iv_mine_back).setOnClickListener(v -> mActivity.finish());//回退按钮
        findViewById(R.id.iv_mine_punch).setOnClickListener(v -> ToastUtil.toastLong("打卡成功！"));//签到按钮
        TextView tvUsername = findViewById(R.id.tv_mine_username);//用户名
        TextView tvUserDesc = findViewById(R.id.tv_mine_user_desc);//用户备注
        tvUsername.setText(getIntent().getStringExtra("username"));
        tvUserDesc.setText(getIntent().getStringExtra("userDesc"));
        ivHeadImg = findViewById(R.id.iv_mine_head);//用户头像
        ivHeadImg.setOnClickListener(v -> new ZPopupMenu(mActivity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//重新设置头像
                .addAction("从相册选择图片")
                .addAction("拍照")
                .addAction("取消")
                .setBackgroundColor(Color.parseColor("#fafafa"))
                .setIsFullDim(true)
                .setOnItemClickListener((item1, position) -> {
                    if (position == 0) {
                        takePhotoUtils.openPhotoAlbum();//打开相册
                    } else if (position == 1) {
                        takePhotoUtils.openCamera();//打开相机
                    }
                    return true;
                })
                .showAtLocation(findViewById(R.id.mine_details_activity), 0, 0, Gravity.BOTTOM)
        );
        setHeadImg();
        
        TextView tabPlan = findViewById(R.id.tab_mine_plan);//我的计划
        TextView tabFood = findViewById(R.id.tab_mine_food);//我的美食
        TextView tabBeauty = findViewById(R.id.tab_mine_beauty);//我的美景
        tabPlan.setTextColor(getResources().getColorStateList(R.color.mine_tab_text_color_selector));
        tabFood.setTextColor(getResources().getColorStateList(R.color.mine_tab_text_color_selector));
        tabBeauty.setTextColor(getResources().getColorStateList(R.color.mine_tab_text_color_selector));
        List<View> tabViewList = Arrays.asList(tabPlan, tabFood, tabBeauty);
        setTabMode(tabViewList, 0);
        tabPlan.setOnClickListener(v -> setTabMode(tabViewList, 0));
        tabFood.setOnClickListener(v -> setTabMode(tabViewList, 1));
        tabBeauty.setOnClickListener(v -> setTabMode(tabViewList, 2));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TakePhotoUtils.TAKECAMERAREQUESTCODE:
                    //打开相机拍照后的图片并启动裁剪
                    String path = mActivity.getExternalCacheDir().getPath();
                    String name = "output.png";
                    mActivity.startActivityForResult(takePhotoUtils.cutForCamera(path, name), TakePhotoUtils.CUTPHOTOREQUESTCODE);
                    break;
                case TakePhotoUtils.TAKEPHOTOALBUMREQUESTCODE:
                    //打开相册选择的图片并启动裁剪
                    Intent intent = takePhotoUtils.cutForPhoto(data.getData());
                    mActivity.startActivityForResult(intent, TakePhotoUtils.CUTPHOTOREQUESTCODE);
                    break;
                case TakePhotoUtils.CUTPHOTOREQUESTCODE:
                    takePhotoUtils.getCutPhotoAndUpload();
                    setHeadImg();
                    ToastUtil.toastLong("头像修改成功！");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设置tab选项状态
     */
    private void setTabMode(List<View> tabList, int index) {
        for (int i = 0; i < tabList.size(); i++) {
            if (i == index) {
                tabList.get(i).setSelected(true);
                tabList.get(i).setBackgroundColor(Color.WHITE);
            } else {
                tabList.get(i).setSelected(false);
                tabList.get(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
    
    /**
     * 设置头像
     */
    private void setHeadImg() {
        if (SPUtil.contains("headImgUrl")) {
            String headImgUrl = SPUtil.getString("headImgUrl", "");
            Bitmap bitmap = BitmapFactory.decodeFile(headImgUrl);
            RoundedBitmapDrawable shopDrawable = RoundedBitmapDrawableFactory.create(mActivity.getResources(), bitmap);//构造圆形图片
            shopDrawable.setCornerRadius(Integer.MAX_VALUE);
            shopDrawable.setCircular(true);
            ivHeadImg.setBackgroundDrawable(shopDrawable);
        }
    }
}
