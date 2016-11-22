package com.itheima.loopviewpager.anim;

import android.view.View;

/**
 * 折叠
 */
public class AccordionTransformer extends BasePageTransformer {

    public AccordionTransformer(int orientation) {
        super(orientation);
    }

    @Override
    public void transformViewPage(View view, float position) {
        view.setPivotX(position <= 0 ? view.getMeasuredWidth() : 0);
        view.setScaleX(position <= 0 ? 1f + position : 1f - position);
    }

}
