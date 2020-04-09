package com.csu.xgum.ble;

import com.clj.fastble.data.BleDevice;

/**
 * 作者：sucheng on 2020/4/7 0007 16:52
 */
public class BleUtils {
    private BleUtils() {

    }

    public static BleParent getBbe(BleDevice bleDevice) {
        BleParent bleParent = null;
        if (true) {
            bleParent = new BleControl1();
        } else {
            bleParent = new BleControl2();
        }
        bleParent.setDevice(bleDevice);
        return bleParent;
    }
}
