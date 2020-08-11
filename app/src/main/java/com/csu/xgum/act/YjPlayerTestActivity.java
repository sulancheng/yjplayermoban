package com.csu.xgum.act;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.csu.xgum.R;
import com.csu.xgum.base.BaseActivity;
import com.csu.xgum.listener.OnTransitionListener;
import com.example.mypublib.utils.MyLog;
import com.example.mypublib.widge.MyCustomVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import butterknife.BindView;

public class YjPlayerTestActivity extends BaseActivity {
    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";

    @BindView(R.id.my_player)
    public MyCustomVideo detailPlayer;
    private GSYVideoOptionBuilder gsyVideoOption;
    private OrientationUtils orientationUtils;
    private boolean isTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_yj_player_test);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_yj_player_test;
    }

    private boolean isPlay;
    private boolean isPause;

    @Override
    public void initView() {
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        initVideoSet();
        initTransition();
    }

    private void initVideoSet() {
        String url = "http://mp4.vjshi.com/2016-04-14/8d38b2fa34abf98e5677788900f7ed51.mp4";
        String url2 = "http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4";
        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        String name = "普通";

        String source2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
        String name2 = "清晰";

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        gsyVideoOption = new GSYVideoOptionBuilder();

        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);

        detailPlayer.setEnlargeImageRes(R.drawable.full_screen);
        detailPlayer.setShrinkImageRes(R.drawable.nofull);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        setbuild(source1, "测试一");
        detailPlayer.getFullscreenButton().setOnClickListener(v -> {
            //直接横屏
            orientationUtils.resolveByClick(); //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
//            detailPlayer.startWindowFullscreen(YjPlayerTestActivity.this, true, true);
        });
        //设置返回按键功能
        detailPlayer.getBackButton().setOnClickListener(v -> clearPlayerAll());

        detailPlayer.setGSYVideoProgressListener((progress, secProgress, currentPosition, duration) -> {
            MyLog.i(Tag, "进度progress = " + progress);
            if (progress > 5) {
                detailPlayer.onVideoPause();//暂停
            }
        });
//        detailPlayer.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //开始播放了才能旋转和全屏
//                orientationUtils.setEnable(true);
//                isPlay = true;
//                detailPlayer.startAfterPrepared();
//                detailPlayer.getFullscreenButton().callOnClick();
//            }
//        }, 800);
//        detailPlayer.startPlayLogic();
    }


    public final String Tag = "YjPlayerTestActivity";

    private void setbuild(String url, String tit) {
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);
        gsyVideoOption.setThumbImageView(imageView)
                .setIsTouchWiget(true) //是否可以滑动调整
                .setRotateViewAuto(false)
                .setLockLand(false)
                /**
                 * 是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏，注意，这时候默认旋转无效
                 * @param autoFullWithSize 默认false
                 */
                .setAutoFullWithSize(true)
                .setShowFullAnimation(true)
                .setSeekRatio(1)
                .setNeedLockFull(true)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle(tit)
                .setNeedShowWifiTip(true)
                .setStartAfterPrepared(true)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);

                        MyLog.i(Tag, "准备好播放了");
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;

                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        MyLog.i(Tag, "退出全屏");
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        MyLog.i(Tag, "进入全屏");

                    }
                }).setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        }).build(detailPlayer);
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                MyLog.i(Tag, "调用返回了1");

                clearPlayerAll();

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPause() {
        detailPlayer.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        detailPlayer.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.i(Tag, "调用返回了4");
        if (isPlay) {
            detailPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    public void clearPlayerAll() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        //先返回正常状态
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
//        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            detailPlayer.getFullscreenButton().performClick();
//            return;
//        }
        //释放所有
        detailPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
        super.onBackPressed();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MyLog.i(Tag, "调用返回了2");
            ActivityCompat.finishAfterTransition(this);//进行退出动画。
        } else {
            MyLog.i(Tag, "调用返回了3");
            new Handler().postDelayed(() -> {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }, 500);
        }

    }

    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(detailPlayer, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            detailPlayer.startPlayLogic();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        Transition transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    detailPlayer.startPlayLogic();
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }
}
