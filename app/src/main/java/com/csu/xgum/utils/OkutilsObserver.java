package com.csu.xgum.utils;

import android.content.Context;
import android.text.TextUtils;

import com.csu.xgum.bean.MyTrowable;
import com.example.mypublib.utils.MyLog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * Created by suye
 * on 2019/4/2 0002.
 */
public class OkutilsObserver {
    public static boolean RELEASE = false;
    private static final String ADDRESS_1 = "http://frank.51vip.biz/"; //release service
    private static final String ADDRESS_2 = "http://139.159.151.240:55678"; //local service
    private static final String SERVICE = "/api/";
    private static Gson gson = new Gson();
    public static final String img_f_path = "http://139.159.151.240:55678"; //img

    public static String getServerChannel() {
        String serverUrl = "";
        if (RELEASE) {
            serverUrl = ADDRESS_2 + SERVICE;
        } else {
            serverUrl = ADDRESS_1 + SERVICE;
        }
        return serverUrl;
    }


    //获取商品轮播图
    public static <T> Observable<T> aroundShopNews(Context context, Class<T> clazz) {
        return requsetJsonparmget(getServerChannel() + "AroundShop/AroundShopNews", context, clazz);
    }


    //修改个人资料
    public static <T> Observable<T> uploadPhotoAndChangeNickName(Context context, List<File> files, HashMap<String, String> params, Class<T> clazz) {
        return requsetWenjian(getServerChannel() + "User/UploadPhotoAndChangeNickName?token=" + params.get("token") + "&nickName=" + params.get("nickName"), context, files, params, clazz);
    }



    public static <T> Observable<T> myfiler(String data, Class<T> clazz) {
        return Observable.create(emitter -> {
            emitter.onNext(gson.fromJson(data, clazz));
        });
    }

