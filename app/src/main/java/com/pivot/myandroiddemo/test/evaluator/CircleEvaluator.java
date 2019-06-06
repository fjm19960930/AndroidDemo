package com.pivot.myandroiddemo.test.evaluator;

import android.animation.TypeEvaluator;

/**
 * 圆圈半径估值器
 */
public class CircleEvaluator implements TypeEvaluator<Circle> {
    @Override
    public Circle evaluate(float fraction, Circle startValue, Circle endValue) {
        int start = startValue.getRadius();
        int end = endValue.getRadius();
        int curValue = (int) (start + fraction * (end - start));
        return new Circle(curValue);
    }
}
