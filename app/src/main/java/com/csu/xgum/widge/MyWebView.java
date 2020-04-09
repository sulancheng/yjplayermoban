package com.csu.xgum.widge;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

/**
 * 作者：sucheng on 2018/12/19 0019 09:19
 */
public class MyWebView extends WebView {
    private Context context;

    public MyWebView(Context context) {
        super(context);
        init(context);//这样写防止键盘弹出失败
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context mcontext) {
        this.context = mcontext;
        WebSettings webSettings = getSettings();
        //设置支持javaScript脚步语言
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);

        //支持双击-前提是页面要支持才显示
//        webSettings.setUseWideViewPort(true);

        //支持缩放按钮-前提是页面要支持才显示
        webSettings.setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        //webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setUseWideViewPort(true); // 关键点 会使得布局放大
        webSettings.setAllowFileAccess(true); // 允许访问文件
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//不加上，会显示白边
        //设置客户端-不跳转到默认浏览器中
        //myweb.setWebViewClient(new WebViewClient());
        webSettings.setDomStorageEnabled(true);//
        setDownloadListener(new MyWebViewDownLoadListener());
        //设置支持js调用java
        addJavascriptInterface(new AndroidAndJSInterface(context), "Android");
        //设置监听
        setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();// 接受所有网站的证书
            }

            //开始加载时候调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                view.loadUrl("javascript:(function(){"
//                        + "var mydiv = $('#xuecontent')[0];"
//                        + "var objs = mydiv.getElementsByTagName(\"img\"); "
//                        + "for(var i=0;i<objs.length;i++)  " + "{"
//                        + "    objs[i].onclick=function()  " + "    {  "
//                        + "        window.imagelistner.openImage(this.src);  "
//                        + "    }  " + "}" + "})()");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //页面重定向。默认点击webview上面的连接会使用其他的浏览器打开
                //设置下面的，会在自己页面中 打开
                try {
                    if (url.startsWith("http://") && url.startsWith("https://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    return true;
                }
                loadUrl(url);
                //return super.shouldOverrideUrlLoading(view, url);
                return true;
            }
        });
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    private String trcontent;

    public void setData(String data) {
        this.trcontent = data;
    }

    /**
     * js可以调用该类的方法
     */
    class AndroidAndJSInterface {
        @JavascriptInterface
        public String getContent() {
            return trcontent;
        }

        private Context context;

        public AndroidAndJSInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void openImage(String img, String imgs, String position) {
            //MyLog.i("JavascriptInterfaceimg="+imgs+"  position="+position);
//            Intent intent = new Intent();
//            intent.putExtra("img", img);
//            intent.putExtra("imgs", imgs);
//            intent.putExtra("position", position);
//            intent.setClass(context, ImgActivity.class);
//            context.startActivity(intent);
        }
    }

    /**
     * 关于下载pdf的代码
     * Created by sucheng
     * on 2017/9/1 11:35
     */
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            String end = url.substring(url.lastIndexOf(".") + 1, url.length()).toLowerCase();
            if (!end.equalsIgnoreCase("pdf") && !end.equalsIgnoreCase("excel")) {
                return;
            }
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
        }

    }

    //内部类

    //下载完成跳转
    public Intent getFileIntent(File file) {
//       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type=" + type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    //下载时候的圆形进度条
    private Dialog mDialog;

    private void showProgressDialog() {
//        if (mDialog == null) {
//            if (mDialog == null) {
//                mDialog = new AlertDialog.Builder(this).create();
//                mDialog.setCanceledOnTouchOutside(false);
//                mDialog.setCancelable(true);
//                mDialog.show();
//                View layout = View.inflate(this, R.layout.layout_downloadpdf, null);
//                mDialog.getWindow().setContentView(layout);
//                mDialog.getWindow().setGravity(Gravity.CENTER);
//                mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        // TODO Auto-generated method stub
//                        mDialog = null;
//                    }
//                });
//            }
//
//        }
    }

    private void closeProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    //判断文件类型
    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

        /* 依扩展名的类型决定MimeType */
        if (end.equals("pdf")) {
            type = "application/pdf";//
        } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
//      else if(end.equals("pptx")||end.equals("ppt")){
//        type = "application/vnd.ms-powerpoint";
//      }else if(end.equals("docx")||end.equals("doc")){
//        type = "application/vnd.ms-word";
//      }else if(end.equals("xlsx")||end.equals("xls")){
//        type = "application/vnd.ms-excel";
//      }
        else {
//        /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }
}
