package com.example.administrator.smartbutler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;

import com.example.administrator.smartbutler.fragment.BulterFragment;
import com.example.administrator.smartbutler.fragment.GridFragment;
import com.example.administrator.smartbutler.fragment.UserFragment;
import com.example.administrator.smartbutler.fragment.WeChatFragment;
import com.example.administrator.smartbutler.ui.SettingActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPage
    private ViewPager mViewPager;
    //Title
    private List<String> mTitle;
    //Fragment
    private List<Fragment> mFragments;
    //悬浮窗
    private FloatingActionButton fab_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //去掉阴影
        getSupportActionBar().setElevation(0);

        initDate();
        initView();

    }

    //初始化数据
    private void initDate() {
        mTitle = new ArrayList<>();
        mTitle.add(getResources().getString(R.string.butler_service));
        mTitle.add(getResources().getString(R.string.wechat_select));
        mTitle.add(getResources().getString(R.string.beauties_community));
        mTitle.add(getResources().getString(R.string.personal_center));

        mFragments = new ArrayList<>();
        mFragments.add(new BulterFragment());
        mFragments.add(new WeChatFragment());
        mFragments.add(new GridFragment());
        mFragments.add(new UserFragment());
    }
    //初始化View
    private void initView(){
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);
        //默认隐藏
        fab_setting.setVisibility(View.GONE);
        mTabLayout = (TabLayout) findViewById(R.id.mTableLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPage);

        //viewPager的滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //正在滑动的监听
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //选中
            @Override
            public void onPageSelected(int position) {
                Log.i("TAG", "position:" + position);
                if (position==0){
                    fab_setting.setVisibility(View.GONE);
                }else{
                    fab_setting.setVisibility(View.VISIBLE);
                }
            }
            //滑动状态的监听
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //预加载
        mViewPager.setOffscreenPageLimit(mFragments.size());

        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的Item
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            //返回Item的个数
            @Override
            public int getCount() {
                return mFragments.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
        }
    }
}
