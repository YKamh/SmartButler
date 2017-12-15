package com.example.administrator.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.entity.GrilData;
import com.example.administrator.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：    SmartButler
 * 包名：      com.example.administrator.smartbutler.adapter
 * 创建者：    Kamh
 * 创建时间：  2017/12/1422:26
 * 描述：      妹子适配器
 */
public class GridAdapter extends BaseAdapter{

    private Context mContext;
    private List<GrilData> mList;
    private LayoutInflater inflater;
    private GrilData data;
    private WindowManager wm;
    //屏幕宽
    private int width;

    public GridAdapter(Context context, List<GrilData> list) {
        mContext = context;
        mList = list;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.girl_item, null);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.mImageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = mList.get(position);
        //解析图片
        String url = data.getImgUrl();
        PicassoUtils.loadImageViewSize(mContext, url, width/2, 600, viewHolder.mImageView);
        return convertView;
    }

    class ViewHolder{
        private ImageView mImageView;
    }
}
