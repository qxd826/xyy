package com.example.xyy.xyyapplication.source.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.fragment.GoodsFragment;
import com.example.xyy.xyyapplication.source.fragment.SupplyFragment;
import com.example.xyy.xyyapplication.source.fragmentAdapter.MFragmentAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
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

    private ArrayList<Fragment> fragmentsList;

    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;
    private Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initClick();
        initViewPager();
    }

    public void initViewPager() {
        fragmentsList = new ArrayList<>();
        Fragment fragmentOne = new SupplyFragment();
        Fragment fragmentTwo = new GoodsFragment();

        fragmentsList.add(fragmentOne);
        fragmentsList.add(fragmentTwo);

        MFragmentAdapter mFragmentAdapter = new MFragmentAdapter(getSupportFragmentManager(),fragmentsList);
        viewpager.setAdapter(mFragmentAdapter);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initClick(){
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
    }

    private void InitWidth() {
/*        ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
        bottomLineWidth = ivBottomLine.getLayoutParams().width;
        Log.d(TAG, "cursor imageview width=" + bottomLineWidth);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) ((screenW / 4.0 - bottomLineWidth) / 2);
        Log.i("MainActivity", "offset=" + offset);

        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_one:
                viewpager.setCurrentItem(0);
                break;
            case R.id.btn_two:
                viewpager.setCurrentItem(1);
                break;
            case R.id.btn_three:
                break;
            case R.id.btn_four:
                break;
        }
    }
}
