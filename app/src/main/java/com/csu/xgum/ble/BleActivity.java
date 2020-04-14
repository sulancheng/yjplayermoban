package com.csu.xgum.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.csu.xgum.R;
import com.csu.xgum.base.BaseActivity;
import com.csu.xgum.utils.MyLog;
import com.csu.xgum.widge.PublicObserver;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BleActivity extends BaseActivity {
    @BindView(R.id.rcv)
    RecyclerView rcv;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List allDatas;
    private DeviceItemAdapter deviceItemAdapter;
    private BleService mblservice;
    private MyServiceConn conn;
    private Intent serviceIntent;

    //https://github.com/Jasonchenlijian/FastBle/wiki
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ble);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_ble;
    }

    @Override
    public void initView() {
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)//默认打开库中的运行日志，如果不喜欢可以关闭
                .setReConnectCount(3, 2000)//设置连接时重连次数和重连间隔（毫秒），默认为0次不重连
                .setSplitWriteNum(20)//设置分包发送的时候，每一包的数据长度，默认20个字节
                .setConnectOverTime(15000)//设置连接超时时间（毫秒），默认10秒
                .setOperateTimeout(5000);//设置readRssi、setMtu、write、read、notify、indicate的超时时间（毫秒），默认5秒

        sCanRule();
        initRecyc();
        initService();
    }

    public class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //mblservice = (Iservice) service;
            BleService.MyBinder mBinder = (BleService.MyBinder) service;
            mblservice = mBinder.getService();
            MyLog.i("BleUtils555", "绑定service成功");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void initService() {
        serviceIntent = new Intent(this, BleService.class);
        conn = new MyServiceConn();
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void initRecyc() {
        LinearLayoutManager lay_hot = new LinearLayoutManager(this);
        rcv.setLayoutManager(lay_hot);
        allDatas = new ArrayList<>();
        deviceItemAdapter = new DeviceItemAdapter(allDatas);
        deviceItemAdapter.setOnItemChildClickListener((adapter, view12, position) -> {

        });
        deviceItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            BleDevice bleDevice = (BleDevice) adapter.getItem(position);
            connectDev(bleDevice);
        });
        rcv.setAdapter(deviceItemAdapter);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            startScan();
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
        });
        refreshLayout.autoRefresh(500);
    }

    @Override
    public void initData() {

    }

    private void sCanRule() {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
//                .setDeviceName(true, names)         // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(6000)              // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    public void openBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BleManager.getInstance().getBluetoothAdapter();
        // 判断设备是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_LONG).show();
            return;
        }
        // 判断蓝牙是否打开
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();  // 直接打开蓝牙
            refreshLayout.finishRefresh();
            refreshLayout.autoRefresh();
        }
    }

    private void startScan() {
        openBluetooth();
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                // 开始扫描（主线程）
                MyLog.i("BleManagerble", "开始扫描" + success);
                if (!success) {
                    refreshLayout.finishRefresh();
                } else {
                    allDatas.clear();
                }
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                // 扫描到一个符合扫描规则的BLE设备（主线程）
                MyLog.i("BleManagerble", "扫描1" + bleDevice.getMac());
                allDatas.add(bleDevice);
                deviceItemAdapter.setNewData(allDatas);
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                MyLog.i("BleManagerble", "扫描结束" + scanResultList.size());
                refreshLayout.finishRefresh();
                // 扫描结束，列出所有扫描到的符合扫描规则的BLE设备（主线程）
                allDatas.clear();
                allDatas.addAll(scanResultList);
                deviceItemAdapter.setNewData(allDatas);
            }
        });

    }

    private void connectDev(BleDevice device) {
        BleManager.getInstance().cancelScan();
        if (mblservice != null)
            mblservice.conn(device, new BleGattCallback() {
                @Override
                public void onStartConnect() {
                    // 开始连接
                    MyLog.i("BleManagerble", "开始连接" + device.getMac());
                }

                @Override
                public void onConnectFail(BleDevice bleDevice, BleException exception) {
                    // 连接失败
                    MyLog.i("BleManagerble", "连接失败" + bleDevice.getMac());
                }

                @Override
                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    // 连接成功，BleDevice即为所连接的BLE设备
                    MyLog.i("BleManagerble", "连接成功" + bleDevice.getMac());
//                    BleParent bbe = BleUtils.getBbe(bleDevice);
//                    bbe.band();
                    currMac = bleDevice.getMac();
                    initPart(currMac, bleDevice);

                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                    // 连接中断，isActiveDisConnected表示是否是主动调用了断开连接方法
                    MyLog.i("BleManagerble", "连接中断" + device.getMac());
                }
            });

    }

    private void initPart(String currMac, BleDevice bleDevice) {
        mblservice.getAndSlpshBleParent(currMac, bleDevice).oppenNotify(new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                // 打开通知操作成功
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                // 打开通知操作失败
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                // 打开通知后，设备发过来的数据将在这里出现
                MyLog.i("BleManagerble", "收到数据 data" + Arrays.toString(data));
            }
        });
        sendData(currMac, bleDevice, "");
    }

    private String currMac = "";

    @OnClick({R.id.bt_senddata})
    public void click(View view) {
        sendData(currMac, null, "");
    }

    private void sendData(String tag, BleDevice bleDevice, String data) {
        Observable.just(mblservice.getAndSlpshBleParent(tag, bleDevice))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(a -> a.band())
                .compose(BleActivity.this.bindUntilEvent(ActivityEvent.DESTROY))//内部传入指定生命周期参数 指定 RxJava的自动取消订阅
                .observeOn(Schedulers.io())
                .doOnError(erro -> MyLog.i("BleManagerble5", erro.getMessage()))
                .subscribe(new PublicObserver());
    }

    public class DeviceItemAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

        public DeviceItemAdapter(@Nullable List<T> data) {
            super(R.layout.sc_device_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, T item) {
//            int layoutPosition = helper.getLayoutPosition();
//            List<T> data = getData();
//            if (layoutPosition == data.size() - 1) {
//                View itemView = helper.itemView;
//                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
//                int i = DensityUtil.dip2px(mContext, 16);
//                layoutParams.setMargins(i, i, i, i);
//                itemView.setLayoutParams(layoutParams);
//            }

            BleDevice device = (BleDevice) item;
            String name = device.getName();
            if (name == null || name.equals("")) {
                name = String.valueOf(device.getRssi());
            }
            helper.setText(R.id.tv_name, name);
            helper.setText(R.id.tv_mac, device.getMac());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BleManager.getInstance().disconnect(bleDevice);
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        unbindService(conn);
        stopService(serviceIntent);
    }
}
