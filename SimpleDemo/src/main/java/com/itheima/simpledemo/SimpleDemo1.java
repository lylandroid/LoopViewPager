package com.itheima.simpledemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.itheima.loopviewpager.LoopViewPager;

public class SimpleDemo1 extends AppCompatActivity {

    private LoopViewPager loopViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_demo1);
        loopViewPager = (LoopViewPager) findViewById(R.id.lvp_pager);
        findViewById(R.id.btn_list_string).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopViewPager.setImgData(DataFactory.imgListString());
                loopViewPager.start();
            }
        });
        findViewById(R.id.btn_list_int).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopViewPager.setImgData(DataFactory.imgListInt());
                loopViewPager.start();
            }
        });
        findViewById(R.id.btn_array_string).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopViewPager.setImgData(DataFactory.imgArrayString());
                loopViewPager.start();
            }
        });
        findViewById(R.id.btn_array_int).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopViewPager.setImgData(DataFactory.imgArrayInt());
                loopViewPager.start();
            }
        });
    }

}
