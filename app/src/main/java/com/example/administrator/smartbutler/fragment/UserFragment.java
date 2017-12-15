package com.example.administrator.smartbutler.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.entity.MyUser;
import com.example.administrator.smartbutler.ui.CourierActivity;
import com.example.administrator.smartbutler.ui.LoginActivity;
import com.example.administrator.smartbutler.ui.PhoneActivity;
import com.example.administrator.smartbutler.utils.L;
import com.example.administrator.smartbutler.utils.ShareUtil;
import com.example.administrator.smartbutler.utils.StaticClass;
import com.example.administrator.smartbutler.utils.UtilTools;
import com.example.administrator.smartbutler.view.CustomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * 描述： 个人中心
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    private Button btn_logout;
    private TextView edit_data;
    private EditText et_username;
    private EditText et_gender;
    private EditText et_age;
    private EditText et_desc;
    //更新按钮
    private Button btn_update_ok;
    //圆形头像
    private CircleImageView profile_image;
    private CustomDialog dialog;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    //快递查询
    private TextView tv_courier;
    //归属地查询
    private TextView tv_phone;

    private boolean isEdit = false;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        findView(view);
        return view;
    }

    //初始化View
    private void findView(View view) {
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        edit_data = (TextView) view.findViewById(R.id.edit_user);
        edit_data.setOnClickListener(this);
        tv_courier = (TextView) view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);

        et_username = (EditText) view.findViewById(R.id.et_username);
        et_gender = (EditText) view.findViewById(R.id.et_gender);
        et_age = (EditText) view.findViewById(R.id.et_age);
        et_desc = (EditText) view.findViewById(R.id.et_desc);
        btn_update_ok = (Button) view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        UtilTools.getImageFromShare(profile_image, getActivity());

        dialog = new CustomDialog(getActivity(), 100, 100, R.layout.dialog_photo, R.style.Theme_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        //屏外点击无效
        dialog.setCancelable(false);
        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        //默认是不可编辑的
        setEnabled(false);

        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getUsername());
        et_gender.setText(userInfo.isSex() ? "男" : "女");
        et_age.setText(userInfo.getAge() + "");
        et_desc.setText(userInfo.getDesc());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                //退出登录
                //清除缓存对象
                MyUser.logOut();
                //现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.edit_user:
                if (isEdit){
                    setEnabled(false);
                    isEdit = false;
                    btn_update_ok.setVisibility(View.GONE);
                }else{
                    setEnabled(true);
                    isEdit = true;
                    btn_update_ok.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_update_ok:
                //拿到输入框的值
                String username = et_username.getText().toString();
                String age = et_age.getText().toString();
                String gender = et_gender.getText().toString();
                String desc = et_desc.getText().toString();
                //判断是否为空
                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(age) & !TextUtils.isEmpty(gender)) {
                    //更新属性
                    MyUser myUser = new MyUser();
                    myUser.setUsername(username);
                    myUser.setAge(Integer.parseInt(age));
                    myUser.setSex(gender.equals("男") ? true : false);
                    if (!TextUtils.isEmpty(desc)) {
                        myUser.setDesc(desc);
                    } else {
                        myUser.setDesc("这个人很懒什么都没有留下!");
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    myUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //修改成功
                                setEnabled(false);
                                btn_update_ok.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.can_not_input_null), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_camera:
                //启动相机
                toCamera();
                break;
            case R.id.btn_picture:
                //启动图库
                toPicture();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    //相机请求码
    public static final int CAMERA_REQUEST_CODE = 100;
    //图库请求码
    public static final int IMAGE_REQUEST_CODE = 101;
    //裁剪请求码
    public static final int RESULT_REQUEST_CODE = 102;
    private File tempFile = null;

    //启动图库
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用,可用的话进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //启动相机
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册返回的数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机返回数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null){
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null){
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //裁剪图片
    private void startPhotoZoom(Uri uri) {
        if (uri == null){
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", true);
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspecty", 1);
        //裁剪图片的质量（分辨率）
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //控制焦点
    private void setEnabled(boolean is) {
        et_username.setEnabled(is);
        et_gender.setEnabled(is);
        et_age.setEnabled(is);
        et_desc.setEnabled(is);
    }

    private void setImageToView(Intent data){
        Bundle bundle = data.getExtras();
        if (bundle != null){
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存
        UtilTools.putImageToShare(profile_image, getActivity());
    }
}
