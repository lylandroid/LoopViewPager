package com.itheima.loopviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.itheima.loopviewpager.dots.DotBaseView;
import com.itheima.loopviewpager.dots.DotOvalView;
import com.itheima.loopviewpager.dots.DotRectangleView;
import com.itheima.loopviewpager.dots.DotStyle;
import com.itheima.loopviewpager.dots.DotTriangleView;

public class LoopDotsView extends LinearLayout {

    private int dotSize;
    private int dotWidth;
    private int dotHeight;
    private int dotRange;
    private int dotShape;
    private int dotColor;
    private int dotSelectColor;
    private int dotResource;
    private int dotSelectResource;

    public LoopDotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopDotsView);
        dotSize = (int) a.getDimension(R.styleable.LoopDotsView_dotSize, 0);
        dotWidth = (int) a.getDimension(R.styleable.LoopDotsView_dotWidth, 0);
        dotHeight = (int) a.getDimension(R.styleable.LoopDotsView_dotHeight, 0);
        dotRange = (int) a.getDimension(R.styleable.LoopDotsView_dotRange, 0);
        dotShape = a.getInt(R.styleable.LoopDotsView_dotShape, LoopDefault.dotShape);
        dotColor = a.getColor(R.styleable.LoopDotsView_dotColor, LoopDefault.dotColor);
        dotSelectColor = a.getColor(R.styleable.LoopDotsView_dotSelectColor, LoopDefault.dotSelectColor);
        dotResource = a.getResourceId(R.styleable.LoopDotsView_dotResource, 0);
        dotSelectResource = a.getResourceId(R.styleable.LoopDotsView_dotSelectResource, 0);
        a.recycle();
    }

    public void setDotsLength(int length) {
        removeAllViews();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.width = dotWidth > 0 ? dotWidth : dotSize;
        params.height = dotHeight > 0 ? dotHeight : dotSize;
        for (int i = 0; i < length; i++) {
            DotBaseView dotView = null;
            if (dotShape == DotStyle.RECTANGLE || (dotSelectResource != 0 && dotResource != 0)) {
                dotView = new DotRectangleView(getContext());
            } else if (dotShape == DotStyle.OVAL) {
                dotView = new DotOvalView(getContext());
            } else if (dotShape == DotStyle.TRIANGLE) {
                dotView = new DotTriangleView(getContext());
            }
            if (i == 0) {
                params.setMargins(0, 0, 0, 0);
                if (dotSelectResource != 0) {
                    dotView.setBackgroundResource(dotSelectResource);
                } else {
                    dotView.setBackgroundColor(dotSelectColor);
                }
            } else {
                params.setMargins(getOrientation() == VERTICAL ? 0 : (dotRange > 0 ? dotRange : dotSize), getOrientation() == VERTICAL ? (dotRange > 0 ? dotRange : dotSize) : 0, 0, 0);
                if (dotResource != 0) {
                    dotView.setBackgroundResource(dotResource);
                } else {
                    dotView.setBackgroundColor(dotColor);
                }
            }
            dotView.setLayoutParams(params);
            addView(dotView);
        }
    }

    public void updateStatus(int index, int dotIndex) {
        if (index >= 0) {
            if (dotSelectResource != 0) {
                getChildAt(index).setBackgroundResource(dotSelectResource);
            } else {
                getChildAt(index).setBackgroundColor(dotSelectColor);
            }
        }
        if (dotIndex >= 0) {
            if (dotResource != 0) {
                getChildAt(dotIndex).setBackgroundResource(dotResource);
            } else {
                getChildAt(dotIndex).setBackgroundColor(dotColor);
            }
        }
    }

}
