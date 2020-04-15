package com.csu.xgum.ble;

/**
 * 作者：sucheng on 2020/4/15 0015 09:14
 */
public class BatteryExplan extends ExplanDataPar {
    private byte[] mData= new byte[20];
    @Override
    public void parse(byte[] data) {
        System.arraycopy(data, 0, mData, 0, 20);
    }
}
