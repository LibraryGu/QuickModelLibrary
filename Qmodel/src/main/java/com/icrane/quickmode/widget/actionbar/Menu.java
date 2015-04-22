package com.icrane.quickmode.widget.actionbar;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/**
 * Created by gujiwen on 15/4/17.
 */
public interface Menu {

    public enum Position {
        DRAWABLE_LEFT,
        DRAWABLE_TOP,
        DRAWABLE_RIGHT,
        DRAWABLE_BOTTOM
    }

    /**
     * 设置Menu显示的文字
     *
     * @param resId     文字的资源id
     * @param textSize  字体大小
     * @param textColor 字体颜色
     * @param tf        字体样式
     */
    public void menuText(int resId, float textSize, int textColor, Typeface tf);

    /**
     * 设置Menu显示的文字
     *
     * @param text      字符串
     * @param textSize  字体大小
     * @param textColor 字体颜色
     * @param tf        字体样式
     */
    public void menuText(String text, float textSize, int textColor, Typeface tf);

    /**
     * 设置Menu的Icon
     *
     * @param resId    icon的资源id
     * @param position 放置在哪个位置:
     *                 DRAWABLE_LEFT,
     *                 DRAWABLE_TOP,
     *                 DRAWABLE_RIGHT,
     *                 DRAWABLE_BOTTOM
     */
    public void menuIcon(int resId, Position position);

    /**
     * 设置Menu的Icon
     *
     * @param drawable 可变图像
     * @param position 放置在哪个位置:
     *                 DRAWABLE_LEFT,
     *                 DRAWABLE_TOP,
     *                 DRAWABLE_RIGHT,
     *                 DRAWABLE_BOTTOM
     */
    public void menuIcon(Drawable drawable, Position position);

    /**
     * 设置Menu的Icon
     *
     * @param bitmap   位图
     * @param position 放置在哪个位置:
     *                 DRAWABLE_LEFT,
     *                 DRAWABLE_TOP,
     *                 DRAWABLE_RIGHT,
     *                 DRAWABLE_BOTTOM
     */
    public void menuIcon(Bitmap bitmap, Position position);

}
