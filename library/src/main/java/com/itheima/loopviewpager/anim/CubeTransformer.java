package com.itheima.loopviewpager.anim;

import android.view.View;

/**
 * 立方体
 */
public class CubeTransformer extends BasePageTransformer {

    public CubeTransformer(int orientation) {
        super(orientation);
    }

    @Override
    public void transformViewPage(View view, float position) {
        view.setPivotX(position <= 0 ? view.getMeasuredWidth() : 0);
        view.setPivotY(view.getMeasuredHeight() * 0.5f);
        view.setRotationY(90f * position);
    }

}
