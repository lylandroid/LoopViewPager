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
import com.itheima.loopviewpager.anim.AccordionLeftTransformer;
import com.itheima.loopviewpager.anim.AccordionUpTransformer;
import com.itheima.loopviewpager.anim.CubeLeftTransformer;
import com.itheima.loopviewpager.anim.CubeUpTransformer;
import com.itheima.loopviewpager.anim.FixedSpeedScroller;
import com.itheima.loopviewpager.anim.TransformerStyle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LoopViewPager<A, B> extends FrameLayout implements View.OnTouchListener {

    private final int MIN_TIME = 1000;//最小轮播间隔时间
    private final int CODE_SCROLL = 1;//轮播消息

    private ViewPager viewPager;


    private List<LoopDotsView> loopDotsViews = new ArrayList<>();//轮播圆点
    private List<LoopTitleView> loopTitleViews = new ArrayList<>();//轮播文本
    private int realIndex;//真实的索引
    private int showIndex;//展示的索引

    private int loopTime;
    private int animTime;
    private int animStyle;
    private boolean scrollEnable;
    private boolean touchEnable;

    private List<A> imgList;//图片集合数据
    private A[] imgArray;//图片数组数据
    private List<B> titleList;//标题集合数据
    private B[] titleArray;//标题数组数据

    private List<View> imgViews;

    private int imgLength;//图片数据长度
    private int titleLength;//标题数据长度

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_SCROLL:
                    realIndex++;
                    viewPager.setCurrentItem(realIndex);
                    handler.sendEmptyMessageDelayed(CODE_SCROLL, loopTime);
                    break;
            }
        }
    };

    public void setImgData(List<A> imgList) {
        this.imgList = imgList;
        imgArray = null;
        titleList = null;
        titleArray = null;
        imgLength = imgList.size();
        titleLength = 0;
        start();
    }

    public void setImgData(A[] imgArray) {
        this.imgArray = imgArray;
        imgList = null;
        titleList = null;
        titleArray = null;
        imgLength = imgArray.length;
        titleLength = 0;
        start();
    }

    //////////////////////////////////////
    public void setImgView(View[] imgArray) {
        imgArray = imgArray;
        imgList = null;
        titleList = null;
        titleArray = null;
        imgLength = imgArray.length;
        titleLength = 0;
        start();
    }

    public void setImgAndTitleData(List<A> imgList, List<B> titleList) {
        this.imgList = imgList;
        this.titleList = titleList;
        imgArray = null;
        titleArray = null;
        imgLength = imgList.size();
        titleLength = titleList.size();
        start();
    }

    public void setImgAndTitleData(List<A> imgList, B[] titleArray) {
        this.imgList = imgList;
        this.titleArray = titleArray;
        imgArray = null;
        titleList = null;
        imgLength = imgList.size();
        titleLength = titleArray.length;
        start();
    }

    public void setImgAndTitleData(A[] imgArray, List<B> titleList) {
        this.imgArray = imgArray;
        this.titleList = titleList;
        imgLength = imgArray.length;
        titleLength = titleList.size();
        start();
    }

    public void setImgAndTitleData(A[] imgArray, B[] titleArray) {
        this.imgArray = imgArray;
        this.titleArray = titleArray;
        imgLength = imgArray.length;
        titleLength = titleArray.length;
        start();
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopViewPager);
        loopTime = a.getInt(R.styleable.LoopViewPager_loopTime, 0);
        animTime = a.getInt(R.styleable.LoopViewPager_animTime, 0);
        animStyle = a.getInt(R.styleable.LoopViewPager_animStyle, 0);
        scrollEnable = a.getBoolean(R.styleable.LoopViewPager_scrollEnable, true);
        touchEnable = a.getBoolean(R.styleable.LoopViewPager_touchEnable, true);
        a.recycle();

        View.inflate(getContext(), R.layout.hm_loopviewpager, this);
        viewPager = (ViewPager) findViewById(R.id.vp_pager);

        // loopTime必须大于MIN_TIME
        if (loopTime > 0 && loopTime < MIN_TIME) {
            loopTime = MIN_TIME;
        }
        // animTime必须小于loopTime
        if (animTime > 0 && animTime > loopTime) {
            animTime = loopTime;
        }

        ViewPager.PageTransformer transformer = null;
        switch (animStyle) {
            case TransformerStyle.ACCORDION_LEFT:
                transformer = new AccordionLeftTransformer();
                break;
            case TransformerStyle.ACCORDION_UP:
                transformer = new AccordionUpTransformer();
                break;
            case TransformerStyle.CUBE_LEFT:
                transformer = new CubeLeftTransformer();
                break;
            case TransformerStyle.CUBE_UP:
                transformer = new CubeUpTransformer();
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
                    handler.sendEmptyMessageDelayed(CODE_SCROLL, loopTime);
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
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = onCreateItemView(position % imgLength);
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
            int index = position % imgLength;
            if (loopDotsViews.size() > 0 && imgLength > 0) {
                for (LoopDotsView loopDotsView : loopDotsViews) {
                    loopDotsView.updateStatus(index, showIndex);
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

    private void start() {
        loopDotsViews.clear();
        loopTitleViews.clear();
        getLoopChild(this);
        realIndex = 1000 * imgLength;
        showIndex = -1;
        viewPager.setOnPageChangeListener(new LoopPageChangeListener());
        viewPager.setAdapter(new LoopPagerAdapter());
        viewPager.setOnTouchListener(this);
        viewPager.setCurrentItem(realIndex);
        handler.removeCallbacksAndMessages(null);
        if (loopTime > 0) {
            handler.sendEmptyMessageDelayed(CODE_SCROLL, loopTime);
        }
    }

    private View onCreateItemView(int index) {
        if (onCreateItemViewListener != null) {
            return onCreateItemViewListener.getItemView(index);
        }
        ImageView view = new ImageView(getContext());
        if (imgList != null) {
            Glide.with(getContext()).load(imgList.get(index)).centerCrop().into(view);
        } else if (imgArray != null) {
            Glide.with(getContext()).load(imgArray[index]).centerCrop().into(view);
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

}
