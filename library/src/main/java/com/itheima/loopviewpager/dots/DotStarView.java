package com.itheima.loopviewpager.dots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.widget.ImageView;

public class DotStarView extends ImageView {

    private int color;
    private Paint paint;

    public DotStarView(Context context) {
        super(context);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = getRoundBitmap();
        paint.reset();
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    private Bitmap getRoundBitmap() {
        Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        paint.setAntiAlias(true);
        paint.setColor(color);

        Path path = new Path();
        path.moveTo(0, getHeight());
        path.lineTo(getWidth(),getHeight()/2);
        path.lineTo(0,getHeight()/2);
        path.lineTo(getWidth(),getHeight());
        path.lineTo(getWidth()/2,0);
        path.lineTo(0,getHeight());
        canvas.drawPath(path,paint);
//        canvas.drawOval(new RectF(0, 0, getWidth(), getHeight()), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawColor(Color.TRANSPARENT);
        return output;
    }

    public void change(int color) {
        this.color = color;
        invalidate();
    }

}
