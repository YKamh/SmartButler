package com.example.administrator.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.administrator.smartbutler.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class QrCodeActivity extends AppCompatActivity {

    private ImageView iv_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        initView();
    }

    private void initView() {
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        int width = getResources().getDisplayMetrics().widthPixels;
        //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("我的智能管家", width/2, width/2,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        iv_qr_code.setImageBitmap(qrCodeBitmap);
    }
}
