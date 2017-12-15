package com.example.administrator.smartbutler.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.adapter.GridAdapter;
import com.example.administrator.smartbutler.entity.GrilData;
import com.example.administrator.smartbutler.utils.L;
import com.example.administrator.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 描述： 美女社区
 */
public class GridFragment extends Fragment {

    private GridView mGridView;
    private List<GrilData> mList = new ArrayList<>();
    private GridAdapter mAdapter;

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

        //解析
        RxVolley.get(StaticClass.Girl_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("Json" + t);
                parsingJson(t);
            }
        });
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i< results.length();i++){
                JSONObject json = (JSONObject) results.get(i);
                String url = json.getString("url");
                GrilData data = new GrilData();
                data.setImgUrl(url);
                mList.add(data);
            }
            mAdapter = new GridAdapter(getActivity(), mList);
            mGridView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
