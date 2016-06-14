package com.example.xyy.xyyapplication.source.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.fragment.CustomerFragment;
import com.example.xyy.xyyapplication.source.fragment.GoodsFragment;
import com.example.xyy.xyyapplication.source.fragment.SupplyFragment;
import com.example.xyy.xyyapplication.source.fragment.UserFragment;
import com.example.xyy.xyyapplication.source.fragmentAdapter.MFragmentAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    @Bind(R.id.btn_one)
    Button btnOne;
    @Bind(R.id.btn_two)
    Button btnTwo;
    @Bind(R.id.btn_three)
    Button btnThree;
    @Bind(R.id.btn_four)
    Button btnFour;
    @Bind(R.id.ll_tabs)
    LinearLayout llTabs;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.select_icon)
    ImageView selectIcon;

    private ArrayList<Fragment> fragmentsList;

    private int currIndex = 0;
    private int selectIconWidth;
    private int selectIconHeight;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;
    private Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "mainActivity onCreate......");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initWidth();
        initClick();
        initViewPager();
    }

    public void initViewPager() {
        fragmentsList = new ArrayList<Fragment>();
        Fragment goodsFragment = new GoodsFragment();
        Fragment supplyFragment = new SupplyFragment();
        Fragment customerFragment = new CustomerFragment();
        Fragment userFragment = new UserFragment();

        fragmentsList.add(goodsFragment);
        fragmentsList.add(supplyFragment);
        fragmentsList.add(customerFragment);
        fragmentsList.add(userFragment);

        MFragmentAdapter mFragmentAdapter = new MFragmentAdapter(getSupportFragmentManager(), fragmentsList);
        viewpager.setAdapter(mFragmentAdapter);
        viewpager.setOffscreenPageLimit(2);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Animation animation = null;
                switch (position) {
                    case 0:
                        if (currIndex == 1) {
                            animation = new TranslateAnimation(position_one, 0, 0, 0);
                        } else if (currIndex == 2) {
                            animation = new TranslateAnimation(position_two, 0, 0, 0);
                        } else if (currIndex == 3) {
                            animation = new TranslateAnimation(position_three, 0, 0, 0);
                        }
                        break;
                    case 1:
                        if (currIndex == 0) {
                            animation = new TranslateAnimation(0, position_one, 0, 0);
                        } else if (currIndex == 2) {
                            animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        } else if (currIndex == 3) {
                            animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        }
                        break;
                    case 2:
                        if (currIndex == 0) {
                            animation = new TranslateAnimation(0, position_two, 0, 0);
                        } else if (currIndex == 1) {
                            animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        } else if (currIndex == 3) {
                            animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        }
                        break;
                    case 3:
                        if (currIndex == 0) {
                            animation = new TranslateAnimation(0, position_three, 0, 0);
                        } else if (currIndex == 1) {
                            animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        } else if (currIndex == 2) {
                            animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        }
                        break;
                }
                currIndex = position;
                animation.setFillAfter(true);
                animation.setDuration(300);
                selectIcon.startAnimation(animation);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initClick() {
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
    }

    private void initWidth() {
        selectIconWidth = selectIcon.getLayoutParams().width;
        selectIconHeight = selectIcon.getLayoutParams().height;
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;

        offset = (int) ((screenW / 4.0 - selectIconWidth) / 2);
        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_one:
                viewpager.setCurrentItem(0);
                Log.i(TAG, "我选择了0");
                break;
            case R.id.btn_two:
                viewpager.setCurrentItem(1);
                Log.i(TAG, "我选择了1");
                break;
            case R.id.btn_three:
                viewpager.setCurrentItem(2);
                Log.i(TAG, "我选择了2");
                break;
            case R.id.btn_four:
                viewpager.setCurrentItem(3);
                Log.i(TAG, "我选择了3");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "mainActivity destroy......");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "mainActivity stop......");
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示"); //设置标题
            builder.setMessage("是否确认退出?"); //设置内容
            builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //关闭dialog
                    System.exit(0);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //参数都设置完成了，创建并显示出来
            builder.create().show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
