package com.yjplay.yjplayer.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by su
 * on 2017/3/24.
 */
public class OkUtils {

    public static boolean RELEASE = false;
    private static final String ADDRESS_1 = "http://139.159.151.240:8889"; //release service
    private static final String ADDRESS_2 = "http://211.110.12.198:89"; //local service
    private static final String SERVICE = "/api/web/";
    private static Gson gson = new Gson();

    public static String getServerChannel() {
        String serverUrl = "";
        if (RELEASE) {
            serverUrl = ADDRESS_2;
        } else {
            serverUrl = ADDRESS_1;
        }
        serverUrl = serverUrl + SERVICE;
        return serverUrl;
    }

    public static String photopa() {
        String serverUrl = "";
        if (RELEASE) {
            serverUrl = ADDRESS_2;
        } else {
            serverUrl = ADDRESS_1;
        }
        return serverUrl;
    }

    public static void demo(Context context, MyResponse myresponse) {
        HashMap<String, String> paramss = new HashMap<>();
        paramss.put("username", "sulc");
        paramss.put("password", "123456");
        paramss.put("q", "s");
        requsetJsonparm("http://192.168.0.102:8085/php/gethint.php", context, paramss, myresponse);
    }

    //获取考试列表
    public static void getTestbiao(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/getExamineList", context, reparam, myresponse);
    }

//    //获取验证码
//    public static void getyanzm(Context context, LogRequestBean reparam, MyResponse myresponse) {
//        requsetJsonparm(getServerChannel() + "sendautthorized", context, reparam, myresponse);
//    }


    //获取版本
    public static void getAppEdition(Context context, HashMap reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "getAppEdition", context, reparam, myresponse);
    }
    //获取更多
    public static void getMore(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/API/Web/getgengduo", context, reparam, myresponse);
    }

    //获取更多2
    public static void getMore2(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/API/Web/getgengduotwo", context, reparam, myresponse);
    }

    //获取最新
    public static void getNew(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/API/Web/getzuixin", context, reparam, myresponse);
    }

    //获取最新2
    public static void getNew2(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/API/Web/getzuixintwo", context, reparam, myresponse);
    }

    //登录
    public static void getLogin(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "login", context, reparam, myresponse);
    }

    //广告
    public static void getguanggaotu(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/API/web/getguanggaotu", context, reparam, myresponse);
    }

    //广告2
    public static void getguanggaotu2(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/API/web/getguanggaotutwo", context, reparam, myresponse);
    }

    //修改密码
    public static void updatepassword(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/API/web/updatepassword", context, reparam, myresponse);
    }

    //轮播
    public static void getlunbotu(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/API/Web/getlunbotu", context, reparam, myresponse);
    }

    //分享
    public static void getshare(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/API/Web/fenxiang", context, reparam, myresponse);
    }

    //获取首页
    public static void getShouye(Context context, Object reparam, MyResponse myresponse) {
        requsetJsonparm(getServerChannel() + "/getExamineList", context, reparam, myresponse);
    }

    public static void uphead(Context context, File file, HashMap<String, String> params, MyResponse myresponse) {
        requsetWenjianOne(getServerChannel() + "ImgUpload", context, file, params, myresponse, 1);
    }

    public static void upDataStudy(Context context, List<File> files, HashMap<String, String> params, MyResponse myresponse) {
        requsetWenjian(getServerChannel() + "/update", context, files, params, myresponse, 1);
    }


    //    //获取论坛回复
