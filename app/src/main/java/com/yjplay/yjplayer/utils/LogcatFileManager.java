package com.yjplay.yjplayer.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by mac on 16/1/15.
 */

public class LogcatFileManager implements Thread.UncaughtExceptionHandler {
    private static LogcatFileManager INSTANCE = null;
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //程序的Context对象
    private Context mContext;
    private LogDumper mLogDumper = null;
    private int mPId;
    private SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String TAG = LogcatFileManager.class.toString();

    public static LogcatFileManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LogcatFileManager();
        }
        return INSTANCE;
    }


    public void init(Context context) {
        this.mContext = context;
        mPId = android.os.Process.myPid();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e("", "error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            // mDefaultHandler.uncaughtException(thread, ex);
            System.exit(1);
        }
    }


    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }.start();
        // 收集设备参数信息
        StringBuffer datas = collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex,datas);
        return true;
    }

    /**
     * 收集设备参数信息
     * @param ctx
     */
    public StringBuffer collectDeviceInfo(Context ctx) {
        StringBuffer datas = new StringBuffer();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                datas.append("versionName" + "=" + versionName + "\n");
                datas.append("versionCode" + "=" + versionCode + "\n");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                datas.append(field.getName() + "=" + field.get(null).toString() + "\n");
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
        return datas;
    }
    /**
     * 保存错误信息到文件
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex, StringBuffer datas) {
//        StringBuffer sb = getTraceInfo(ex);
        StringBuffer sb = new StringBuffer();
        if (datas!=null&&!TextUtils.isEmpty(datas)){
            //添加机型信息。
            sb.append("机型信息 ："+"\n");
            sb.append(datas);
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        //添加错误信息。
        sb.append("错误信息信息 ："+"\n");
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = simpleDateFormat1.format(new Date());
            String fileName = "crash-myapptools-" + time  + ".log";

            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = getDpath("mylogcrash/");
                path = path.endsWith("/") ? path : path + "/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(dir, fileName), true));
                bufferedWriter.write(sb + " \r\n");
                bufferedWriter.close();
//                FileOutputStream fos = new FileOutputStream(path + fileName,true);
//                fos.write(sb.toString().getBytes());
//                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }finally {
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            // mDefaultHandler.uncaughtException(thread, ex);
            System.exit(1);
        }
        return null;
    }


    /**
     * 整理异常信息
     *
     * @param e
     * @return
     */
    public static StringBuffer getTraceInfo(Throwable e) {
        StringBuffer sb = new StringBuffer();

        Throwable ex = e.getCause() == null ? e : e.getCause();
        StackTraceElement[] stacks = ex.getStackTrace();
        for (int i = 0; i < stacks.length; i++) {
            if (i == 0) {
                setError(ex.toString());
            }
            sb.append("class: ").append(stacks[i].getClassName())
                    .append("; method: ").append(stacks[i].getMethodName())
                    .append("; line: ").append(stacks[i].getLineNumber())
                    .append(";  Exception: ").append(ex.toString() + "\n");
        }
        Log.d(TAG, sb.toString());
        return sb;
    }


    /**
     * 设置错误的提示语
     *
     * @param e
     */
    public static void setError(String e) {

    }

    public static String getDpath(String filename){
        String folderPath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            folderPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiaokedou/"+filename;
        }else {
            folderPath = Environment.getDataDirectory().getAbsolutePath() + "/data/xiaokedou/"+filename;
        }
        return folderPath;
    }
    public void startLog(Context context) {
        String folderPath = null;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MMF-Logcat";
//        } else {
//            folderPath = context.getFilesDir().getAbsolutePath() + File.separator + "MMF-Logcat";
//        }
        folderPath = getDpath("mylogcat/");
        folderPath = folderPath.endsWith("/") ? folderPath : folderPath + "/";
        File dir = new File(folderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        LogcatFileManager.getInstance().start(folderPath);
    }


    public void stopLog() {
        LogcatFileManager.getInstance().stop();
    }



    private void start(String saveDirectoy) {

        try {
            if (mLogDumper == null) {
                mLogDumper = new LogDumper(String.valueOf(mPId), saveDirectoy);
            }
            mLogDumper.start();
        } catch (Exception e) {

        }
    }


    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }



    private class LogDumper extends Thread {
        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        String cmds = null;
        private String mPID;
        private String dir;
        private BufferedWriter out = null;


        public LogDumper(String pid, String dir) {
            mPID = pid;
            this.dir = dir;
//            try {
//                out = new FileOutputStream(new File(dir, "logcat-myapptools-" + simpleDateFormat1.format(new Date()) + ".log"), true);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }


            /**
             * * * log level：*:v , *:d , *:w , *:e , *:f , *:s * * Show the
             * current mPID process level of E and W log. * *
             */
            // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
            cmds = "logcat  *:v | grep \"( Tohow )\"";
        }


        public void stopLogs() {
            mRunning = false;
        }
        //非常成功  能够及时刷新
        @Override
        public void run() {
            try {
                out=  new BufferedWriter(new FileWriter(new File(dir, "logcat-myapptools-" + simpleDateFormat1.format(new Date()) + ".log"), true));
//                out = new FileOutputStream(new File(dir, "logcat-sc-" + simpleDateFormat1.format(new Date()) + ".log"), true);
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()));
//                MyLog.i("日志记录",mReader.toString());
//                char[] chr = new char[1024];
//                int len;
//                while ((len = mReader.read(chr)) != -1) {
//                    out.write(chr,0,len);
//                }
                String line = null;
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    if (out != null && line.contains(mPID)) {
                        out.write((simpleDateFormat2.format(new Date()) + "  " + line + "\r\n"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out = null;
                }
            }
        }

//        @Override
//        public void run() {
//            try {
//                logcatProc = Runtime.getRuntime().exec(cmds);
//                mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
//                String line = null;
//                while (mRunning && (line = mReader.readLine()) != null) {
//                    if (!mRunning) {
//                        break;
//                    }
//                    if (line.length() == 0) {
//                        continue;
//                    }
//                    if (out != null && line.contains(mPID)) {
//                        out.write((simpleDateFormat2.format(new Date()) + "  " + line + "\n").getBytes());
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (logcatProc != null) {
//                    logcatProc.destroy();
//                    logcatProc = null;
//                }
//                if (mReader != null) {
//                    try {
//                        mReader.close();
//                        mReader = null;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (out != null) {
//                    try {
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    out = null;
//                }
//            }
//        }
    }
}