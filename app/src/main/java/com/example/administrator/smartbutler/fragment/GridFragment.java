package com.example.administrator.smartbutler.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.adapter.GridAdapter;
import com.example.administrator.smartbutler.entity.GrilData;
import com.example.administrator.smartbutler.utils.L;
import com.example.administrator.smartbutler.utils.PicassoUtils;
import com.example.administrator.smartbutler.utils.StaticClass;
import com.example.administrator.smartbutler.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 * 描述： 美女社区
 */
public class GridFragment extends Fragment {

    private GridView mGridView;
    private List<GrilData> mList = new ArrayList<>();
    private GridAdapter mAdapter;
    //提示框
    private CustomDialog mDialog;
    //预览图片
    private ImageView iv_img;
    //图片地址数据
    private List<String> mListUrl = new ArrayList<>();
    private PhotoViewAttacher mAttacher;

    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mGridView = view.findViewById(R.id.mGridView);

        mDialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, R.layout.dialog_girl, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        iv_img = (ImageView) mDialog.findViewById(R.id.iv_img);

//        String welfare = null;
//        try{
//            welfare = URLEncoder.encode(getString(R.string.text_welfare), "UTF-8");
//        }catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
        //解析
        RxVolley.get(StaticClass.Girl_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("Json" + t);
                parsingJson(t);
            }
        });

        //监听点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解析图片
                PicassoUtils.loadImaheView(getActivity(), mListUrl.get(position), iv_img);
                mAttacher = new PhotoViewAttacher(iv_img);
                //刷新
                mAttacher.update();
                mDialog.show();
            }
        });
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject json = (JSONObject) results.get(i);
                String url = json.getString("url");
                GrilData data = new GrilData();
                data.setImgUrl(url);
                mList.add(data);
                mListUrl.add(url);
            }
            mAdapter = new GridAdapter(getActivity(), mList);
            mGridView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
