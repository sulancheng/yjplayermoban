package com.yjplay.yjplayer.widge;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.yjplay.yjplayer.R;

/**
 * Created by Administrator
 * on 2019/1/23 0023.
 */
public class MyCustomVideo extends StandardGSYVideoPlayer {


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
    }

    @Override
    public int getLayoutId() {
        return R.layout.custom_video;
    }
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        MyCustomVideo sampleCoverVideo = (MyCustomVideo) gsyBaseVideoPlayer;
//        sampleCoverVideo.loadCoverImage(mCoverOriginUrl, mDefaultRes);
        return gsyBaseVideoPlayer;
    }


    @Override
    public GSYBaseVideoPlayer showSmallVideo(Point size, boolean actionBar, boolean statusBar) {
        //下面这里替换成你自己的强制转化
        MyCustomVideo sampleCoverVideo = (MyCustomVideo) super.showSmallVideo(size, actionBar, statusBar);
        sampleCoverVideo.mStartButton.setVisibility(GONE);
        sampleCoverVideo.mStartButton = null;
        return sampleCoverVideo;
    }
}