//    public static synchronized void getLunThfconent(Context context, ZxXqLt reparam, MyResponse myresponse) {
//        requsetJsonparm(getServerChannel() + "/getForumReplyByID",context,reparam,myresponse);
//    }
    public static void logintwo(Context context, MyResponse myresponse, File file) {
        HashMap<String, String> paramss = new HashMap<>();
        paramss.put("username", "sulc");
        paramss.put("password", "123456");
        paramss.put("q", "s");
        ArrayList<File> mfiles = new ArrayList<>();
        mfiles.add(file);
        //requsetWenjian("http://192.168.0.101:8085/php/upload_file.php", context, mfiles, myresponse);
    }

    public static void requsetJsonparm(String url, final Context mContext, HashMap<String, String> params, final MyResponse myresponse, final int type) {
        MyLog.i("post_json数据请求", " url=" + url + " params = " + params.toString());
        OkGo.<String>post(url)
                .tag(mContext)
                //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)//  这里不要使用params，upJson 与 params 是互斥的，只有 upJson 的数据会被上传
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        checkresponse(1, response.body(), myresponse, type);
                        MyLog.i("post_json数据请求返回的初始数据", " response =" + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        MyToast.makeText(mContext, "网络或服务器异常!", Toast.LENGTH_SHORT);
                        MyLog.i("返回的数据", response.getException().toString());
                        checkresponse(2, response.body(), myresponse, type);
                    }
                });
    }

    public static void requsetWenjianOne(final String url, final Context mContext, File file, HashMap<String, String> params, final MyResponse myresponse, final int type) {
        if (!CommenUtils.isNetworkConnected(mContext)) {
            checkresponse(2, "网络连接错误!", myresponse, 1);
            MyToast.makeText(mContext, "网络连接错误!", 0);
            return;
        }
        OkGo.<String>post(url)
                .tag(mContext)
                .params(params)
                .params("pic", file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        checkresponse(1, response.body(), myresponse, type);
                        MyLog.i("post_json数据请求返回的初始数据", " response =" + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        MyToast.makeText(mContext, "网络或服务器异常!", Toast.LENGTH_SHORT);
                        MyLog.i("网络或服务器异常", " response =" + url);
                        MyLog.i("返回的数据", response.getException().toString());
                        checkresponse(2, response.body(), myresponse, type);
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        super.uploadProgress(progress);
                        MyLog.i("返回的进度", progress.fraction + "");
                        if (myresponse != null) {
                            myresponse.getProgress(progress);
                        }
                    }
                });
    }

    public static void requsetWenjian(String url, final Context mContext, List<File> files, HashMap<String, String> params, final MyResponse myresponse, final int type) {
        if (!CommenUtils.isNetworkConnected(mContext)) {
            checkresponse(2, "网络连接错误!", myresponse, 1);
            MyToast.makeText(mContext, "网络连接错误!", 0);
            return;
        }
        PostRequest<String> myfileRequset = OkGo.<String>post(url)//"http://192.168.0.105:8081/student/form"
                .tag(mContext);
        //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
        // .params("myfile", myfile);//  这里不要使用params，upJson 与 params 是互斥的，只有 upJson 的数据会被上传
        if (files != null && files.size() > 0) {
            for (int a = 0; a < files.size(); a++) {
                myfileRequset.params("imgs" + a, files.get(a));
            }
        }
        // myfileRequset.params("imgs1" , files.get(0));
//        myfileRequset.addFileParams("imgs",files);
        myfileRequset.params(params);
        myfileRequset.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                checkresponse(1, response.body(), myresponse, type);
                MyLog.i("post_json数据请求返回的初始数据", " response =" + response.body());
            }

            @Override
            public void onError(Response<String> response) {
                MyToast.makeText(mContext, "网络或服务器异常!", Toast.LENGTH_SHORT);
                MyLog.i("返回的数据", response.getException().toString());
                checkresponse(2, response.body(), myresponse, type);
            }

            @Override
            public void uploadProgress(Progress progress) {
                super.uploadProgress(progress);
                MyLog.i("返回的进度", progress.fraction + "");
                if (myresponse != null) {
                    myresponse.getProgress(progress);
                }
            }
        });
    }

    public static void requsetJsonparm(final String url, final Context mContext, Object obj, final MyResponse myresponse) {
        if (!CommenUtils.isNetworkConnected(mContext)) {
            checkresponse(2, "网络连接错误!", myresponse, 1);
            MyToast.makeText(mContext, "网络连接错误!", 0);
            return;
        }
        String json = gson.toJson(obj);
        MyLog.i("post_json数据请求", " url=" + url + " jsonup = " + json);
        OkGo.<String>post(url)
                .tag(mContext)
                //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                // .params(params)//  这里不要使用params，upJson 与 params 是互斥的，只有 upJson 的数据会被上传
                .upJson(json)//setCertificates
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        MyLog.i("post_json数据请求返回的初始数据", " response =" + response.body());
                        String body = response.body();
                        try {
                            if (body != null) {
//                                LogResponBean respBean = gson.fromJson(body, LogResponBean.class);
//                                if (respBean.getErrcode().equals("1")){
//                                    EventBus.getDefault().post(new EventMessage(Constants.type_shixiao));//重新登录
//                                    return;
//                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            MyLog.e("自定义异常","json格式化异常");
                        }
                        checkresponse(1, response.body(), myresponse, 0);

                    }

                    @Override
                    public void onError(Response<String> response) {
                        MyToast.makeText(mContext, "网络或服务器异常!", Toast.LENGTH_SHORT);
                        MyLog.i("网络或服务器异常", " response =" + url);
                        checkresponse(2, response.body(), myresponse, 0);
                    }
                });
    }

    public abstract static class MyResponse {
        public abstract void expResponse(String myresponse);

        public abstract void error(String erro);

        public void getProgress(Progress progress) {

        }
    }

    private static void checkresponse(int type, String responseBody, MyResponse myresponse, int isupdata) {
        if (type == 1) {//成功
            if (myresponse != null) {
                if (responseBody == null) {
                    responseBody = "";
                }
                myresponse.expResponse(responseBody);
            }

        } else {
            if (myresponse != null) {
                if (responseBody == null) {
                    responseBody = "erro";
                }
                myresponse.error(responseBody);
            }
        }

    }
}
