package com.bigkoo.snappingstepper.listener;

import android.view.View;

/**
 * Created by Sai on 16/1/15.
 */
public interface SnappingStepperValueChangeListener {
    public void onValueChange(View view ,int value,String  isbig); //超大为2   超小 为1   正常为0
}
