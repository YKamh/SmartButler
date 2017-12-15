package com.example.administrator.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 项目名：    SmartButler
 * 包名：      com.example.administrator.smartbutler.utils
 * 创建者：    Kamh
 * 创建时间：  2017/11/2321:10
 * 描述：      工具的统一类
 */
public class UtilTools {


    //设置字体
    public static void setFont(Context context, TextView textView){
        Typeface fontType = Typeface.createFromAsset(context.getAssets(),"fonts/FONT.TTF");
        textView.setTypeface(fontType);
    }
    //保存到ShareUtil下
    public static void putImageToShare(ImageView imageView, Context context){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //第一步：将bitmap压缩成字节数组输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream);
        //第二部：利用Base64将我们的数组输出流转化成String
        byte [] byteArray = byStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三部：将Sting保存在我们的SharedPreference
        ShareUtil.putString(context, StaticClass.IMAGE_KEY, imageString);
    }

    //读取图片
    public static void getImageFromShare(ImageView imageView, Context context){
        //拿到String
        String imgString = ShareUtil.getString(context, StaticClass.IMAGE_KEY, "");
        if (!TextUtils.isEmpty(imgString)){
            //利用Base64将我们的数组输出流转化
            byte [] byteArry = Base64.decode(imgString, Base64.DEFAULT);
            ByteArrayInputStream bySteam = new ByteArrayInputStream(byteArry);
            //生成Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(bySteam);
            imageView.setImageBitmap(bitmap);
        }
    }

}
