package com.icrane.quickmode.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.icrane.quickmode.app.QModel;
import com.icrane.quickmode.model.Ad;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.common.LogUtils;
import com.icrane.quickmode.utils.image.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class AdNavigationView extends FrameLayout implements
        OnPageChangeListener {

    private AdPagerIndicator adPagerIndicator;
    private List<ImageView> views;

    @SuppressLint("HandlerLeak")
    private Handler adHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_SLIDE:
                    if (currentPosition < viewCount - 1) {
                        currentPosition++;
                    } else {
                        currentPosition = 0;
                    }
                    mAdViewPager.setCurrentItem(currentPosition);
                    updateOrder();
                    break;
                case STOP_SLIDE:
                    break;
                default:
                    break;
            }
        }

    };

    public static final String TAG = "AdNavigationView";
    public static final int DEFAULT_SLIDE_TIME = 3000;
    public static final int AUTO_SLIDE = 0;
    public static final int STOP_SLIDE = 1;

    private int order = AUTO_SLIDE;
    private int currentPosition = 0;
    private int viewCount = 0;

    private ViewPager mAdViewPager;
    private AdPagerAdapter mAdPagerAdapter;

    public AdNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AdNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdNavigationView(Context context) {
        super(context);
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        mAdViewPager = new ViewPager(getContext());
        mAdViewPager.setOnPageChangeListener(this);
        this.addView(mAdViewPager, new LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
    }

    /**
     * 开始自动滚动
     */
    public void startAutoSlide() {
        updateOrder();
    }

    public void updateOrder() {
        adHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adHandler.sendEmptyMessage(order);
            }
        }, DEFAULT_SLIDE_TIME);
    }

    public void stopSlide() {
        setOrder(STOP_SLIDE);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * 添加数据
     *
     * @param ads
     */
    public void addAds(List<Ad> ads, int radioResID, int drawableResID) {

        views = new ArrayList<ImageView>();
        for (Ad ad : ads) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new LayoutParams(
                    AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.WRAP_CONTENT));
            ImageLoaderUtils.getDefaultImageLoaderUtils().getImageLoader().displayImage(ad.getImageAdd(), imageView);
            views.add(imageView);
        }
        viewCount = views.size();
        initIndicator(radioResID, drawableResID);
        mAdPagerAdapter = new AdPagerAdapter(views);
        mAdViewPager.setAdapter(mAdPagerAdapter);
        mAdPagerAdapter.notifyDataSetChanged();

    }

    public void releaseViews() {
        views.clear();
        views = null;
    }

    /**
     * 初始化圆点
     */
    protected void initIndicator(int radioResID, int drawableResID) {

        adPagerIndicator = new AdPagerIndicator(getContext());
        adPagerIndicator.builderIndicator(getViewCount(), radioResID, drawableResID);
        adPagerIndicator.getIndicatorButtons().get(currentPosition).setChecked(true);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        this.addView(adPagerIndicator, params);

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        adPagerIndicator.getIndicatorButtons().get(currentPosition).setChecked(true);
    }

    static class AdPagerAdapter extends PagerAdapter {

        private static final String TAG = "AdPagerAdapter";
        private List<ImageView> adImages;

        public AdPagerAdapter(List<ImageView> adImages) {
            this.adImages = adImages;
        }

        @Override
        public int getCount() {
            return adImages.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(adImages.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LogUtils.d("instantiateItem");
            ImageView imageView = adImages.get(position);
            container.addView(imageView, 0);
            return imageView;
        }
    }

    @SuppressLint("NewApi")
    static class AdPagerIndicator extends LinearLayout implements RadioGroup.OnCheckedChangeListener {

        private int indicatorCount = 0;
        private List<RadioButton> indicatorButtons = new ArrayList<RadioButton>();

        private RadioGroup mIndicatorGroup;
        private OnIndicatorChangedListener mOnIndicatorChangedListener;

        public AdPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public AdPagerIndicator(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public AdPagerIndicator(Context context) {
            super(context);
            init();
        }

        public void init() {
        }

        /**
         * 设置个数
         *
         * @param count
         */
        public void setIndicatorCount(int count) {
            this.indicatorCount = count;
        }

        public OnIndicatorChangedListener getOnIndicatorChangedListener() {
            return mOnIndicatorChangedListener;
        }

        public void setOnIndicatorChangedListener(
                OnIndicatorChangedListener mOnIndicatorChangedListener) {
            this.mOnIndicatorChangedListener = mOnIndicatorChangedListener;
        }

        public RadioGroup getIndicatorGroup() {
            return mIndicatorGroup;
        }

        public void setIndicatorGroup(RadioGroup mIndicatorGroup) {
            this.mIndicatorGroup = mIndicatorGroup;
        }

        /**
         * 获取Indicator
         *
         * @return
         */
        public int getIndicatorCount() {
            return indicatorCount;
        }

        /**
         * 创建Indicator
         */
        public void builderIndicator(int count, int radioResID, int drawableResID) {

            this.indicatorCount = count;
            mIndicatorGroup = new RadioGroup(getContext());
            mIndicatorGroup.setOnCheckedChangeListener(this);
            mIndicatorGroup.setOrientation(HORIZONTAL);

            if (indicatorCount > 0) {
                for (int i = 0; i < indicatorCount; i++) {
                    RadioButton mIndicatorButton = (RadioButton) QModel.getInstance().getActivityContext()
                            .getLayoutInflater().inflate(radioResID, this, false);
                    mIndicatorButton.setCompoundDrawablesWithIntrinsicBounds(CommonUtils.obtainDrawable(getContext(), drawableResID), null, null, null);
                    indicatorButtons.add(mIndicatorButton);
                    mIndicatorGroup.addView(mIndicatorButton, new LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                }
            }

            this.addView(mIndicatorGroup, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (mOnIndicatorChangedListener != null) {
                mOnIndicatorChangedListener.onIndicatorChanged(this, checkedId);
            }
        }

        public List<RadioButton> getIndicatorButtons() {
            return indicatorButtons;
        }

        public void setIndicatorButtons(List<RadioButton> indicatorButtons) {
            this.indicatorButtons = indicatorButtons;
        }

        public interface OnIndicatorChangedListener {
            public void onIndicatorChanged(AdPagerIndicator indicator, int checkedId);
        }

    }

}
