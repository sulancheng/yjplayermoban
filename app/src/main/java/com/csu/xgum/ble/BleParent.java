package com.csu.xgum.ble;

import com.clj.fastble.data.BleDevice;

import java.util.UUID;

/**
 * 作者：sucheng on 2020/4/7 0007 16:42
 */
public class BleParent {
    public final UUID SERVER_B_UUID_SERVER = UUID.fromString("0000190B-0000-1000-8000-00805f9b34fb");
    public final UUID SERVER_B_UUID_REQUEST = UUID.fromString("00000003-0000-1000-8000-00805f9b34fb");//WRITE
    public final UUID SERVER_B_UUID_NOTIFY = UUID.fromString("00000004-0000-1000-8000-00805f9b34fb");//NOTIFY
    public final UUID SERVER_C_UUID_NOTIFY = UUID.fromString("00000006-0000-1000-8000-00805f9b34fb");//NOTIFY

    public BleDevice bleDevice;

    public void setDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public void band() {
        throw new RuntimeException("处理绑定命令");
    }
}
