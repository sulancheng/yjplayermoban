package com.yjplay.yjplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yjplay.yjplayer.R;

import vodplayerview.activity.AliyunPlayerSkinActivity;

public class AliyunPlayerTActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_aliyun_player_t);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_aliyun_player_t;
    }

    @Override
    public void initView() {
        //跳转到 阿里云播放器中
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AliyunPlayerTActivity.this, AliyunPlayerSkinActivity.class));
            }
        });
//        AliyunVodPlayerView mAliyunVodPlayerView = (AliyunVodPlayerView)findViewById(com.aliyun.vodplayer.R.id.video_view);
//        //保持屏幕敞亮
//        mAliyunVodPlayerView.setKeepScreenOn(true);
//        PlayParameter.PLAY_PARAM_URL = "http://mp4.vjshi.com/2016-04-14/8d38b2fa34abf98e5677788900f7ed51.mp4";
//        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
//        mAliyunVodPlayerView.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/);
//        mAliyunVodPlayerView.setTheme(AliyunVodPlayerView.Theme.Blue);
//        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
//        alsb.setSource("http://mp4.vjshi.com/2016-04-14/8d38b2fa34abf98e5677788900f7ed51.mp4");
//        alsb.setTitle("nihao");
//        AliyunLocalSource localSource = alsb.build();
//        //mAliyunVodPlayerView.setCirclePlay(true);
//        mAliyunVodPlayerView.setAutoPlay(true);
//        mAliyunVodPlayerView.enableNativeLog();
    }

    @Override
    public void initData() {
//        String url = getIntent().getStringExtra("url");
//        AliyunLocalSource.AliyunLocalSourceBuilder asb = new AliyunLocalSource.AliyunLocalSourceBuilder();
//        asb.setSource("http://mp4.vjshi.com/2016-04-14/8d38b2fa34abf98e5677788900f7ed51.mp4");
//        //aliyunVodPlayer.setLocalSource(asb.build());
//        AliyunLocalSource mLocalSource = asb.build();
//        aliyunVodPlayer.prepareAsync(mLocalSource);
    }
}
