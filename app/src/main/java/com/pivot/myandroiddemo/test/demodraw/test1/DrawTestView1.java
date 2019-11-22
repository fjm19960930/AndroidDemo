package com.pivot.myandroiddemo.test.demodraw.test1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ASUS on 2019/8/14.
 */

public class DrawTestView1 extends View {
    public DrawTestView1(Context context) {
        super(context);
    }

    public DrawTestView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);//画笔颜色
        paint.setStyle(Paint.Style.STROKE);//填充样式 FILL填充内部 FILL_AND_STROKE填充内部和描边 STROKE描边
        paint.setStrokeWidth(40);//画笔宽度
        
        canvas.drawCircle(190, 200, 160, paint);
        
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(10);
        canvas.drawLine(400, 100, 650, 440, paint);
        
        //Rect和RectF的构造方法基本相同，但Rect保存数值类型是int，RectF保存数值类型是float
        paint.setColor(Color.GREEN);
        canvas.drawRect(10, 500, 100, 700, paint);

//        Rect rect = new Rect(150, 300, 250, 400);
        
//        Rect rect = new Rect();
//        rect.set(150, 300, 250, 400);
        
        RectF rectF = new RectF(150f, 500f, 250f, 700f);
        
//        RectF rectF = new RectF(new Rect(150, 300, 250, 400));
        
//        RectF rectF = new RectF();
//        rectF.set(150f, 300f, 250f, 400f);

        paint.setStyle(Paint.Style.FILL);
//        canvas.drawRect(rect, paint);
        canvas.drawRect(rectF, paint);
    }
}
