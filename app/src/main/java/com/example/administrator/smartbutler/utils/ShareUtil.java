package com.example.administrator.smartbutler.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名：    SmartButler
 * 包名：      com.example.administrator.smartbutler.utils
 * 创建者：    Kamh
 * 创建时间：  2017/11/2422:48
 * 描述：      SharedPreference封装
 */
public class ShareUtil {

    /**
     * SharedPreference实现步骤
     * 1、根据Context获取SharedPreference对象
     * 2、利用edit()方法获取Editor对象
     * 3、通过Editor对象存储Key-Value键值对数据
     * 4、通过commit()方法提交数据
     */

//    private void test(Context context){
//        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//
//        editor.putString("key", "value");
//        editor.commit();
//
//        sp.getString("key", "未获取到");
//    }
    public final static String NAME = "config";

    public static void putString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(Context context, String key, int value){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    //删除单个
    public static void deleShare(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
    //删除全部
    public static void deleAll(Context context){
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
