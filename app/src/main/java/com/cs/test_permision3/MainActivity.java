package com.cs.test_permision3;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnMain = (Button) findViewById(R.id.btn_main);
        mBtnMain.setOnClickListener(this);
    }

    private void callPhone() {
        //没有授予权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
            //做权限处理,主要是申请权限
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
        }else {
            doCallPhone();
        }
    }

    private void doCallPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+"10086"));
        startActivity(intent);//代码检查工具,不需要处理
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main:
                callPhone();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            //申请了权限
            case 1:
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                doCallPhone();
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("帮助")
                        .setMessage("当前应用缺少必要权限.请点击'设置'-'权限',打开所需权限.最后点击两次后退按钮,即可返回")
                        .setCancelable(false)
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
                AlertDialog alert = builder.create();
            }
            break;
        }
    }
}
