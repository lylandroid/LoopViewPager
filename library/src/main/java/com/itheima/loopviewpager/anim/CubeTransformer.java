package com.itheima.loopviewpager.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

public class CubeTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        view.setPivotX(position <= 0 ? view.getMeasuredWidth() : 0);
        view.setPivotY(view.getMeasuredHeight() * 0.5f);
        view.setRotationY(90f * position);
    }

}
