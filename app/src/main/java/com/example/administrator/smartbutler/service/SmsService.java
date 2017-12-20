package com.example.administrator.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.view.PixelCopy;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.utils.L;
import com.example.administrator.smartbutler.utils.StaticClass;

/**
 * Created by Administrator on 2017/12/16.
 */

public class SmsService extends Service implements View.OnClickListener{

    private SmsReceiver mSmsReceiver;
    //发件人号码
    private String smsPhone;
    //短信内容
    private String smsContent;

    //窗口管理
    private WindowManager wm;
    //布局参数
    private WindowManager.LayoutParams mLayoutParams;
    //View
    private View mView;

    private TextView tv_phone;
    private TextView tv_content;
    private Button btn_send_sms;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }
    //初始化
    private void init() {
        L.i("init service");

        //动态注册
        mSmsReceiver = new SmsReceiver();
        IntentFilter intent = new IntentFilter();
        //添加Action
        intent.addAction(StaticClass.SMS_ACTION);
        //设置权限
        intent.setPriority(Integer.MAX_VALUE);
        //注册广播
        registerReceiver(mSmsReceiver, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");
        unregisterReceiver(mSmsReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_sms:
                sendSms();
                //消失窗口
                if (mView.getParent()!=null){
                    wm.removeView(mView);
                }
                break;
        }
    }

    private void sendSms() {
        Uri uri = Uri.parse("smsto:" + smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        //设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("smsBody", "");
        startActivity(intent);
    }

    //短信广播
    public class SmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StaticClass.SMS_ACTION.equals(action)){
                L.i("来短信了");
                //获取短息内容，返回是一个Object数组
                Object [] objects = (Object[]) intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for (Object obj : objects){
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                    //发件人
                    smsPhone = sms.getOriginatingAddress();
                    //内容
                    smsContent = sms.getDisplayMessageBody();
                    L.i("短信的内容" + smsContent + ":" + smsPhone);
                    showWindow();
                }
            }
        }
    }

    //窗口提示
    private void showWindow() {
        //获取系统服务
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //获取布局参数
        mLayoutParams = new WindowManager.LayoutParams();
        //定义宽高
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //格式
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        //定义类型
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //加载布局
        mView = View.inflate(getApplicationContext(), R.layout.sms_item, null);

        tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
        tv_content = (TextView) mView.findViewById(R.id.tv_content);
        btn_send_sms = (Button) mView.findViewById(R.id.btn_send_sms);
        btn_send_sms.setOnClickListener(this);

        tv_phone.setText("发件人:" + smsPhone);
        tv_content.setText(smsContent);

        //添加View到窗口
        wm.addView(mView, mLayoutParams);
    }
}
