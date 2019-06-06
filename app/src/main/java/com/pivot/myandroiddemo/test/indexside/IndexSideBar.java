package com.pivot.myandroiddemo.test.indexside;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 右侧字母选择框
 */
public class IndexSideBar extends View {
    public static String[] b = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private List<String> indexContent = new ArrayList<>();
    private Paint paint = new Paint();
    private int mChoose = -1;//被点击字母的位置
    private TextView mIndicatorTv;//顶部字母提示文本框
    private itemClickListener mListener;

    public IndexSideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Collections.addAll(indexContent, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();// 获取对应高度    
        int width = getWidth(); // 获取对应宽度    
        int singleHeight = height / (indexContent.size() + 1);// 获取每一个字母的高度     

        // 画圆圈    
        if (-1 == mChoose) {
            paint.setColor(Color.parseColor("#888888"));
        } else {
            paint.setColor(Color.parseColor("#fa7829"));
        }
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2.0f, singleHeight / 2, singleHeight / 4, paint);
        paint.reset();
        //写字    
        for (int i = 0; i < indexContent.size(); i++) {
            if (i == mChoose) {
                paint.setColor(Color.parseColor("#fa7829"));
            } else {
                paint.setColor(Color.parseColor("#888888"));
            }
            paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体
            paint.setAntiAlias(true);//设为抗锯齿
            paint.setTextSize(30);
            paint.setTextAlign(Paint.Align.CENTER);
            //x源相对坐标设置为中间位置        
            float xPos = width / 2;
            //给点中间线的位置,计算出baseline位置        
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            int center = singleHeight * (i + 1) + singleHeight / 2;
            int baseline = center + (fm.bottom - fm.top) / 2 - fm.bottom;
            canvas.drawText(indexContent.get(i), xPos, baseline, paint);
            paint.reset();// 重置画笔   
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float y = event.getY();// 点击y坐标	
        // 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.    
        int pos = (int) (y / getHeight() * (indexContent.size() + 1));
        //由于总共的字符中包含最顶层的圆圈,所以indexContent中真正字符的位置应当在当前mChoose位置上减一    
        mChoose = pos - 1;
        String text = indexContent.get(mChoose);
        if (null != mIndicatorTv) {
            mIndicatorTv.setVisibility(VISIBLE);
            mIndicatorTv.setText(text);
        }
        if (null != mListener) {
            mListener.onChoosed(mChoose, text);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                mChoose = -1;
                if (null != mIndicatorTv) {
                    mIndicatorTv.setVisibility(GONE);
                }
            }
            break;
            default:
                break;
        }
        //强制重绘    
        invalidate();
        return true;
    }

    public void setIndicatorTv(TextView tv) {
        this.mIndicatorTv = tv;
    }

    public void setListener(itemClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * item点击回调接口
     */
    public interface itemClickListener {
        void onChoosed(int index, String text);
    }
}
