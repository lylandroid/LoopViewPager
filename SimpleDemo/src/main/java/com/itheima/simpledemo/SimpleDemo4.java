package com.itheima.simpledemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.itheima.loopviewpager.LoopViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleDemo4 extends AppCompatActivity {

    private LoopViewPager loopViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_demo4);
        loopViewPager = (LoopViewPager) findViewById(R.id.lvp_pager);
        loopViewPager.setImgAndTitleData(DataFactory.imgArrayInt(), DataFactory.titleArrayString());
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


        final List<View> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setText("textView " + i);
            textView.setBackgroundColor(Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
            list.add(textView);
        }

        loopViewPager.setOnCreateItemViewListener(new LoopViewPager.OnCreateItemViewListener() {
            @Override
            public View getItemView(int position) {
                return list.get(position);
            }
        });
    }

}
