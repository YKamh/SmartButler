package com.example.administrator.smartbutler.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.smartbutler.MainActivity;
import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.entity.MyUser;
import com.example.administrator.smartbutler.utils.ShareUtil;
import com.example.administrator.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //注册按钮
    private Button btn_registered;
    private EditText et_name;
    private EditText et_pass;
    private Button btn_login;
    private CheckBox keep_password;
    private TextView tv_forget;

    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_pass = (EditText) findViewById(R.id.et_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        keep_password = (CheckBox) findViewById(R.id.keep_password);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);

        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏外点击无效
        dialog.setCancelable(false);

        //设置选中的状态
        boolean isCheck = ShareUtil.getBoolean(this, "keeppass", false);
        keep_password.setChecked(isCheck);
        if (isCheck){
            //设置密码
            et_name.setText(ShareUtil.getString(this, "name", ""));
            et_pass.setText(ShareUtil.getString(this, "password",""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_registered:
                startActivity(new Intent(this, RegisteredActivity.class));
                break;
            case R.id.btn_login:
                //获取输入框的值
                String name = et_name.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(pass)){
                    dialog.show();
                    //登录
                    MyUser myUser = new MyUser();
                    myUser.setUsername(name);
                    myUser.setPassword(pass);
                    myUser.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            dialog.dismiss();
                            //判断返回结果
                            if (e == null){
                                //判断邮箱是否认证
                                if (myUser.getEmailVerified()){
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "请前往邮箱认证", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "登录失败:" + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }
    //假设我输入用户名和密码，但是我不点击登录，而是直接退出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtil.putBoolean(this, "keeppass", keep_password.isChecked());

        //是否记住密码
        if (keep_password.isChecked()){
            //记住用户名和密码
            ShareUtil.putString(this, "name", et_name.getText().toString());
            ShareUtil.putString(this, "password", et_pass.getText().toString());
        }else{
            ShareUtil.deleShare(this, "name");
            ShareUtil.deleShare(this, "password");
        }
    }
}
