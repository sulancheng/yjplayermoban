package com.yjplay.yjplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/3.
 * <p/>
 * 工具类：
 */
public class CommenUtils {

    private static Toast sToast;

    public static float dip2px(Context context, float value) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static Handler getHandler() {

        return sHandler;
    }

    public static void showSafeToast(final Context context, final String text) {

        sHandler.post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        });
    }


    public static void showSingleToast(Context context, String text) {

        if (sToast == null) {

            sToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }

        sToast.setText(text);
        sToast.show();
    }

    public static String onezero(double number) {
        DecimalFormat df = new DecimalFormat("0.0");
        return "" + df.format(number);
    }

    public static String twozero(double number) {
        DecimalFormat df = new DecimalFormat("0.00");
        return "" + df.format(number);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null)
            return info.isAvailable() && info.isConnected();
        return false;
    }

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    //下载地址
    public static String getdownloadPath(String youpath) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/oldman/" + youpath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /*对于改变空间的高宽*/
    public void changeWithHeight(View view, int with, int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = with;
        params.height = height;//设置当前控件布局的高度
        view.setLayoutParams(params);//将设置好的布局参数应用到控件中
    }

    //屏幕的宽高
    public static DisplayMetrics getwithandheight(Context mcontext) {
        DisplayMetrics outMetrics = mcontext.getResources().getDisplayMetrics();
        return outMetrics;
    }


    public static String deletzero(double number) {
        DecimalFormat df = new DecimalFormat("0");
        return "" + df.format(number);
    }
    //安装apk
    public void install(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
//            Log.i("27版本更新","更新"+file.getAbsolutePath()+"  包名："+context.getPackageName());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }


    /**
     * 当日志过长， 需要打印出来全部的方法
     */
    public static void printlnAllLog(String allLog) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String format = simpleDateFormat.format(new Date());
        String fileName = "mylog-" + format + ".log";
        String s = CommenUtils.getdownloadPath("mylog");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(s, fileName), true);
            fos.write((allLog + " \r\n").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
        }
    }
    public static void closeKeyboard(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }
    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

}
