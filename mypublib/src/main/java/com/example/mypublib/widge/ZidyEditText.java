package com.example.mypublib.widge;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by sucheng
 * on 2019/5/9.
 */
public class ZidyEditText extends EditText {
    public ZidyEditText(Context context) {
        super(context);
    }

    public ZidyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZidyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
