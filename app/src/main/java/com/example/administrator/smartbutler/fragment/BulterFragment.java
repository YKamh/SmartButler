package com.example.administrator.smartbutler.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.adapter.ChatListAdapter;
import com.example.administrator.smartbutler.entity.ChatListDate;
import com.example.administrator.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 描述：服务管家
 */
public class BulterFragment extends Fragment implements View.OnClickListener {

    private ListView mChatListView;
    //输入框
    private EditText et_text;
    //发送按钮
    private Button btn_send;
    private ChatListAdapter mChatListAdapter;
    private List<ChatListDate> mList = new ArrayList<>();

    public BulterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bulter, container, false);
        findView(view);
        return view;
    }

    //初始化View
    private void findView(View view) {
        mChatListView = view.findViewById(R.id.mChatListView);
        et_text = view.findViewById(R.id.et_text);
        btn_send = view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        //设置适配器
        mChatListAdapter = new ChatListAdapter(getActivity(), mList);
        mChatListView.setAdapter(mChatListAdapter);

        addLeft("你好，我是小优！");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                /**
                 * 1。获取输入框的内容
                 * 2.判断是否为空
                 * 3.内容不能超过30
                 * 4.发送给机器人请求返回内容
                 * 5.清空输入框
                 * 6.添加你输入的内容到right item
                 * 7.拿到机器人的返回值之后添加在left item
                 */
                String text = et_text.getText().toString();
                if (!TextUtils.isEmpty(text)){
                    if (text.length()>30){
                        Toast.makeText(getActivity(), "输入长度超出限制", Toast.LENGTH_SHORT).show();
                    }else{
                        //清空输入框
                        et_text.setText("");
                        //添加输入内容到right item
                        addRight(text);
                        ///发送请求
                        String url = "http://op.juhe.cn/robot/index?info="+ text +"&key="+ StaticClass.CHAT_LIST_KEY;
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                parsingJson(t);
                            }
                        });
                    }
                }else{
                    Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            if (jsonObject.getInt("error_code")!=0){
                String reason = jsonObject.getString("reason");
                addLeft(reason);
                return;
            }
            JSONObject result = jsonObject.getJSONObject("result");
            //拿到返回值
            String code = result.getString("code");
            String text = result.getString("text");
            if (!code.equals("100000")){
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                return;
            }
            //添加到left item
            addLeft(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //添加左边文本
    private void addLeft(String text){
        ChatListDate date = new ChatListDate();
        date.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        date.setText(text);
        mList.add(date);
        //通知Adapter刷新
        mChatListAdapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }
    //添加右边文本
    private void addRight(String text){
        ChatListDate date = new ChatListDate();
        date.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        date.setText(text);
        mList.add(date);
        //通知Adapter刷新
        mChatListAdapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }
}
