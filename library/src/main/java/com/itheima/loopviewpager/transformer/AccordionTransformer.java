package com.itheima.loopviewpager.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class AccordionTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        if (position <= 0) {
            view.setPivotX(view.getMeasuredWidth());
            view.setScaleX(1f + position);
        } else if (position <= 1) {
            view.setPivotX(0);
            view.setScaleX(1f - position);
        }
    }

}
