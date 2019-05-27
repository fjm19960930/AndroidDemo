package com.pivot.myandroiddemo.util;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 有关地图的工具
 */

public class MapUtils {
    
    /**
     * 视图转Bitmap 自定义Marker时使用
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }
}
