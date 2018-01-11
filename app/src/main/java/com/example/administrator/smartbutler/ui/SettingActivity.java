package com.example.administrator.smartbutler.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.service.SmsService;
import com.example.administrator.smartbutler.utils.L;
import com.example.administrator.smartbutler.utils.ShareUtil;
import com.example.administrator.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    //语音播报
    private Switch sw_speak;
    //短信提醒
    private Switch sw_sms;
    //检测更新
    private LinearLayout ll_update, ll_scan, ll_qr_code, ll_my_location, ll_about;
    private TextView tv_version, tv_scan_result;

    private String versionName;
    private int versionCode;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        //语音播报
        sw_speak = (Switch) findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);
        boolean isSpeak = ShareUtil.getBoolean(this, "isSpeak", false);
        sw_speak.setChecked(isSpeak);
        //短信提醒
        sw_sms = (Switch) findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);
        boolean isSMS = ShareUtil.getBoolean(this, "isSMS", false);
        sw_sms.setChecked(isSMS);
        //检测版本
        ll_update = (LinearLayout) findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);
        //扫一扫
        ll_scan = (LinearLayout) findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);
        //二维码分享
        ll_qr_code = (LinearLayout) findViewById(R.id.ll_qr_code);
        ll_qr_code.setOnClickListener(this);
        //我的位置
        ll_my_location = (LinearLayout) findViewById(R.id.ll_my_location);
        ll_my_location.setOnClickListener(this);
        //关于软件
        ll_about = (LinearLayout) findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);

        tv_version = (TextView) findViewById(R.id.tv_version);

        try {
            getVersionNameCode();
            tv_version.setText("检测版本：" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText("检测版本");
        }

        tv_scan_result = (TextView) findViewById(R.id.tv_scan_result);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sw_speak:
                //切换相反
                sw_speak.setSelected(!sw_speak.isSelected());
                //保存状态
                ShareUtil.putBoolean(this, "isSpeak", sw_speak.isChecked());
                break;
            case R.id.sw_sms:
                sw_sms.setSelected(!sw_sms.isSelected());
                ShareUtil.putBoolean(this, "isSMS", sw_sms.isChecked());
                if (sw_sms.isChecked()){
                    startService(new Intent(this, SmsService.class));
                }else{
                    stopService(new Intent(this, SmsService.class));
                }
                break;
            case R.id.ll_update:
                /**
                 * 1、请求服务器的配置文件，拿到code
                 * 2、比较
                 * 3、dialog提示
                 * 4、传值跳转到更新界面，并且把url传递过去
                 */
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        L.i("json:" + t);
                        parsingJson(t);
                    }
                });
                break;
            case R.id.ll_scan:
                applyCameraPermission();
                break;
            case R.id.ll_qr_code:
                startActivity(new Intent(this, QrCodeActivity.class));
                break;
            case R.id.ll_my_location:
                startActivity(new Intent(this, LocationActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            if (code > versionCode){
                showUpdateDialog(jsonObject.getString("content"));
            }else{
                Toast.makeText(this, "当前是最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this)
                .setTitle("有新版本啦！")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(SettingActivity.this, UpdateActivity.class);
                        i.putExtra("url", url);
                        startActivity(i);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //什么都不做，也会执行dismis方法
            }
        }).show();
    }

    //获取版本号
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
        versionName = info.versionName;
        versionCode = info.versionCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tv_scan_result.setText(scanResult);
        }
    }

    public void applyCameraPermission(){
        String []permission = {Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= 23){
            int check = ContextCompat.checkSelfPermission(this, permission[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED){
                //开始下载
                scanQrCode();
            }else{
                requestPermissions(permission, 1);
            }
        }else{
            scanQrCode();
        }
    }

    private void scanQrCode(){
        //打开扫描界面扫描条形码或二维码
        Intent openCameraIntent = new Intent(SettingActivity.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    scanQrCode();
                }
        }
    }
}
