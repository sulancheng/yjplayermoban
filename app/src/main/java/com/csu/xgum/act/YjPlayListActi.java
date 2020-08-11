package com.csu.xgum.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.csu.xgum.R;
import com.csu.xgum.base.BaseActivity;

import butterknife.OnClick;

public class YjPlayListActi extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_yj_play_list);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_yj_play_list;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.bt_one})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_one:
                goToVideoPlayer(this, view);
                break;
        }
    }

    /**
     * 跳转到视频播放
     *
     * @param activity
     * @param view
     */
    public void goToVideoPlayer(Activity activity, View view) {
        Intent intent = new Intent(activity, YjPlayerTestActivity.class);
        intent.putExtra(YjPlayerTestActivity.TRANSITION, true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair pair = new Pair<>(view, YjPlayerTestActivity.IMG_TRANSITION);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair);//这个就是 点击的控件慢慢放大跳转到第二个页面 用的最广
//            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeCustomAnimation(this,//这个可以自定义动画
//                    R.anim.abc_fade_in, R.anim.abc_fade_out);
//            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeScaleUpAnimation(view, 此效果不明显
//                    view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }

}
