package com.csu.xgum.base;

import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.csu.xgum.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by su
 * on 2017/3/24.
 */
public class Myapplication extends MultiDexApplication {
    static {
        initsmart();
    }

    private String shopid;
    //设置当前选中的店铺的shopid
    public void setCurentShopid(String shopid){
        this.shopid = shopid;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
    private static Myapplication instance;


    public static Myapplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;//存储引用
        initokgo();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }


    private static void initsmart() {
        //static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.white, R.color.headtext);//全局设置主题颜色
            ClassicsHeader classicsHeader = new ClassicsHeader(context);
//                classicsHeader.setLastUpdateText("上次更新 3秒前");//手动更新时间文字设置（将不会自动更新时间）
            classicsHeader.setEnableLastTime(false);//关闭时间显示
            classicsHeader.setTextSizeTitle(14);
            classicsHeader.setDrawableSize(18);
//            classicsHeader.setAccentColor(R.color.headtext)
            return classicsHeader;//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter classicsFooter = new ClassicsFooter(context);
            classicsFooter.setTextSizeTitle(14);
            classicsFooter.setDrawableSize(18);
            return classicsFooter;
        });
    }


    private void initokgo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(15, TimeUnit.SECONDS);
//        OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.newBuilder().connectTimeout(5, TimeUnit.SECONDS)
        //.readTimeout(20, TimeUnit.SECONDS)
//                .build();
        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())               //必须调用初始化
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(1);
        //.addCommonHeaders(headers)                      //全局公共头
        //.addCommonParams(params);                       //全局公共参数

    }

}
