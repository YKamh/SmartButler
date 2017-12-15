package com.example.administrator.smartbutler.application;

import android.app.Application;

import com.example.administrator.smartbutler.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

/**
 * 项目名：    SmartButler
 * 包名：      com.example.administrator.smartbutler.application
 * 创建者：    Kamh
 * 创建时间：  2017/11/2320:55
 * 描述：      Application
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);
        //初始化Bomb默认初始化
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
    }
}
