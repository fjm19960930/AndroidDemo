package com.pivot.myandroiddemo.test.evaluator;

import android.os.Bundle;
import android.view.View;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseActivity;

/**
 * 估值器使用示例
 */
public class EvaluatorActivity extends BaseActivity {
    private boolean isPause = false;//是否处于暂停状态
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluator);
        setToolbarTitle("估值器Demo");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
            
        CharChangeView charChangeView = findViewById(R.id.evaluator_char_change);
        CircleChangeView circleChangeView = findViewById(R.id.evaluator_circle_change);
        findViewById(R.id.char_start).setOnClickListener(v -> {
            isPause = false;
            charChangeView.startAnimation();
            circleChangeView.startAnimation();
            findViewById(R.id.char_pause_resume).setVisibility(View.VISIBLE);
        });
        findViewById(R.id.char_pause_resume).setOnClickListener(v -> {
            if (isPause) {
                isPause = false;
                charChangeView.getAnimator().resume();
            } else {
                isPause = true;
                charChangeView.getAnimator().pause();
            }
        });
    }
    
}
