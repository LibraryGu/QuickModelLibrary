package com.icrane.quickmode.widget.actionbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.icrane.quickmode.R;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.common.SDKSupport;

/**
 * Created by gujiwen on 15/4/16.
 */
@SuppressWarnings("ALL")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class ActionBarMenu extends TextView implements Menu {

    public ActionBarMenu(Context context) {
        this(context, null);
    }

    public ActionBarMenu(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.ActionBarMenu_ActionBarMenuStyle);
    }

    public ActionBarMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置菜单按钮图标放置的位置
     *
     * @param drawable  图片
     * @param direction 位置方向
     */
    protected void setMenuIconDirection(Drawable drawable, Position direction) {

        Drawable lDrawable = null;
        Drawable tDrawable = null;
        Drawable rDrawable = null;
        Drawable bDrawable = null;

        switch (direction) {
            case DRAWABLE_LEFT:
                lDrawable = drawable;
                break;
            case DRAWABLE_TOP:
                tDrawable = drawable;
                break;
            case DRAWABLE_RIGHT:
                rDrawable = drawable;
                break;
            case DRAWABLE_BOTTOM:
                bDrawable = drawable;
                break;
        }

        if (SDKSupport.isSupportTargetApiVersion(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(lDrawable, tDrawable, rDrawable, bDrawable);
            return;
        }
        this.setCompoundDrawablesWithIntrinsicBounds(lDrawable, tDrawable, rDrawable, bDrawable);
    }

    @Override
    public void menuText(int resId, float textSize, int textColor, Typeface tf) {
        this.menuText(getResources().getString(resId), textSize, textColor, tf);
    }

    @Override
    public void menuText(String text, float textSize, int textColor, Typeface tf) {
        this.setText(text);
        this.setTextSize(textSize);
        this.setTextColor(textColor);
        this.setTypeface(tf);
    }

    @Override
    public void menuIcon(int resId, Position position) {
        menuIcon(CommonUtils.obtainDrawable(getContext(), resId), position);
    }

    @Override
    public void menuIcon(Drawable drawable, Position position) {
        this.setMenuIconDirection(drawable, position);
    }

    @Override
    public void menuIcon(Bitmap bitmap, Position position) {
        menuIcon(CommonUtils.obtainDrawable(getContext(), bitmap), position);
    }

}
