package com.csu.xgum.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.csu.xgum.R;
import com.csu.xgum.bean.Constants;
import com.csu.xgum.utils.SPUtils;

import butterknife.ButterKnife;


/**
 * 作者：sucheng on 2017/11/6 14:23
 */

public abstract class BaseFragment extends com.trello.rxlifecycle2.components.support.RxFragment {
    protected Context mContext;
    private View view;
    protected Gson gson;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    public abstract void shuaxin();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gson = new Gson();
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_base, container, false);
            FrameLayout fl_container = (FrameLayout) view.findViewById(R.id.trplace);
            View inflateView = View.inflate(mContext, createViewId(), null);
            fl_container.addView(inflateView);
            //绑定fragment
            ButterKnife.bind(this, view);
            viewCreated(view, savedInstanceState);
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /* @Override
     public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
     }*/
    public abstract int createViewId();
    public abstract void viewCreated(View view, Bundle savedInstanceState);


    public <T extends Activity> void gotActivity(Class<T> xx) {
        Intent intent = new Intent(mContext, xx);
        mContext.startActivity(intent);
    }
    protected boolean isCanGo() {
        String token = SPUtils.getString(mContext, Constants.TOKEN);
        return token != null && !TextUtils.isEmpty(token);
    }

}
