package com.yjplay.yjplayer.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yjplay.yjplayer.R;


/**
 * Created by sucheng
 * on 2017/8/15.
 */

public class MyToast {
    private static Toast mToast;
    private static View v;
    private static TextView textView;

    private MyToast(Context context, CharSequence text, int duration) {
        textView.setText(text);

        mToast.setDuration(duration);
        mToast.setView(v);
    }

    public static void makeText(Context context, CharSequence text, int duration) {
        if(v==null||textView == null){
            v = View.inflate(context, R.layout.eplay_toast, null);
            textView = (TextView) v.findViewById(R.id.tv_text);
            mToast = new Toast(context);
        }
        textView.setText(text);
        mToast.setDuration(duration);
        mToast.setView(v);
        if (mToast != null) {
            mToast.show();
        }
    }
    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }
    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
