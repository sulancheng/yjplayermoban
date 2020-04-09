package com.csu.xgum.receiver;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 * */
public class MyJPushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG = "MyJPushMessageReceiver";

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        myonTagOperatorResult(context,jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage){
//        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
        int sequence = jPushMessage.getSequence();
        Log.i(TAG,"action - onCheckTagOperatorResult, sequence:"+sequence+",checktag:"+jPushMessage.getCheckTag());
        super.onCheckTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        myonAliasOperatorResult(context,jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
        int sequence = jPushMessage.getSequence();
        Log.i(TAG,"action - onMobileNumberOperatorResult, sequence:"+sequence+",mobileNumber:"+jPushMessage.getMobileNumber());
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
    public void myonTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG,"action - onTagOperatorResult, sequence:"+sequence+",tags:"+jPushMessage.getTags());
        Log.i(TAG,"tags size:"+jPushMessage.getTags().size());

    }
    public void myonAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG,"action - onAliasOperatorResult, sequence:"+sequence+",alias:"+jPushMessage.getAlias());

    }
}
