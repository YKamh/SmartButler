package com.example.administrator.smartbutler.utils;

import android.util.Log;

/**
 * 项目名：    SmartButler
 * 包名：      com.example.administrator.smartbutler.utils
 * 创建者：    Kamh
 * 创建时间：  2017/11/2422:30
 * 描述：      TODO
 */
public class L  {

    //开关
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "SmartButler";

    /**
     * 五个等级
     * D：DEGUG指出细粒度信息事件对调试应用程序是非常有帮助的
     * I: INFO表明粗粒度级别上突出强调应用程序的运行过程
     * W: WARN表明会出现潜在的错误情形，会表示应用程序的退出
     * E：ERROR指出虽然发生错误事件，但仍然不影响系统的继续运行
     * F: FATAL指出每个严重的错误事件将会导致应用程序的退出
     */
    public static void d(String text){
        if (DEBUG){
            Log.d(TAG, text);
        }
    }

    public static void i(String text){
        if (DEBUG){
            Log.i(TAG, text);
        }
    }

    public static void w(String text){
        if (DEBUG){
            Log.w(TAG, text);
        }
    }

    public static void e(String text){
        if (DEBUG){
            Log.e(TAG, text);
        }
    }
}
