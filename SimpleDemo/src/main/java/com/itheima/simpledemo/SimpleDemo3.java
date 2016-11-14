package com.itheima.simpledemo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.itheima.loopviewpager.LoopViewPager;

public class SimpleDemo3 extends AppCompatActivity {

    private LoopViewPager loopViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_demo3);
        loopViewPager = (LoopViewPager) findViewById(R.id.lvp_pager);
        loopViewPager.setImgAndTitleData(DataFactory.imgListString(), DataFactory.titleListString());
        loopViewPager.setPageTransformer(1500, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                if (position <= 0) {
                    view.setPivotX(view.getMeasuredWidth());
                    view.setPivotY(view.getMeasuredHeight() * 0.5f);
                    view.setRotationY(90f * position);
                } else if (position <= 1) {
                    view.setPivotX(0);
                    view.setPivotY(view.getMeasuredHeight() * 0.5f);
                    view.setRotationY(90f * position);
                }
            }
        });
    }

}
