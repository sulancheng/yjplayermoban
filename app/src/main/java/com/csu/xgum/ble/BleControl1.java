package com.csu.xgum.ble;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.csu.xgum.utils.MyLog;

import java.util.Arrays;

/**
 * 作者：sucheng on 2020/4/7 0007 16:43
 */
public class BleControl1 extends BleParent {
    public BleControl1() {
    }

    @Override
    public void band() {
        BleManager.getInstance().write(
                bleDevice,
                SERVER_B_UUID_SERVER.toString(),
                SERVER_B_UUID_NOTIFY.toString(),
                "AT_BOND".getBytes(),
//                data.getBytes(),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        // 发送数据到设备成功
                        MyLog.i("BleManagerble", "发送数据" + current + "  total =  " + total + "   data = " + Arrays.toString(justWrite));
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        // 发送数据到设备失败
                        MyLog.i("BleManagerble", "发送失败" + exception.toString());
                    }
                });
    }
}
