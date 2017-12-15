package com.example.administrator.smartbutler.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_forget_password;
    private EditText et_email;
    private EditText et_old_pass;
    private EditText et_new_pass;
    private EditText et_new_pass_again;
    private Button btn_update_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();
    }
    //初始化View
    private void initView() {
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        btn_forget_password.setOnClickListener(this);
        et_email = (EditText) findViewById(R.id.et_email);
        et_old_pass = (EditText) findViewById(R.id.et_old_pass);
        et_new_pass = (EditText) findViewById(R.id.et_new_pass);
        et_new_pass_again = (EditText) findViewById(R.id.et_new_pass_again);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);
        btn_forget_password.setOnClickListener(this);
        btn_update_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_forget_password:
                //获取输入框的邮箱
                final String email = et_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email)){
                    //发送邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Toast.makeText(ForgetPasswordActivity.this, "邮箱已经发送至："+ email, Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(ForgetPasswordActivity.this, "邮箱发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_update_password:
                //过去输入框的值
                String oldPass = et_old_pass.getText().toString();
                String newPass = et_new_pass.getText().toString();
                String againPass = et_new_pass_again.getText().toString();
                if (!TextUtils.isEmpty(oldPass) & !TextUtils.isEmpty(newPass) & !TextUtils.isEmpty(againPass)) {
                    if (newPass.equals(againPass)){
                        //重置密码
                        MyUser.updateCurrentUserPassword(oldPass, newPass, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    Toast.makeText(ForgetPasswordActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(ForgetPasswordActivity.this, "重置密码失败", Toast.LENGTH_SHORT).show();
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
