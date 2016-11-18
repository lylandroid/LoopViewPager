package com.itheima.loopviewpager.dots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

public abstract class DotCustomView extends View {

    public int color;
    public Paint paint;

    public DotCustomView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        customDraw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawColor(Color.TRANSPARENT);
        paint.reset();
    }

    @Override
    public void setBackgroundColor(int color) {
        this.color = color;
        invalidate();
    }

    public abstract void customDraw(Canvas canvas);

}
