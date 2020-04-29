package com.example.mypublib.widge;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：sucheng on 2019/8/12 11:19
 */
public class CehuiView extends View {
    public CehuiView(Context context) {
        super(context);
    }

    public CehuiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CehuiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        measure(widthMeasureSpec,heightMeasureSpec);
//        int width = getMeasuredWidth();
//        int height = getMeasuredHeight();
        int size = 20;//模拟传递进来的值
        int width = resolveSize(size, widthMeasureSpec);
        int height = resolveSize(size, heightMeasureSpec);
        // 设置最终的测量结果
        setMeasuredDimension(width, height);
    }

    public static int resolveSize(int size, int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:// unspecified
                result = size;
                break;
            case MeasureSpec.AT_MOST:// wrap_content
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.EXACTLY:// match_parent
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawBitmap(scaledDashBoard, 0, 0, null);
    }
}
