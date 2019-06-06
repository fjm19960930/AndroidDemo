package com.pivot.myandroiddemo.test.evaluator;

import android.animation.TypeEvaluator;

/**
 * 字母估值器
 */
public class CharEvaluator implements TypeEvaluator<Character> {
    @Override
    public Character evaluate(float fraction, Character startValue, Character endValue) {
        int startInt = (int) startValue;//起始字母的ASCII码，A是65
        int endInt = (int) endValue;
        int curInt = (int) (startInt + fraction * (endInt - startInt));//根据进度算出当前字母的ASCII
        char result = (char) curInt;
        return result;
    }
}
