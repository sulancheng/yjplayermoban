package com.csu.xgum.utils;

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
import android.text.TextUtils;
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
    public static SimpleDateFormat nianyue = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
    public static SimpleDateFormat nianyuedan = new SimpleDateFormat("yyyy-M", Locale.getDefault());
    public static SimpleDateFormat df_nyr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat bijiao = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

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
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/xgum/" + youpath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
    public static String getnewpath(String youpath) {
        String pathname = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xgum/" + youpath;
        pathname = pathname.endsWith("/") ? pathname : pathname + "/";
        File dir = new File(pathname);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
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
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 火星坐标转换为百度坐标
     *
     * @param gg_lat
     * @param gg_lon
     */
    static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;


    public static boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * 隐藏手机号中间四个
     *
     * @param tar
     * @return
     */
    public static String yicfour(String tar) {
        return tar.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

}
