package com.pivot.myandroiddemo.test.demoanimation.test3;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * 三阶贝塞尔曲线估值器
 */

public class BezierEvaluator implements TypeEvaluator<PointF> {
    private PointF point1;//控制点1    
    private PointF point2;//控制点2     

    public BezierEvaluator(PointF point1, PointF point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float minusT = 1.0f - fraction;
        PointF point = new PointF();
        point.x = minusT * minusT * minusT * (startValue.x)
                + 3 * minusT * minusT * fraction * (point1.x)
                + 3 * minusT * fraction * fraction * (point2.x)
                + fraction * fraction * fraction * (endValue.x);
        point.y = minusT * minusT * minusT * (startValue.y)
                + 3 * minusT * minusT * fraction * (point1.y)
                + 3 * minusT * fraction * fraction * (point2.y)
                + fraction * fraction * fraction * (endValue.y);
        return point;
    }
}
