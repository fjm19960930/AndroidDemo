package com.pivot.myandroiddemo.test.demoanimation.test3;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;

@ActivityParam(isShowToolBar = false)
public class AnimationTest3Activity extends BaseActivity {

    private HeartLayout mHeartLayout;
    private LoadingImageView mLoadingImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test3);
        
        mLoadingImageView = findViewById(R.id.iv_anim_test3);
        mLoadingImageView.start();
        
        mHeartLayout = findViewById(R.id.heart_layout);
        mHeartLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mHeartLayout.addHeart();
//                        Log.d("AnimationTest3Activity", "down");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        mHeartLayout.addHeart();
//                        Log.d("AnimationTest3Activity", "up");
//                        break;
//                }
                mHeartLayout.addHeart();
                Log.d("AnimationTest3Activity", event.toString());
                return false;
                //返回false时代表不消耗当前点击事件且只响应down事件
                //返回true时代表消耗当前事件且响应down-move-up三个事件
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHeartLayout.heartFinish();
        mLoadingImageView.cancel();
    }
}
