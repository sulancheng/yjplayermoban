package com.csu.xgum.ble;

import io.reactivex.Observable;

/**
 * 作者：sucheng on 2020/4/7 0007 16:43
 */
public class BleControl1 extends BleParent {
    public BleControl1() {
    }

    @Override
    public <T> Observable<T> band() {
//        return publSendMessage("AT+BOND");
        return publSendMessage("AT+BOND");
    }

}
