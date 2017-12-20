package com.example.administrator.smartbutler.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.service.SmsService;
import com.example.administrator.smartbutler.utils.ShareUtil;

/**
 * 设置
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    //语音播报
    private Switch sw_speak;
    //短信提醒
    private Switch sw_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        sw_speak = (Switch) findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);
        boolean isSpeak = ShareUtil.getBoolean(this, "isSpeak", false);
        sw_speak.setChecked(isSpeak);

        sw_sms = (Switch) findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);
        boolean isSMS = ShareUtil.getBoolean(this, "isSMS", false);
        sw_sms.setChecked(isSMS);
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
        }
    }
}
