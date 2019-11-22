package com.pivot.myandroiddemo.test.demoanimation.test2;

import android.graphics.PointF;
import android.view.animation.Interpolator;

/**
 * 三阶贝塞尔曲线插值器
 * 
 * 也可以在XML文件中通过pathInterpolator标签来实现相同的效果，注意pathInterpolator只能实现三阶的贝塞尔插值器，
 * 高阶的需要类似于三阶实现Interpolator接口
 * 
 * https://blog.csdn.net/xsl_bj/article/details/47722489
 */

public class EaseCubicInterpolator implements Interpolator {
    private int mLastI = 0;
    private static final float STEP_SIZE = 1.0f / 4096;
    private final PointF point1 = new PointF();
    private final PointF point2 = new PointF();

    public EaseCubicInterpolator(float x1, float y1, float x2, float y2) {
        point1.x = x1;
        point1.y = y1;
        point2.x = x2;
        point2.y = y2;
    }

    @Override
    public float getInterpolation(float input) {
        float t = input;
        //如果重新开始要重置缓存的i。
        if (input == 0) {
            mLastI = 0;
        }
        // 近似求解t
        double tempX;
        for (int i = mLastI; i < 4096; i++) {
            t = i * STEP_SIZE;
            tempX = cubicEquation(t, point1.x, point2.x);
            if (tempX >= input) {
                mLastI = i;
                break;
            }
        }
        double value = cubicEquation(t, point1.y, point2.y);

        //如果结束要重置缓存的i。
        if (input == 1) {
            mLastI = 0;
        }
        return (float) value;
    }

    public static double cubicEquation(double t, double p1, double p2) {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double ttt = tt * t;
        return 3 * uu * t * p1 + 3 * u * tt * p2 + ttt;
    }
}