    public static <T> Observable<T> upDataHeadImg(String url, final Context mContext, File headFile, Class<T> clazz) {
        MyLog.i("发送数据OkutilsObserver qingqiu", "url=" + url + "json = " + headFile.getAbsolutePath());
        return Observable.create((ObservableEmitter<T> e) -> {
            OkGo.<String>post(url)
                    .tag(mContext)
                    //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                    .params("headimg", headFile)//  这里不要使用params，upJson 与 params 是互斥的，只有 upJson 的数据会被上传
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            MyLog.i("获取得数据OkutilsObserverurl=" + url, response.body());
                            String body = response.body();
                            if (body == null || TextUtils.isEmpty(body)) {
                                e.tryOnError(new MyTrowable("服务器数据异常！", -2));
                            } else {
                                try {
                                    //数据发送已经完成
                                    if (!clazz.equals(String.class)) {
                                        e.onNext(gson.fromJson(body, clazz));
                                    } else {
                                        e.onNext((T) body);
                                    }
                                } catch (Exception ex) {
                                    e.tryOnError(new MyTrowable(ex.getMessage() + "json格式异常", -3));
                                }
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Throwable exception = response.getException();
                            MyLog.e("获取得数据OkutilsObserver erro", "url=" + url + " exception = " + exception.getMessage());
                            e.tryOnError(exception);
                            e.onComplete();
                        }
                    });
        });
    }

    public static <T> Observable<T> requsetWenjian(String url, final Context mContext, List<File> files, HashMap<String, String> params, Class<T> clazz) {

        return Observable.create((ObservableEmitter<T> e) -> {
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
            // myfileRequset.addFileParams("imgs",files);
            myfileRequset.params(params);
            myfileRequset.isMultipart(true);//该方法表示是否强制使用multipart/form-data表单上传
            myfileRequset.execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    MyLog.i("获取得数据OkutilsObserverurl=" + url, response.body());
                    String body = response.body();
                    if (body == null || TextUtils.isEmpty(body)) {
                        e.tryOnError(new MyTrowable("服务器数据异常！", -2));
                    } else {
                        try {
//                                    LogResponBean respBean = gson.fromJson(body, LogResponBean.class);
//                                    if (respBean.getCode().equals("10007")) {
//                                        EventBus.getDefault().post(new EventMessage(Constants.type_shixiao));//重新登录
//                                        return;
//                                    }
                        } catch (Exception ex) {
                            e.tryOnError(new MyTrowable(ex.getMessage() + "json格式异常", -3));
                        }
                        try {
                            //数据发送已经完成
                            if (!clazz.equals(String.class)) {
                                e.onNext(gson.fromJson(body, clazz));
                            } else {
                                e.onNext((T) body);
                            }
                        } catch (Exception ex) {
                            e.tryOnError(new MyTrowable(ex.getMessage() + "json格式异常", -3));
                        }
                    }
                }

                @Override
                public void onError(Response<String> response) {
                    Throwable exception = response.getException();
                    MyLog.e("获取得数据OkutilsObserver erro", "url=" + url + " exception = " + exception.getMessage());
                    e.tryOnError(exception);
                    e.onComplete();
                }

                @Override
                public void uploadProgress(Progress progress) {
                    super.uploadProgress(progress);
                    MyLog.i("返回的进度", progress.fraction + "");
                }
            });
        });
    }


    public static <T> Observable<T> requsetJsonparmget(String url, final Context mContext, Class<T> clazz) {
        MyLog.i("发送数据OkutilsObserver qingqiu", "url=" + url);
        return Observable.create((ObservableEmitter<T> e) -> {
            OkGo.<String>get(url)
                    .tag(mContext)
                    //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                    // .params(params)//  这里不要使用params，upJson 与 params 是互斥的，只有 upJson 的数据会被上传
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String name3 = Thread.currentThread().getName();
                            MyLog.i("定义的当前线程3", name3);
                            MyLog.i("获取得数据OkutilsObserverurl=" + url, response.body());
                            String body = response.body();
                            if (body == null || TextUtils.isEmpty(body)) {
                                e.tryOnError(new MyTrowable("服务器数据异常！", -2));
                            } else {
                                try {
//                                    LogResponBean respBean = gson.fromJson(body, LogResponBean.class);
//                                    if (respBean.getCode().equals("10007")) {
//                                        EventBus.getDefault().post(new EventMessage(Constants.type_shixiao));//重新登录
//                                        return;
//                                    }
                                } catch (Exception ex) {
                                    e.tryOnError(new MyTrowable(ex.getMessage() + "json格式异常", -3));
                                }
                                try {
                                    //数据发送已经完成
                                    if (!clazz.equals(String.class)) {
                                        e.onNext(gson.fromJson(body, clazz));
                                    } else {
                                        e.onNext((T) body);
                                    }
                                } catch (Exception ex) {
                                    e.tryOnError(new MyTrowable(ex.getMessage() + "json格式异常", -3));
                                }
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Throwable exception = response.getException();
                            MyLog.e("获取得数据OkutilsObserver erro", "url=" + url + " exception = " + exception.getMessage());
                            e.tryOnError(exception);
                            e.onComplete();
                        }
                    });
        });
    }

    public static <T> Observable<T> requsetJsonparm(String url, final Context mContext, Object obj, Class<T> clazz) {
        String json = gson.toJson(obj);
        MyLog.i("发送数据OkutilsObserver qingqiu", "url=" + url + "  json = " + json);
        return Observable.create((ObservableEmitter<T> e) -> {
            OkGo.<String>post(url)
                    .tag(mContext)
                    //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                    // .params(params)//  这里不要使用params，upJson 与 params 是互斥的，只有 upJson 的数据会被上传
                    .upJson(json)//setCertificates
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            MyLog.i("获取得数据OkutilsObserverurl=" + url, response.body());
                            String body = response.body();
                            if (body == null || TextUtils.isEmpty(body)) {
                                e.tryOnError(new MyTrowable("服务器数据异常！", -2));
                            } else {
                                try {
//                                    LogResponBean respBean = gson.fromJson(body, LogResponBean.class);
//                                    if (respBean.getCode().equals("10007")) {
//                                        EventBus.getDefault().post(new EventMessage(Constants.type_shixiao));//重新登录
//                                        return;
//                                    }
                                } catch (Exception ex) {
                                    e.tryOnError(new MyTrowable(ex.getMessage() + "json格式异常", -3));
                                }
                                try {
                                    //数据发送已经完成
                                    if (!clazz.equals(String.class)) {
                                        e.onNext(gson.fromJson(body, clazz));
                                    } else {
                                        e.onNext((T) body);
                                    }
                                } catch (Exception ex) {
                                    e.tryOnError(new MyTrowable(ex.getMessage() + "json格式异常", -3));
                                }
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Throwable exception = response.getException();
                            MyLog.e("获取得数据OkutilsObserver erro", "url=" + url + " exception = " + exception.getMessage());
                            e.tryOnError(exception);
                            e.onComplete();
                        }
                    });
        });
    }
}
