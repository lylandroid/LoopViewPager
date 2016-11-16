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
import com.itheima.loopviewpager.anim.CubeTransformer;
import com.itheima.loopviewpager.anim.FixedSpeedScroller;
import com.itheima.loopviewpager.anim.ScrollUpTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LoopViewPager<A, B> extends FrameLayout implements View.OnTouchListener {

    private List<A> imgList;//图片集合数据
    private List<B> titleList;//标题集合数据
    private A[] imgArray;//图片数组数据
    private B[] titleArray;//标题数组数据
    private int imgLength;//图片数据长度
    private int titleLength;//标题数据长度

    public void setImgData(List<A> imgList) {
        this.imgList = imgList;
        titleList = null;
        imgLength = imgList.size();
        titleLength = 0;
    }

    public void setImgData(A[] imgArray) {
        this.imgArray = imgArray;
        imgLength = imgArray.length;
        titleLength = 0;
    }

    public void setImgAndTitleData(List<A> imgList, List<B> titleList) {
        this.imgList = imgList;
        this.titleList = titleList;
        imgLength = imgList.size();
        titleLength = titleList.size();
    }

    public void setImgAndTitleData(List<A> imgList, B[] titleArray) {
        this.imgList = imgList;
        this.titleArray = titleArray;
        imgLength = imgList.size();
        titleLength = titleArray.length;
    }

    public void setImgAndTitleData(A[] imgArray, List<B> titleList) {
        this.imgArray = imgArray;
        this.titleList = titleList;
        imgLength = imgArray.length;
        titleLength = titleList.size();
    }

    public void setImgAndTitleData(A[] imgArray, B[] titleArray) {
        this.imgArray = imgArray;
        this.titleArray = titleArray;
        imgLength = imgArray.length;
        titleLength = titleArray.length;
    }


    //////////////////////////////////
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCROLL:
                    realIndex++;
                    viewPager.setCurrentItem(realIndex);
                    handler.sendEmptyMessageDelayed(SCROLL, loopTime);
                    break;
            }
        }
    };

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopViewPager);
        loopTime = a.getInt(R.styleable.LoopViewPager_intervalTime, 0);
        scrollEnable = a.getBoolean(R.styleable.LoopViewPager_scrollEnable, true);
        touchEnable = a.getBoolean(R.styleable.LoopViewPager_touchEnable, true);
        animTime = a.getInt(R.styleable.LoopViewPager_animTime, 0);
        animStyle = a.getInt(R.styleable.LoopViewPager_animStyle, 0);
        a.recycle();
        View.inflate(getContext(), R.layout.hm_loopviewpager, this);
        viewPager = (ViewPager) findViewById(R.id.vp_pager);
        if (loopTime > 0 && loopTime < MIN_TIME) {
            loopTime = MIN_TIME;
        }
        if (animTime > 0 && animTime > loopTime) {
            animTime = loopTime;
        }
        setPageTransformer(animTime, animStyle);
    }

    /////////////////////////
    private List<LoopDotsView> loopDotsViews = new ArrayList<>();//轮播圆点
    private List<LoopTitleView> loopTitleViews = new ArrayList<>();//轮播文本
    private int realIndex;//真实的索引
    private int showIndex;//展示的索引

    public void start() {
        //获取XML配置文件中的标题和圆点
        loopDotsViews.clear();
        loopTitleViews.clear();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof LoopDotsView) {
                LoopDotsView loopDotsView = (LoopDotsView) view;
                loopDotsView.initDots(imgLength);
                loopDotsViews.add(loopDotsView);
            } else if (view instanceof LoopTitleView) {
                LoopTitleView loopTitleView = (LoopTitleView) view;
                loopTitleViews.add(loopTitleView);
            }
        }

        realIndex = 1000 * imgLength;
        showIndex = 0;

        viewPager.addOnPageChangeListener(new LoopPageChangeListener());
        viewPager.setAdapter(new LoopPagerAdapter());
        viewPager.setOnTouchListener(this);
        viewPager.setCurrentItem(realIndex);

        handler.removeCallbacksAndMessages(null);
        if (loopTime >= MIN_TIME) {
            handler.sendEmptyMessageDelayed(SCROLL, loopTime);
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
            if (loopDotsViews.size() > 0 && imgLength > 0) {
                for (LoopDotsView loopDotsView : loopDotsViews) {
                    loopDotsView.updateDots(index, showIndex);
                }
            }
            if (loopTitleViews.size() > 0 && titleLength > 0) {
                for (LoopTitleView loopTitleView : loopTitleViews) {
                    if (titleList != null) {
                        loopTitleView.setText("" + titleList.get(index));
                    } else if (titleArray != null) {
                        loopTitleView.setText("" + titleArray[index]);
                    }
                }
            }
            realIndex = position;
            showIndex = index;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    ///////////////////////////////////////////////////////////////////

    private final int MIN_TIME = 500;//最小轮播间隔时间
    private final int SCROLL = 1;//轮播消息常量


    private int loopTime;//轮播时间
    private boolean scrollEnable;//是否禁用滚动
    private boolean touchEnable;//是否禁用触摸
    private int animTime;//动画时间
    private int animStyle;//动画样式
    private ViewPager viewPager;//轮播页面



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (touchEnable && loopTime >= MIN_TIME) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.sendEmptyMessageDelayed(SCROLL, loopTime);
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
            if (imgLength > 0) {
                int index = position % imgLength;
                ImageView view = new ImageView(getContext());
                if (imgList != null) {
                    Glide.with(getContext()).load(imgList.get(index)).centerCrop().into(view);
                } else if (imgArray != null) {
                    Glide.with(getContext()).load(imgArray[index]).centerCrop().into(view);
                }
                container.addView(view);
                return view;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private void setPageTransformer(int animTime, int animStyle) {
        ViewPager.PageTransformer transformer = null;
        switch (animStyle) {
            case LoopAnimStyle.CUBE:
                transformer = new CubeTransformer();
                break;
            case LoopAnimStyle.ACCORDION:
                transformer = new AccordionTransformer();
                break;
            case LoopAnimStyle.SCROLLUP:
                transformer = new ScrollUpTransformer();
                break;
        }
        setPageTransformer(animTime, transformer);
    }

    public void setPageTransformer(int animTime, ViewPager.PageTransformer transformer) {
        if (animTime > 0) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                FixedSpeedScroller scroller = new FixedSpeedScroller(getContext());
                scroller.setmDuration(animTime);
                mScroller.set(viewPager, scroller);
            } catch (Exception e) {
            }
        }
        if (transformer != null) {
            setPageTransformer(transformer);
        }
    }

    public void setPageTransformer(ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
    }

}
