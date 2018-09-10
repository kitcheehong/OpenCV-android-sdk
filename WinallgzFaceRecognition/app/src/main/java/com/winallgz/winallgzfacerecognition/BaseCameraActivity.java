package com.winallgz.winallgzfacerecognition;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by kitchee on 2018/9/7.
 * desc:
 */

public class BaseCameraActivity extends AppCompatActivity {

    // 要校验的权限
    private final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    // 识别请求码
    private final int REQUEST_CODE_DETECTION = 0;

    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 保持手机不息屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        mToast = new Toast(getApplicationContext());
        mToast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);

        // 请求权限
        requestCameraPermission();

    }

    private void requestCameraPermission() {
        // SDK >= 23
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 检查当前权限是否已经授予
            if(checkSelfPermission(PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED){
               // 没有授予，去请求
                requestPermissions(PERMISSIONS,REQUEST_CODE_DETECTION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // grantResults数组与权限字符串数组对应，里面存放权限申请结果
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(BaseCameraActivity.this,"已授权",Toast.LENGTH_SHORT).show();
            // 往下的业务开启执行

        }else{
            Toast.makeText(BaseCameraActivity.this,"拒绝授权",Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 显示缺少权限的对话框
     */
    protected void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请求权限");
        builder.setMessage("Android 6.0+ 动态请求相机权限");
        builder.setPositiveButton("去设置权限", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                startAppSettings(getApplicationContext());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 显示没有安装OpenCV Manager的对话框
     */
    protected void showInstallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("您还没有安装OpenCV Manager");
        builder.setMessage("是否下载安装？");
        builder.setPositiveButton("去下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "去下载", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/kongqw/FaceDetectLibrary/tree/opencv3.2.0/OpenCVManager")));
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    private static void startAppSettings(Context context) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public void showLongToast(String message){
        mToast.setDuration(Toast.LENGTH_LONG);
        showToast(message);
    }

    public void showShortToast(String message){
        mToast.setDuration(Toast.LENGTH_SHORT);
        showToast(message);
    }

    private void showToast(String message){
        mToast.setText(message);
        mToast.show();
    }
}
