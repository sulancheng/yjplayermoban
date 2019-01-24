package com.yjplay.yjplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.yjplay.yjplayer.R;
import com.yjplay.yjplayer.utils.CommenUtils;
import com.yjplay.yjplayer.utils.Exit;
import com.yjplay.yjplayer.widge.LoadingDailog;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks {
    public static final int REQUESTCODE_PHOTOZOOM = 4;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final String IMAGE_FILE_LOCATION = new File(CommenUtils.getdownloadPath("/jianqie"), "temp.jpg").getPath();
    public static final int REQUESTCODE_PHOTOGRAPH = 3;
    public File file = new File(CommenUtils.getdownloadPath("/jianqie"));
    public Gson gson;
    protected LoadingDailog loadingDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_base);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.head_back));
        }
        //设置布局
        setContentView(intiLayout());
        Exit.getInstance().addActivity(this);
        partInit();
        //初始化控件
        initView();
        //设置数据
        initData();
    }

    private void partInit() {
        gson = new Gson();
        loadingDailog = new LoadingDailog(this).setMessage("加载中...").create();
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

//    @Override
////    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        switch (keyCode) {
////            case KeyEvent.KEYCODE_BACK:
////                finish();
////                return true;
////        }
////        return super.onKeyDown(keyCode, event);
////    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
    public void xiangce() {
        Intent i = new Intent(Intent.ACTION_PICK, null);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(i, REQUESTCODE_PHOTOZOOM);
    }
    private void gopaizhao() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMAGE_FILE_LOCATION)));
        startActivityForResult(intent, REQUESTCODE_PHOTOGRAPH);
    }
}
