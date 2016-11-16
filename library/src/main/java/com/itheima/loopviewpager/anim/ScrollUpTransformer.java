package com.itheima.loopviewpager.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

public class ScrollUpTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        if (position <= 0) {
            view.setTranslationY(-position);

//            view.setPivotX(view.getMeasuredWidth());
//            view.setPivotY(view.getMeasuredHeight() * 0.5f);
//            view.setRotationY(90f * position);
        } else if (position <= 1) {
            view.setTranslationY(position);
//            view.setPivotX(0);
//            view.setPivotY(view.getMeasuredHeight() * 0.5f);
//            view.setRotationY(90f * position);
        }
    }

}
