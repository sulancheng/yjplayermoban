package com.example.mypublib.utils;

import android.content.Context;

import java.util.Random;

/**
 * Created by Administrator
 * on 2017/10/16.
 */

public class CommonUtil {
    /**
     * 微信支付签名算法sign
     * @param characterEncoding 签名编码（UTF-8)
     * @param parameters 要签名的参数的集合
     * @param key 商户自己设置的key
     *            key 商户Key
     *            ◆ key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
     */
//    public static String createSign256(String characterEncoding, SortedMap<Object,Object> parameters, String key){
//        StringBuffer sb = new StringBuffer();
//        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
//        Iterator it = es.iterator();
//        while(it.hasNext()) {
//            Map.Entry entry = (Map.Entry)it.next();
//            String k = (String)entry.getKey();
//            Object v = entry.getValue();
//            if(null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
//                sb.append(k + "=" + v + "&");
//            }
//        }
//        sb.append("key=" + key);
//        MyLog.i("签名完成1："+sb.toString());
//        //String sign = WxMd5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
//        String sign="";
//        try {
//             sign = WXPayUtil.HMACSHA256(sb.toString(), key);
//        } catch (Exception e) {
//            e.printStackTrace();
//            MyLog.i("签名完成3：错误");
//        }
//        MyLog.i("签名完成2："+sign);
//        return sign;
//    }
    /**
     * 获得随机字符串
     * @return
     */
    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
