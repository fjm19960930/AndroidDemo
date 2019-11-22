package com.pivot.myandroiddemo.test.demoview.test1;

import android.graphics.RectF;
import android.os.Bundle;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.util.SystemUtils;

@ActivityParam(isShowToolBar = false)
public class ViewTest1Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test1);

        DragScaleView dragScaleView = findViewById(R.id.drag_scale_view);
        DrawBoxView drawBoxView = findViewById(R.id.draw_box_view);

        dragScaleView.setImageResource(R.drawable.test);
        dragScaleView.setRect(new RectF(0, 0, SystemUtils.dp2px(getBaseContext(), 300), SystemUtils.dp2px(getBaseContext(), 500)));
        dragScaleView.setUpdateListener(new DragScaleView.UpdateListener() {
            @Override
            public void onChange(float posX, float posY, float scale) {
                drawBoxView.setValues(posX, posY, scale);
            }
        });
        drawBoxView.setRect(new RectF(SystemUtils.dp2px(getBaseContext(), 5), SystemUtils.dp2px(getBaseContext(), 5), 
                SystemUtils.dp2px(getBaseContext(), 300) + SystemUtils.dp2px(getBaseContext(), 5), 
                SystemUtils.dp2px(getBaseContext(), 500) + SystemUtils.dp2px(getBaseContext(), 5)));
        drawBoxView.setRectOffset(SystemUtils.dp2px(getBaseContext(), 5));
        drawBoxView.setCropListener(new DrawBoxView.CropListener() {
            @Override
            public void onUp(int positionIndex, float scale, float xOffset, float yOffset, RectF rectF) {
                dragScaleView.doAnimation(positionIndex, scale, xOffset, yOffset, rectF);
            }
        });
    }
}
