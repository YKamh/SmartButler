package com.example.administrator.smartbutler.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisteredActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_user;
    private EditText et_age;
    private EditText et_desc;
    private RadioGroup mRadioGroup;
    private EditText et_pass;
    private EditText et_password;
    private EditText et_email;
    private Button btn_registered;
    //性别 true = bou
    private boolean isGender = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        initView();

    }

    private void initView() {
        et_user = (EditText) findViewById(R.id.et_user);
        et_age = (EditText) findViewById(R.id.et_age);
        et_desc = (EditText) findViewById(R.id.et_desc);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_registered:
                //获取到输入框的值
                String name = et_user.getText().toString().trim();//trim()是去空格
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                //判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(age) &
                        !TextUtils.isEmpty(pass) &
                        !TextUtils.isEmpty(password) &
                        !TextUtils.isEmpty(email)){
                    //判断两次输入的密码是否一致
                    if (pass.equals(password)){
                        //把性别判断一下
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == R.id.rb_boy){
                                    isGender = true;
                                }else if (checkedId == R.id.rb_girl){
                                    isGender = false;
                                }
                            }
                        });

                        //判断简介是否为空
                        if (TextUtils.isEmpty(desc)){
                            desc = "这个人很懒，什么都没有留下";
                        }

                        //注册
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(pass);
                        user.setEmail(email);
                        user.setAge(Integer.parseInt(age));
                        user.setSex(isGender);
                        user.setDesc(desc);

                        user.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if (e==null){
                                    Toast.makeText(RegisteredActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(RegisteredActivity.this, "注册失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
