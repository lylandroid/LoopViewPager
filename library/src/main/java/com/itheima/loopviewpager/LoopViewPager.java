package com.itheima.loopviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LoopViewPager<A, B> extends FrameLayout implements View.OnTouchListener {

    private final int MIN_TIME = 500;//最小轮播间隔时间
    private final int SCROLL = 1;//轮播消息常量

    private A imgData;//图片数据，数组（int,String），集合（int,String）
    private B titleData;//标题数据，数组（String），集合（String）
    private int imgLength;//图片数据长度
    private int titleLength;//标题数据长度

    private int intervalTime;//轮播时间
    private boolean scrollEnable;//是否禁用滚动
    private boolean touchEnable;//是否禁用触摸
    private CustomViewPager viewPager;//轮播页面

    private List<LoopDotsView> loopDotsViews = new ArrayList<>();//轮播圆点
    private List<LoopTitleView> loopTitleViews = new ArrayList<>();//轮播文本

    private int realIndex;//真实的索引
    private int showIndex;//展示的索引

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCROLL:
                    realIndex++;
                    viewPager.setCurrentItem(realIndex);
                    handler.sendEmptyMessageDelayed(SCROLL, intervalTime);
                    break;
            }
        }
    };

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopViewPager);
        intervalTime = a.getInt(R.styleable.LoopViewPager_intervalTime, 0);
        scrollEnable = a.getBoolean(R.styleable.LoopViewPager_scrollEnable, true);
        touchEnable = a.getBoolean(R.styleable.LoopViewPager_touchEnable, true);
        a.recycle();
        View.inflate(getContext(), R.layout.weight_loopviewpager, this);
        viewPager = (CustomViewPager) findViewById(R.id.cvp_pager);
        if (intervalTime > 0 && intervalTime < MIN_TIME) {
            intervalTime = MIN_TIME;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (touchEnable && intervalTime >= MIN_TIME) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.sendEmptyMessageDelayed(SCROLL, intervalTime);
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollEnable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return true;
        }
    }

    public void setPageTransformer(ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
    }

    public void setPageTransformer(int animTime, ViewPager.PageTransformer transformer) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext());
            scroller.setmDuration(animTime);
            mScroller.set(viewPager, scroller);
        } catch (Exception e) {
        }
        setPageTransformer(transformer);
    }

    public void setImgData(A imgData) {
        imgLength = 0;
        if (imgData != null) {
            this.imgData = imgData;
            if (imgData instanceof List) {
                imgLength = ((List) imgData).size();
            } else {
                if (imgData instanceof String[]) {
                    imgLength = ((String[]) imgData).length;
                } else if (imgData instanceof int[]) {
                    imgLength = ((int[]) imgData).length;
                }
            }
        }
        if (imgLength > 0) {
            if (titleLength > 0 && imgLength != titleLength) {
                throw new IllegalArgumentException("imgData or titleData is illegal parameter");
            } else {
                init();
            }
        } else {
            throw new IllegalArgumentException("imgData is illegal parameter");
        }
    }

    public void setImgAndTitleData(A imgData, B titleData) {
        titleLength = 0;
        if (titleData != null) {
            this.titleData = titleData;
            if (titleData instanceof List) {
                titleLength = ((List) titleData).size();
            } else {
                if (titleData instanceof String[]) {
                    titleLength = ((String[]) titleData).length;
                }
            }
        }
        if (titleLength > 0) {
            setImgData(imgData);
        } else {
            throw new IllegalArgumentException("titleData is illegal parameter");
        }
    }

    /**
     * 初始化数据
     */
    private void init() {
        handler.removeCallbacksAndMessages(null);
        loopTitleViews.clear();
        loopDotsViews.clear();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof LoopTitleView) {
                LoopTitleView loopTitleView = (LoopTitleView) view;
                loopTitleViews.add(loopTitleView);
            } else if (view instanceof LoopDotsView) {
                LoopDotsView loopDotsView = (LoopDotsView) view;
                loopDotsView.initDots(imgLength);
                loopDotsViews.add(loopDotsView);
            }
        }
        realIndex = 1000 * (Integer.MAX_VALUE % imgLength);
        showIndex = 0;
        viewPager.setAdapter(new LoopPagerAdapter());
        viewPager.setCurrentItem(realIndex);
        viewPager.setOnTouchListener(this);
        viewPager.addOnPageChangeListener(new LoopPageChangeListener());
        if (intervalTime >= MIN_TIME) {
            handler.sendEmptyMessageDelayed(SCROLL, intervalTime);
        }
    }

    private class LoopPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = position % imgLength;
            ImageView view = new ImageView(getContext());
            if (imgData instanceof List) {
                Glide.with(getContext()).load(((List) imgData).get(index)).centerCrop().into(view);
            } else {
                if (imgData instanceof String[]) {
                    Glide.with(getContext()).load(((String[]) imgData)[index]).centerCrop().into(view);
                } else if (imgData instanceof int[]) {
                    Glide.with(getContext()).load(((int[]) imgData)[index]).centerCrop().into(view);
                }
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * 同步显示圆点与标题
     */
    private class LoopPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int index = position % imgLength;
            if (loopTitleViews.size() > 0 && titleData != null) {
                for (LoopTitleView loopTitleView : loopTitleViews) {
                    if (titleData instanceof List) {
                        loopTitleView.setText(((List<String>) titleData).get(index));
                    } else {
                        loopTitleView.setText(((String[]) titleData)[index]);
                    }
                }
            }
            if (loopDotsViews.size() > 0 && imgData != null) {
                for (LoopDotsView loopDotsView : loopDotsViews) {
                    loopDotsView.update(index, showIndex);
                }
            }
            realIndex = position;
            showIndex = index;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

}
