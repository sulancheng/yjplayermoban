package com.yjplay.yjplayer.utils;

import android.app.Activity;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adminq on 2016/3/31.
 */
public class Exit {

    private List<Activity> mList = new LinkedList<Activity>();
    private static Exit instance;

    private Exit() {
    }

    public synchronized static Exit getInstance() {
        if (null == instance) {
            instance = new Exit();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void finish() {
        if (mList != null && mList.size() > 0) {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
            mList.clear();
        }
    }

    public void exit() {
        Log.i("onRequestPermission", "finish + =" + mList.size());
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
            mList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}
