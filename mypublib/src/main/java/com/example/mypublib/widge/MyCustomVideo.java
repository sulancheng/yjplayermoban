package com.example.mypublib.widge;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mypublib.R;
import com.example.mypublib.utils.MyLog;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

/**
 * 作者：sucheng on 2020/4/8 0008 13:34
 */
public class MyCustomVideo extends StandardGSYVideoPlayer {

    public final String Tag = "MyCustomVideo";

    private TextView nextmy;

    public MyCustomVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyCustomVideo(Context context) {
        super(context);
    }

    public MyCustomVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        initview();
    }

    private void initview() {
        nextmy = findViewById(R.id.nextmy);
        nextmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.i(Tag, "点击到了555");
                nextVideo();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.custom_video;
    }

    @Override
    protected void initInflate(Context context) {
        super.initInflate(context);

    }

    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        //有自己的逻辑可实现音量的
    }

    @Override
    protected void showBrightnessDialog(float percent) {
        super.showBrightnessDialog(percent);
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
//        mChangePosition = false;
//
//        //不给触摸音量，如果需要，屏蔽下方代码即可
//        mChangeVolume = false;
//
//        //不给触摸亮度，如果需要，屏蔽下方代码即可
//        mBrightness = false;
    }

    @Override
    protected void touchDoubleUp() {
        super.touchDoubleUp();
        //不需要双击暂停
    }

    /**
     * 切换视频资源
     */
    public void nextVideo() {
        release();
        String url = "http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4";
        setUp(url, mCache, mCachePath, "测试二");
        startPlayLogic();
        hideAllWidget();
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        MyCustomVideo sampleCoverVideo = (MyCustomVideo) gsyBaseVideoPlayer;
        MyLog.i(Tag, "进入自定义全屏");
//        hideAllWidget();
        return sampleCoverVideo;
    }

    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
        MyLog.i(Tag, "进入自定义退出全屏resolveNormalVideoShow");
    }

    @Override
    public GSYBaseVideoPlayer showSmallVideo(Point size, boolean actionBar, boolean statusBar) {
        //下面这里替换成你自己的强制转化
        MyCustomVideo sampleCoverVideo = (MyCustomVideo) super.showSmallVideo(size, actionBar, statusBar);
        MyLog.i(Tag, "进入自定义小屏幕");
//        sampleCoverVideo.mStartButton.setVisibility(GONE);
//        sampleCoverVideo.mStartButton = null;
        return sampleCoverVideo;
    }

    @Override
    protected void cloneParams(GSYBaseVideoPlayer from, GSYBaseVideoPlayer to) {
        super.cloneParams(from, to);
        MyCustomVideo sf = (MyCustomVideo) from;
        MyCustomVideo st = (MyCustomVideo) to;
        st.mShowFullAnimation = sf.mShowFullAnimation;
    }
}
