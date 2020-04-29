package com.csu.xgum.act;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.csu.xgum.R;
import com.csu.xgum.base.BaseActivity;

import java.util.Arrays;
import java.util.List;

import dolphin.tools.util.ToastUtil;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class StartActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private String[] permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_start;
    }

    private void init() {
        permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, permissions)) {
            quanxian(Arrays.asList(permissions));
        } else {
            EasyPermissions.requestPermissions(this, "需要读写本地权限",
                    007, permissions);
        }
    }

    private void quanxian(List<String> perms) {
        boolean all = false;
        for (String quan : perms) {
            if (EasyPermissions.hasPermissions(this, quan)) {
                all = true;
            } else {
                all = false;
                break;
            }
        }
        if (all) {
            // 如果所有的权限都授予了
            gotoNext();
        } else {
            // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
            showHintDialog();
        }
    }

    @Override
    public void initView() {
        init();
    }

    @Override
    public void initData() {

    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//      Log.i(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        if (requestCode == 007) {
            quanxian(Arrays.asList(permissions));
        }
    }

    private void gotoNext() {
        Intent intent = new Intent(this, DiscreteScrollViewTest.class);
        startActivity(intent);
        finish();
    }

    //  失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        MyLog.i(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        ToastUtil.shortShow(this, "没有该权限，此应用程序可能无法正常工作。");
        //(可选的)检查用户是否拒绝授权权限，并且点击了“不再询问”
        //下面的语句，展示一个对话框指导用户在应用设置里授权权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //new AppSettingsDialog.Builder(this).build().show();
            //这里需要重新设置Rationale和title，否则默认是英文格式
            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
        }
    }

    //显示询问用户是否更新APP的dialog
    private void showHintDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher)
                .setTitle("必需权限")
                .setMessage("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setPositiveButton("去设置", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                    finish();
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
