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
import com.itheima.loopviewpager.anim.AccordionTransformer;
import com.itheima.loopviewpager.anim.AnimStyle;
import com.itheima.loopviewpager.anim.CubeTransformer;
import com.itheima.loopviewpager.anim.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LoopViewPager<A, B> extends FrameLayout implements View.OnTouchListener {

    private int loopTime;
    private int animTime;
    private int animStyle;
    private boolean scrollEnable;
    private boolean touchEnable;

    private ViewPager viewPager;
    private final int MIN_TIME = 1000;
    private final int CODE = 1;

    private List<LoopDotsView> loopDotsViews;
    private List<LoopTitleView> loopTitleViews;
    private int realIndex;
    private int showIndex;
    private LoopPagerAdapter loopPagerAdapter;
    private LoopPageChangeListener pageChangeListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE:
                    realIndex++;
                    viewPager.setCurrentItem(realIndex);
                    handler.sendEmptyMessageDelayed(CODE, loopTime);
                    break;
            }
        }
    };

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopViewPager);
        loopTime = a.getInt(R.styleable.LoopViewPager_loopTime, 0);
        animTime = a.getInt(R.styleable.LoopViewPager_animTime, 0);
        animStyle = a.getInt(R.styleable.LoopViewPager_animStyle, 0);
        scrollEnable = a.getBoolean(R.styleable.LoopViewPager_scrollEnable, true);
        touchEnable = a.getBoolean(R.styleable.LoopViewPager_touchEnable, true);
        a.recycle();
        init();
    }

    private void init() {
        loopDotsViews = new ArrayList<>();//轮播圆点
        loopTitleViews = new ArrayList<>();//轮播文本
        realIndex = -1;//真实的索引
        showIndex = -1;//展示的索引

        // loopTime必须大于MIN_TIME
        if (loopTime > 0 && loopTime < MIN_TIME) {
            loopTime = MIN_TIME;
        }
        // animTime必须小于loopTime
        if (animTime > 0 && animTime > loopTime) {
            animTime = loopTime;
        }
        View.inflate(getContext(), R.layout.hm_loopviewpager, this);
        viewPager = (ViewPager) findViewById(R.id.vp_pager);
        ViewPager.PageTransformer transformer = null;
        switch (animStyle) {
            case AnimStyle.ACCORDION:
                transformer = new AccordionTransformer();
                break;
            case AnimStyle.CUBE:
                transformer = new CubeTransformer();
                break;
        }
        setPageTransformer(animTime, transformer);
    }

    public void setPageTransformer(ViewPager.PageTransformer transformer) {
        setPageTransformer(0, transformer);
    }

    public void setPageTransformer(int animTime, ViewPager.PageTransformer transformer) {
        try {
            if (animTime > 0) {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                FixedSpeedScroller scroller = new FixedSpeedScroller(getContext());
                scroller.setmDuration(animTime);
                mScroller.set(viewPager, scroller);
            }
        } catch (Exception e) {
        }
        if (transformer != null) {
            viewPager.setPageTransformer(true, transformer);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (loopTime > 0 && touchEnable) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.sendEmptyMessageDelayed(CODE, loopTime);
                    break;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollEnable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return true;
        }
    }

    private class LoopPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgLength > 0 ? Integer.MAX_VALUE : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getDefaultItemView(position % imgLength);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private class LoopPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int currentIndex = position % imgLength;
            if (loopDotsViews.size() > 0 && imgLength > 0) {
                for (LoopDotsView loopDotsView : loopDotsViews) {
                    loopDotsView.update(currentIndex, showIndex);
                }
            }
            if (loopTitleViews.size() > 0 && titleLength > 0) {
                for (LoopTitleView loopTitleView : loopTitleViews) {
                    if (titleList != null) {
                        loopTitleView.setText("" + titleList.get(currentIndex));
                    } else if (titleArray != null) {
                        loopTitleView.setText("" + titleArray[currentIndex]);
                    }
                }
            }
            realIndex = position;
            showIndex = currentIndex;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    }

    private View getDefaultItemView(int currentIndex) {
        View view = null;
        if (onCreateItemViewListener != null) {
            view = onCreateItemViewListener.getItemView(currentIndex);
        }
        if (view == null) {
            view = new ImageView(getContext());
            if (imgList != null) {
                Glide.with(getContext()).load(imgList.get(currentIndex)).centerCrop().into((ImageView) view);
            } else if (imgArray != null) {
                Glide.with(getContext()).load(imgArray[currentIndex]).centerCrop().into((ImageView) view);
            }
        }
        return view;
    }

    public interface OnCreateItemViewListener {
        View getItemView(int position);
    }

    private OnCreateItemViewListener onCreateItemViewListener;

    public void setOnCreateItemViewListener(OnCreateItemViewListener onCreateItemViewListener) {
        this.onCreateItemViewListener = onCreateItemViewListener;
    }

    private void start() {
        loopDotsViews.clear();
        loopTitleViews.clear();
        getLoopChild(this);
        loopPagerAdapter = new LoopPagerAdapter();
        pageChangeListener = new LoopPageChangeListener();
        realIndex = 1000 * imgLength;
        viewPager.setOnPageChangeListener(pageChangeListener);
        viewPager.setAdapter(loopPagerAdapter);
        viewPager.setOnTouchListener(this);
        viewPager.setCurrentItem(realIndex);
        handler.removeCallbacksAndMessages(null);
        if (loopTime > 0) {
            handler.sendEmptyMessageDelayed(CODE, loopTime);
        }
    }

    private void getLoopChild(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof LoopDotsView) {
                LoopDotsView loopDotsView = (LoopDotsView) view;
                loopDotsView.setDotsLength(imgLength);
                loopDotsViews.add(loopDotsView);
            } else if (view instanceof LoopTitleView) {
                LoopTitleView loopTitleView = (LoopTitleView) view;
                loopTitleViews.add(loopTitleView);
            } else if (view instanceof ViewGroup) {
                getLoopChild((ViewGroup) view);
            }
        }
    }

    private List imgList;//图片集合数据
    private A[] imgArray;//图片数组数据
    private int imgLength;//图片数据长度
    private List titleList;//标题集合数据
    private B[] titleArray;//标题数组数据
    private int titleLength;//标题数据长度

    public void setImgData(List imgList) {
        this.imgList = imgList;
        this.imgArray = null;
        this.imgLength = imgList.size();
        this.titleList = null;
        this.titleArray = null;
        this.titleLength = 0;
        start();
    }

    public void setImgData(A[] imgArray) {
        this.imgArray = imgArray;
        this.imgList = null;
        this.imgLength = imgArray.length;
        this.titleList = null;
        this.titleArray = null;
        this.titleLength = 0;
        start();
    }

    public void setImgAndTitleData(List imgList, List<B> titleList) {
        this.imgList = imgList;
        this.imgArray = null;
        this.imgLength = imgList.size();
        this.titleList = titleList;
        this.titleArray = null;
        this.titleLength = titleList.size();
        start();
    }

    public void setImgAndTitleData(List imgList, B[] titleArray) {
        this.imgList = imgList;
        this.imgArray = null;
        this.imgLength = imgList.size();
        this.titleList = null;
        this.titleArray = titleArray;
        this.titleLength = titleArray.length;
        start();
    }

    public void setImgAndTitleData(A[] imgArray, List titleList) {
        this.imgList = null;
        this.imgArray = imgArray;
        this.imgLength = imgArray.length;
        this.titleList = titleList;
        this.titleArray = null;
        this.titleLength = titleList.size();
        start();
    }

    public void setImgAndTitleData(A[] imgArray, B[] titleArray) {
        this.imgList = null;
        this.imgArray = imgArray;
        this.imgLength = imgArray.length;
        this.titleList = null;
        this.titleArray = titleArray;
        this.titleLength = titleArray.length;
        start();
    }

}
