package com.csu.xgum.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.csu.xgum.R;
import com.csu.xgum.bean.Constants;
import com.csu.xgum.utils.CommenUtils;
import com.csu.xgum.utils.Exit;
import com.csu.xgum.utils.SPUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends RxAppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final int REQUESTCODE_PHOTOZOOM = 4;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final String IMAGE_FILE_LOCATION = new File(CommenUtils.getdownloadPath("/jianqie"), "temp.jpg").getPath();
    public static final int REQUESTCODE_PHOTOGRAPH = 3;
    public File file = new File(CommenUtils.getdownloadPath("/jianqie"));
    public Gson gson;
    protected final int pageSize = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_base);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.out_to_left);
        //设置允许通过ActivityOptions.makeSceneTransitionAnimation发送或者接收Bundle
//        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        //设置使用TransitionManager进行动画，不设置的话系统会使用一个默认的TransitionManager
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.zhuantlan));
        }
        //设置布局
        setContentView(intiLayout());
        Exit.getInstance().addActivity(this);
        partInit();
        //初始化控件
        initView();
        //设置数据
        initData();
        reiInit(savedInstanceState);
    }
    protected boolean isCanGo() {
        String token = SPUtils.getString(this, Constants.TOKEN);
        return token != null && !TextUtils.isEmpty(token);
    }
    protected void reiInit(Bundle savedInstanceState) {

    }

    private void partInit() {
        gson = new Gson();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int intiLayout();

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 设置数据
     */
    public abstract void initData();

    /**
     * 使用频率高 一般用于Activity初始化界面
     * 例如 onCreate()里。初始化布局就用到setContentView(R.layout.xxxx) 就是初始化页面布局
     */
    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        //Butter Knife初始化
        ButterKnife.bind(this);
    }

    /**
     * 这个一般用于加载自定义控件，或者系统空间的布局
     */
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        //Butter Knife初始化
        ButterKnife.bind(this);
    }

    /**
     *
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        //Butter Knife初始化
        ButterKnife.bind(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void paizhao() {//剪切
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, permissions)) {
//            LogcatFileManager.getInstance().start(CommenUtils.getdownloadPath("logs"));//异常收集
            gopaizhao();
        } else {
            EasyPermissions.requestPermissions(this, "需要照相机的使用权限", 8, permissions);
        }

    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        gopaizhao();
    }

    //  失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //(可选的)检查用户是否拒绝授权权限，并且点击了“不再询问”
        //下面的语句，展示一个对话框指导用户在应用设置里授权权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，无法拍照")
                    .setTitle("权限")
                    .build()
                    .show();
        }
    }

    public void clickBack() {
        RxView.clicks(findViewById(R.id.ll_back))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(a -> finish());
    }

    public void xiangce() {
        Intent i = new Intent(Intent.ACTION_PICK, null);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(i, REQUESTCODE_PHOTOZOOM);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void gopaizhao() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", new File(IMAGE_FILE_LOCATION));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMAGE_FILE_LOCATION)));
        startActivityForResult(intent, REQUESTCODE_PHOTOGRAPH);
    }


    @Override
    public void finish() {
        super.finish();
    }

    public <T extends Activity> void gotActivity(Context mcont, Class<T> xx) {
        Intent intent = new Intent(mcont, xx);
        mcont.startActivity(intent);
    }

}
