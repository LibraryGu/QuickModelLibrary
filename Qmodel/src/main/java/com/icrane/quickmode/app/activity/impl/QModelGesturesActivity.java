package com.icrane.quickmode.app.activity.impl;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.icrane.quickmode.app.activity.QModelActivity;
import com.icrane.quickmode.app.effect.SlideEffect;

/**
 * Created by gujiwen on 15/4/9.
 */
@SuppressWarnings("ALL")
public class QModelGesturesActivity extends QModelActivity {

    /**
     * 默认最大值
     */
    public static final int DEFAULT_MAX = 55;
    /**
     * 默认X坐标触发距离
     */
    public static final int DEFAULT_DISTANCE_X = DEFAULT_MAX;
    /**
     * 默认Y坐标触发距离
     */
    public static final int DEFAULT_DISTANCE_Y = DEFAULT_MAX;
    /**
     * 默认测量最大速度
     */
    public static final int DEFAULT_VELOCITY = DEFAULT_MAX;

    /**
     * X坐标上手指最大距离
     */
    private int maxDistanceX = DEFAULT_DISTANCE_X;

    /**
     * Y坐标上手指最大距离
     */
    private int maxDistanceY = DEFAULT_DISTANCE_Y;

    /**
     * X坐标上手指滑动最大速度
     */
    private int maxVelocityX = DEFAULT_VELOCITY;

    /**
     * Y坐标上手指滑动最大速度
     */
    private int maxVelocityY = DEFAULT_VELOCITY;

    /**
     * 用于打开滑动效果
     */
    private boolean isSliding = true;

    /**
     * 用于打开左滑效果
     */
    private boolean isSlidingLeft = false;

    /*
     * 用于打开右滑效果
     */
    private boolean isSlidingRight = false;

    /**
     * 用于打开下滑效果
     */
    private boolean isSlidingBottom = false;

    /**
     * 用于打开上滑效果
     */
    private boolean isSlidingTop = false;

    /**
     * 用于监听滑动
     */
    private OnSlideListener onSlideListener;

