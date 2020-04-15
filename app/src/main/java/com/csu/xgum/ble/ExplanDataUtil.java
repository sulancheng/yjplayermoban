package com.csu.xgum.ble;

/**
 * 作者：sucheng on 2020/4/15 0015 09:09
 */
public class ExplanDataUtil {

    private BatteryExplan batteryExplan;

    public ExplanDataUtil() {

    }


    public void explan(byte[] data) {
        switch (data[0]) {
            case 3://解析电量
                if (batteryExplan == null) {
                    batteryExplan = new BatteryExplan();
                }
                batteryExplan.parse(data);
                break;

            default:
                break;
        }
    }
}
