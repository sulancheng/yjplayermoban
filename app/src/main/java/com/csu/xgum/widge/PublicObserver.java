package com.csu.xgum.widge;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by sucheng
 * on 2019/5/21.
 */
public class PublicObserver implements Observer {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable e) {
//        MyLog.e("publicerro", e.getMessage());
    }

    @Override
    public void onComplete() {

    }
}
