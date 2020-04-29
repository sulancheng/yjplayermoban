package com.csu.xgum.act;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.csu.xgum.R;
import com.csu.xgum.base.BaseActivity;
import com.csu.xgum.base.BaseFragment;
import com.csu.xgum.base.Myapplication;
import com.csu.xgum.bean.EventMessage;
import com.csu.xgum.bean.TabEntity;
import com.example.mypublib.utils.MyLog;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity {
    @BindView(R.id.tl_2)
    CommonTabLayout mTabLayout_2;
    private String[] mTitles = {"首页", "下单", "周边商城", "购物车", "我的"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconUnselectIds = {
            R.drawable.home_seun, R.drawable.order_seun, R.drawable.shangc_seun, R.drawable.shop_seun,
            R.drawable.me_seun};
    private int[] mIconSelectIds = {
            R.drawable.home_se, R.drawable.order_se, R.drawable.shangc_se, R.drawable.shop_se,
            R.drawable.me_se};
    private List<BaseFragment> datas;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();
        datas = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTabLayout_2.setTabData(mTabEntities);
        mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                MyLog.i("当前选中", position + "");
                mTabLayout_2.setCurrentTab(position);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.vp_main, datas.get(position));
                transaction.commit();
            }

            @Override
            public void onTabReselect(int position) {
                MyLog.i("再次选择之后调用刷新:" + position);
            }
        });
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.vp_main, datas.get(0));
        transaction.commit();
        mTabLayout_2.setCurrentTab(0);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changUiyou(EventMessage messageEvent) {
        String type = messageEvent.getType();
        MyLog.i("数据typemessage", type);

    }

    public void changTabOnlyShuax(String shopid) {
        Myapplication.getInstance().setShopid(shopid);
        datas.get(1).shuaxin();
    }

    public void changTab(int tabindex, String shopid) {
        if (tabindex == 1) {
            Myapplication.getInstance().setShopid(shopid);
            datas.get(1).shuaxin();
        }
        mTabLayout_2.setCurrentTab(tabindex);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.vp_main, datas.get(tabindex));
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                moveTaskToBack(false);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
