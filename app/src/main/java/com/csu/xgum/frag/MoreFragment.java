package com.csu.xgum.frag;

import android.os.Bundle;
import android.view.View;

import com.flyco.tablayout.widget.MsgView;
import com.csu.xgum.R;
import com.csu.xgum.base.BaseFragment;

import static com.flyco.tablayout.utils.UnreadMsgUtils.show;

/**
 * 作者：sucheng on 2019/8/6 17:14
 */
public class MoreFragment extends BaseFragment {


    @Override
    public void shuaxin() {

    }

    @Override
    public void viewCreated(View view, Bundle savedInstanceState) {
//        RxView.clicks(ll_jifen)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(a -> gotActivity(JIfenActivity.class));

        setLoginOrNOTType(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        getPersonMessage();
    }

    @Override
    public int createViewId() {
        return R.layout.fragment_more;
    }

    private void getPersonMessage() {

    }

    private void changeUi() {

    }

    private void setLoginOrNOTType(boolean islogin) {

    }

    public void showMsgDoc(int count, MsgView tarView) {
        if (count <= 0) {
            tarView.setVisibility(View.GONE);
        } else {
            tarView.setVisibility(View.VISIBLE);
            show(tarView, count);
        }
    }
}
