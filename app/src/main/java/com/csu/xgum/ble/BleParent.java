package com.csu.xgum.ble;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.csu.xgum.bean.MyTrowable;
import com.csu.xgum.utils.MyLog;

import java.util.Arrays;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * 作者：sucheng on 2020/4/7 0007 16:42
 */
public class BleParent {
/*    public final UUID SERVER_B_UUID_SERVER = UUID.fromString("0000190B-0000-1000-8000-00805f9b34fb");
    public final UUID SERVER_B_UUID_REQUEST = UUID.fromString("00000003-0000-1000-8000-00805f9b34fb");//WRITE
    public final UUID SERVER_B_UUID_NOTIFY = UUID.fromString("00000004-0000-1000-8000-00805f9b34fb");//NOTIFY
    public final UUID SERVER_C_UUID_NOTIFY = UUID.fromString("00000006-0000-1000-8000-00805f9b34fb");//NOTIFY*/


    public final UUID SERVER_B_UUID_SERVER = UUID.fromString("0000FFF0-0000-1000-8000-00805f9b34fb");
    public final UUID SERVER_B_UUID_WRITE = UUID.fromString("0000FFF1-0000-1000-8000-00805f9b34fb");
    public final UUID SERVER_B_UUID_RESPONE = UUID.fromString("0000FFF2-0000-1000-8000-00805f9b34fb");
    public final UUID SERVER_B_UUID_NOTIFY = UUID.fromString("0000FFF3-0000-1000-8000-00805f9b34fb");//NOTIFY
    public BleDevice bleDevice;

    public void oppenNotify(BleNotifyCallback callback) {
        BleManager.getInstance().notify(
                bleDevice,
                SERVER_B_UUID_SERVER.toString(),
                SERVER_B_UUID_NOTIFY.toString(),
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        MyLog.i("BleManagerble", "打开通知成功");
                        // 打开通知操作成功
                        callback.onNotifySuccess();
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        MyLog.i("BleManagerble", "打开通知失败");
                        // 打开通知操作失败
                        callback.onNotifyFailure(exception);
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        MyLog.i("BleManagerble", "收到数据 data" + Arrays.toString(data));
                        callback.onCharacteristicChanged(data);
                        // 打开通知后，设备发过来的数据将在这里出现
                    }
                });
    }

    public void setDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public <T> Observable<T> band() {
        throw new RuntimeException("处理绑定命令");
    }


    public class ReturnDate {
        private int current;
        private int total;
        private byte[] justWrite;

        public ReturnDate(int current, int total, byte[] justWrite) {
            this.current = current;
            this.total = total;
            this.justWrite = justWrite;
        }
    }

    public <T> Observable<T> publSendMessage(String data) {
        byte[] b = new byte[]{(byte)0x03,(byte)0x01};
        return Observable.create((ObservableEmitter<T> e) -> {
            BleManager.getInstance().write(
                    bleDevice,
                    SERVER_B_UUID_SERVER.toString(),
                    SERVER_B_UUID_WRITE.toString(),
                    b,
//                data.getBytes(),
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            // 发送数据到设备成功
                            MyLog.i("BleManagerble", "发送数据" + current + "  total =  " + total + "   data = " + Arrays.toString(justWrite));
                            e.onNext((T) new ReturnDate(current, total, justWrite));
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            // 发送数据到设备失败
                            MyLog.i("BleManagerble", "发送失败" + exception.toString());
                            e.tryOnError(new MyTrowable(exception.getDescription() + "  bleDevice = " + bleDevice.getMac(), exception.getCode()));
                        }
                    });
        });
    }
}
