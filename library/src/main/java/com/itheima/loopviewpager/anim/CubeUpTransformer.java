package com.itheima.loopviewpager.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

public class CubeUpTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        if (position <= 0) {
            view.setPivotX(view.getMeasuredWidth() * 0.5f);
            view.setPivotY(view.getMeasuredHeight());
            view.setRotationY(90f * position);
        } else if (position <= 1) {
            view.setPivotX(view.getMeasuredWidth() * 0.5f);
            view.setPivotY(0);
            view.setRotationY(90f * position);
        }
    }

}