    /**
     * 用于接收dispatchTouchEvent方法中的事件
     */
    private GestureDetector detector;

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e1 != null && e2 != null) {
                float distanceX = Math.abs(e2.getX() - e1.getX());
                float distanceY = Math.abs(e2.getY() - e1.getY());
                if (distanceX > maxDistanceX && distanceY < maxDistanceY
                        && distanceX > distanceY) {

                    if (velocityX > maxVelocityX && isSlidingRight) {
                        onSlidingToDirection(SlideEffect.SlideDirection.LEFT_TO_RIGHT);
                        return true;
                    } else if (velocityX < -maxVelocityX && isSlidingLeft) {
                        onSlidingToDirection(SlideEffect.SlideDirection.RIGHT_TO_LEFT);
                        return true;
                    }

                } else if (distanceX < maxDistanceX && distanceY > maxDistanceY
                        && distanceX < distanceY) {

                    if (velocityY > maxVelocityY && isSlidingBottom) {
                        onSlidingToDirection(SlideEffect.SlideDirection.TOP_TO_BOTTOM);
                        return true;
                    } else if (velocityY < -maxVelocityY && isSlidingTop) {
                        onSlidingToDirection(SlideEffect.SlideDirection.BOTTOM_TO_TOP);
                        return true;
                    }

                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化
        detector = new GestureDetector(this, mOnGestureListener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean superTouchEvent = super.dispatchTouchEvent(ev);
        if (isSliding) {
            return detector.onTouchEvent(ev);
        } else {
            return superTouchEvent;
        }

    }

    /**
     * 是否可滑动
     *
     * @return boolean值，如果结果为true则表示开启了滑动
     */
    public boolean isSliding() {
        return isSliding;
    }

    /**
     * 设置开启可滑动
     *
     * @param isSliding boolean值，设置如果为true则表示打开，false为关闭。
     */
    public void setSlidingEnable(boolean isSliding) {
        this.isSliding = isSliding;
    }

    /**
     * 是否开启滑动，从左到右方向
     *
     * @return boolean值，如果结果为true则表示开启了滑动
     */
    public boolean isSlidingRight() {
        return isSlidingRight;
    }

    /**
     * 设置开启可滑动，从左到右方向
     *
     * @param isSliding boolean值，设置如果为true则表示打开，false为关闭。
     */
    public void setSlidingRightEnable(boolean isSliding) {
        this.isSlidingRight = isSliding;
    }

    /**
     * 是否开启滑动，从右到左方向
     *
     * @return boolean值，如果结果为true则表示开启了滑动
     */
    public boolean isSlidingLeft() {
        return isSlidingLeft;
    }

    /**
     * 设置开启可滑动，从右到左方向
     *
     * @param isSlidingLeft boolean值，设置如果为true则表示打开，false为关闭。
     */
    public void setSlidingRightToLeftEnable(boolean isSlidingLeft) {
        this.isSlidingLeft = isSlidingLeft;
    }

    /**
     * 是否开启滑动，从上到下方向
     *
     * @return boolean值，如果结果为true则表示开启了滑动
     */
    public boolean isSlidingBottom() {
        return isSlidingBottom;
    }

    /**
     * 设置开启可滑动，从上到下方向
     *
     * @param isSlidingBottom boolean值，设置如果为true则表示打开，false为关闭。
     */
    public void setSlidingTopToBottomEnable(boolean isSlidingBottom) {
        this.isSlidingBottom = isSlidingBottom;
    }

    /**
     * 是否开启滑动，从下到上方向
     *
     * @return boolean值，如果结果为true则表示开启了滑动
     */
    public boolean isSlidingTop() {
        return isSlidingTop;
    }

    /**
     * 设置开启可滑动，从下到上方向
     *
     * @param isSlidingTop boolean值，设置如果为true则表示打开，false为关闭。
     */
    public void setSlidingBottomToTopEnable(boolean isSlidingTop) {
        this.isSlidingTop = isSlidingTop;
    }

    /**
     * 获取滑动监听
     *
     * @return 滑动监听对象
     */
    public OnSlideListener getOnSlideListener() {
        return onSlideListener;
    }

    /**
     * 设置滑动监听
     *
     * @param onSlideListener 滑动监听对象
     */
    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }

    /**
     * 获取滑动时,两点X坐标间的最大距离
     *
     * @return 两点X坐标间的最大距离
     */
    public int getMaxDistanceX() {
        return maxDistanceX;
    }

    /**
     * 设置滑动时,两点X坐标间的最大距离
     *
     * @param maxDistanceX 两点X坐标间的最大距离
     */
    public void setMaxDistanceX(int maxDistanceX) {
        this.maxDistanceX = maxDistanceX;
    }

    /**
     * 获取滑动时,两点Y坐标间的最大距离
     *
     * @return 两点Y坐标间的最大距离
     */
    public int getMaxDistanceY() {
        return maxDistanceY;
    }

    /**
     * 设置滑动时,两点Y坐标间的最大距离
     *
     * @param maxDistanceY
     */
    public void setMaxDistanceY(int maxDistanceY) {
        this.maxDistanceY = maxDistanceY;
    }

    /**
     * 获取滑动时,X坐标上的最大速度
     *
     * @return X坐标上的最大速度
     */
    public int getMaxVelocityX() {
        return maxVelocityX;
    }

    /**
     * 设置滑动时,X坐标上的最大速度
     *
     * @param maxVelocityX X坐标上的最大速度
     */
    public void setMaxVelocityX(int maxVelocityX) {
        this.maxVelocityX = maxVelocityX;
    }

    /**
     * 获取滑动时,Y坐标上的最大速度
     *
     * @return Y坐标上的最大距离
     */
    public int getMaxVelocityY() {
        return maxVelocityY;
    }

    /**
     * 设置滑动时,Y坐标上的最大速度
     *
     * @param maxVelocityY Y坐标上的最大速度
     */
    public void setMaxVelocityY(int maxVelocityY) {
        this.maxVelocityY = maxVelocityY;
    }

    /**
     * 从左至右滑动
     */
    protected void onSlidingToDirection(SlideEffect.SlideDirection direction) {
        if (onSlideListener != null) {
            onSlideListener.onSliding(direction);
        }
    }

    /**
     * 用于监听滑动的监听接口，当触发时，监听将在onSliding()方法中返回一个SlideDirection对象，
     * 这个对象决定你触发的方向。
     */
    public interface OnSlideListener {
        public void onSliding(SlideEffect.SlideDirection direction);
    }

}
