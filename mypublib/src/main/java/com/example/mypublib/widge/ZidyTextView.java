package com.example.mypublib.widge;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 作者：sucheng on 2018/1/31 08:54
 */

public class ZidyTextView extends TextView {
    public ZidyTextView(Context context) {
        this(context, null);
    }

    public ZidyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZidyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
//        AssetManager assets = context.getAssets();
//        Typeface typeface = Typeface.createFromAsset(assets, "fonts/Arial.ttf");
//        setTypeface(typeface);
//        setIncludeFontPadding(false);
    }
}
