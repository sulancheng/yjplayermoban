package com.csu.xgum.ble;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.csu.xgum.R;
import com.example.mypublib.utils.MyLog;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/23.
 */
public class BleService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    private HashMap<String, BleParent> mBleParents = new HashMap<>();

    public void dilDel(String tag) {
        if (mBleParents.get(tag) != null) {
            MyLog.i("BleManagerble5", "删除设备" + tag);
            mBleParents.remove(tag);
        }
    }

    public BleParent getAndSlpshBleParent(String tag, BleDevice bleDevice) {
        BleParent bleParent = null;
        if (mBleParents.get(tag) != null) {
            MyLog.i("BleManagerble5", "我本来就有");
            return mBleParents.get(tag);
        }
        if (true) {
            bleParent = new BleControl1();
        } else {
            bleParent = new BleControl2();
        }
        if (bleDevice == null) return null;
        bleParent.setDevice(bleDevice);
        MyLog.i("BleManagerble5", "我诞生了");
        mBleParents.put(tag, bleParent);
        return bleParent;
    }

    private void initialize() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //NotificationCompat.Builder builder =
            //new NotificationCompat.Builder(this, CHANNEL_ID)
            //         .setContentTitle("")
            //         .setContentText("");
            NotificationChannel channel = new NotificationChannel("suble", "bletest", NotificationManager.IMPORTANCE_MIN);
            channel.enableVibration(false);//去除振动

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) manager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(getApplicationContext(), "suble")
                    .setContentTitle("正在后台运行")
                    .setSmallIcon(R.mipmap.ic_launcher);
            startForeground(1, builder.build());//id must not be 0,即禁止是0
        }
    }

    class MyBinder extends Binder {
        public BleService getService() {
            return BleService.this;
        }
    }

    public void conn(BleDevice bleDevice, BleGattCallback bleGattCallback) {
        boolean connected = BleManager.getInstance().isConnected(bleDevice);
        if (!connected) {//98:07:2D:43:17:90
            BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
                @Override
                public void onStartConnect() {

                    bleGattCallback.onStartConnect();
                }

                @Override
                public void onConnectFail(BleDevice bleDevice, BleException exception) {

                    bleGattCallback.onConnectFail(bleDevice, exception);
                }

                @Override
                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    bleGattCallback.onConnectSuccess(bleDevice, gatt, status);

                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {

                    bleGattCallback.onDisConnected(isActiveDisConnected, bleDevice, gatt, status);
                }
            });
        }
    }


    public void stopCon(String mac) {
        BleParent curentPar = mBleParents.get(mac);
        if (curentPar != null) {
            BleDevice device = curentPar.getDevice();
            if (device != null)
                BleManager.getInstance().disconnect(device);
        }
    }

    public void stopAllCon() {
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }
}
