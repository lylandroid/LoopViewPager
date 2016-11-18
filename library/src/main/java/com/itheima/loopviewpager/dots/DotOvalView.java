package com.itheima.loopviewpager.dots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

public class DotOvalView extends DotCustomView {

    public DotOvalView(Context context) {
        super(context);
    }

    @Override
    public void customDraw(Canvas canvas) {
        canvas.drawOval(new RectF(0, 0, getWidth(), getHeight()), paint);
    }

}
