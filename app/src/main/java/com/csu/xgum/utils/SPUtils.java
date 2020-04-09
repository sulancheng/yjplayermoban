package com.csu.xgum.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.csu.xgum.bean.Constants;

public class SPUtils {
    public static String GUIDE_ISFIRST = "guide_isfirst";
    public static String CONFIGFILE_NAME = "config";

    public static String getString(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(CONFIGFILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static int getInt(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(CONFIGFILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    //获取存储的是用户角标
    public static int getIntForUserDeafult(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(CONFIGFILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, 1);
    }

    public static boolean getBoolean(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(CONFIGFILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void put(Context ctx, String key, Object value) {
        SharedPreferences sp = ctx.getSharedPreferences(CONFIGFILE_NAME, Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        }
        edit.commit();
    }

    public static void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIGFILE_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void clearignoreusername(Context context) {
        put(context, Constants.PASS_WORD, "");
        put(context, Constants.NICK, "");
        put(context, Constants.USER_IMG, "");
        put(context, Constants.SESSIONID, "");
        put(context, Constants.TOKEN, "");
    }
}
