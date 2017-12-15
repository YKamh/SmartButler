package com.example.administrator.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.administrator.smartbutler.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 项目名：    SmartButler
 * 包名：      com.example.administrator.smartbutler.utils
 * 创建者：    Kamh
 * 创建时间：  2017/12/1217:02
 * 描述：      Picasso封装
 */
public class PicassoUtils {

    //默认加载图片
    public static void loadImaheView(Context context, String url, ImageView targetImg) {
        Picasso.with(context).load(url).into(targetImg);
    }

    //默认加载图片（指定大小）
    public static void loadImageViewSize(Context context, String url, int width, int height, ImageView targetImg) {
        Picasso.with(context)
                .load(url)
                .resize(width, height)
                .centerCrop()
                .into(targetImg);
    }

    //加载图片有默认图片
    public static void loadImageViewHolder(Context context, String url, int loadImage, int errorImg, ImageView targetImg) {
        Picasso.with(context)
                .load(url)
                .placeholder(loadImage)
                .error(errorImg)
                .into(targetImg);
    }

    //裁剪图片
    public static void loadIamgeViewCrop(Context context, String url, ImageView targetImg){
        Picasso.with(context).load(url).transform(new CropSquareTransformation()).into(targetImg);
    }


    //按比例裁剪 矩形
    public static class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                //回收
                source.recycle();
            }
            return result;
        }
        //标记
        @Override public String key() { return "kamh"; }
    }

}
