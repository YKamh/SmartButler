package com.example.administrator.smartbutler.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends BaseActivity {

    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setElevation(0);

        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);

        mList.add("应用名：" + getString(R.string.app_name));
        mList.add("版本号：" + UtilTools.getVersion(this));
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
    }

}
