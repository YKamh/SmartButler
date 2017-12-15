package com.example.administrator.smartbutler.ui;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.smartbutler.MainActivity;
import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    //容器
    private List<View> mList = new ArrayList<>();
    private View view1, view2, view3;
    //小圆点
    private ImageView point1, point2, point3;
    //跳过
    private ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }
    //初始化
    private void initView() {

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);

        //设置默认图片
        setPointImg(true, false, false);

        mViewPager  =(ViewPager) findViewById(R.id.mViewPage);
        //监听ViewPager滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //监听正在滑动
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //监听选中
            @Override
            public void onPageSelected(int position) {
                L.i("position:"+position);
                switch (position){
                    case 0:
                        setPointImg(true, false, false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImg(false, true, false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImg(false, false, true);
                        iv_back.setVisibility(View.GONE);
                        break;
                }
            }
            //滑动状态的监听
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        view1 = View.inflate(this, R.layout.pager_item_one, null);
        view2 = View.inflate(this, R.layout.pager_item_two, null);
        view3 = View.inflate(this, R.layout.pager_item_three, null);

        view3.findViewById(R.id.btn_start).setOnClickListener(this);

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        mViewPager.setAdapter(new GuideAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
            case R.id.iv_back:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        //添加View
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(mList.get(position));
            return mList.get(position);
        }
        //销毁View
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(mList.get(position));
//            super.destroyItem(container, position, object);
        }
    }
    //设置小圆点的选中效果
    private void setPointImg(boolean isCheck1, boolean isCheck2, boolean isCheck3){
        if (isCheck1){
            point1.setImageResource(R.drawable.point_on);
        }else{
            point1.setImageResource(R.drawable.point_off);
        }
        if (isCheck2){
            point2.setImageResource(R.drawable.point_on);
        }else{
            point2.setImageResource(R.drawable.point_off);
        }
        if (isCheck3){
            point3.setImageResource(R.drawable.point_on);
        }else{
            point3.setImageResource(R.drawable.point_off);
        }
    }
}
