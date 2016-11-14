package com.itheima.simpledemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.itheima.loopviewpager.LoopViewPager;

public class SimpleDemo3 extends AppCompatActivity {

    private LoopViewPager loopViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_demo3);
        loopViewPager = (LoopViewPager) findViewById(R.id.lvp_pager);
        loopViewPager.setImgAndTitleData(DataFactory.imgListString(), DataFactory.titleListString());
    }

}
