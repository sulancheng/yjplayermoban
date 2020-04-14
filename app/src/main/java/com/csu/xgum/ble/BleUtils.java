package com.csu.xgum.ble;

import com.clj.fastble.data.BleDevice;
import com.csu.xgum.utils.MyLog;

import java.util.HashMap;

/**
 * 作者：sucheng on 2020/4/7 0007 16:52
 */
public class BleUtils {
    private static HashMap<String, BleParent> mBleParents = new HashMap<>();

    private BleUtils() {

    }

    public static BleParent getBbe(String tag, BleDevice bleDevice) {//tag可以使用mac
        BleParent bleParent = null;
        if (mBleParents.get(tag) != null) {
            MyLog.i("BleUtils555", "我本来就有");
            return mBleParents.get(tag);
        }
        if (true) {
            bleParent = new BleControl1();
        } else {
            bleParent = new BleControl2();
        }
        if (bleDevice == null) return null;
        bleParent.setDevice(bleDevice);
        MyLog.i("BleUtils555", "我诞生了");
        mBleParents.put(tag, bleParent);
        return bleParent;
    }
}
