package com.icrane.quickmode.device;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import com.icrane.quickmode.app.QModel;
import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.common.LogUtils;

public final class Dimens implements Releasable {

    //	public static final String TAG = "DeviceDimens";
    public static final String ID_STATUS_BAR_HEIGHT = "status_bar_height";
    public static final String CLASS_COM_ANDROID_INTERNAL_DIMEN = "com.android.internal.R$dimen";

    public static final int DEFAULT_DPI = 160;

    private Context context = null;
    private DisplayMetrics mDisplayMetrics = null;
    private static Dimens mDeviceDimens = null;

    /**
     * 构造方法
     */
    private Dimens() {
        this.initDeviceDimenManager();
    }

    /**
     * 获取实例
     *
     * @return Dimens
     */
    public static Dimens getInstance() {
        if (CommonUtils.isEmpty(mDeviceDimens)) {
            mDeviceDimens = new Dimens();
        }
        return mDeviceDimens;
    }

    /**
     * 初始化
     */
    private void initDeviceDimenManager() {
        this.context = QModel.getTopActivity();
        this.mDisplayMetrics = new DisplayMetrics();
        QModel.getTopActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(mDisplayMetrics);
    }

    /**
     * 获取屏幕真实宽度
     *
     * @return 屏幕真实宽度
     */
    public int getScreenWidth() {
        return this.mDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕真实高度
     *
     * @return 屏幕真实高度
     */
    public int getScreenHeight() {
        return this.mDisplayMetrics.heightPixels;
    }

    /**
     * 获取密度
     *
     * @return 密度
     */
    public float getDensity() {
        return this.mDisplayMetrics.density;
    }

    /**
     * 获取设备每英寸细腻程度
     *
     * @return 设备每英寸细腻程度
     */
    public float getDensityDpi() {
        return this.mDisplayMetrics.densityDpi;
    }

    /**
     * 缩放密度
     *
     * @return 缩放密度
     */
    public float getScaleDensity() {
        return this.mDisplayMetrics.scaledDensity;
    }

    /**
     * 获取xdpi
     *
     * @return xdpi
     */
    public float getXDpi() {
        return this.mDisplayMetrics.xdpi;
    }

    /**
     * 获取屏幕取平方后的值
     *
     * @return 屏幕取平方后的值
     */
    public float getDeviceScreenSqrt() {

        int screenWidth = this.getScreenWidth();
        int screenHeight = this.getScreenHeight();
        return (float) Math.sqrt((screenWidth * screenWidth)
                + (screenHeight * screenHeight));
    }

    /**
     * 获取设备尺寸,根据公式dpi = Math.sqrt((width * width + height * height)) / size;
     *
     * @return 设备尺寸
     */
    public float getDeviceSize() {
        return (float) (this.getDensityDpi() / this.getDeviceScreenSqrt());
    }

    /**
     * 转换为px单位 ，根据公式px = dp * (dpi / 160);
     *
     * @param dp 独立像素
     * @return 转换为px单位 ，根据公式px = dp * (dpi / 160);
     */
    public float toPixel(float dp) {
        return dp * (this.getDensityDpi() / DEFAULT_DPI);
    }

    /**
     * 转换为dp单位 ，根据公式px = dp * (dpi / 160);
     *
     * @param px 像素
     * @return 转换为dp单位 ，根据公式px = dp * (dpi / 160);
     */
    public float toDp(float px) {
        return px / (getDensityDpi() / DEFAULT_DPI);
    }

    /**
     * 转化为字体单位
     *
     * @param px 像素
     * @return 转化为字体单位
     */
    public float toSp(float px) {
        return px * getScaleDensity();
    }

    /**
     * 转化为pt单位
     *
     * @param px 像素
     * @return 转化为pt单位
     */
    public float toPt(float px) {
        return px * getXDpi() * (1.0f / 72);
    }

    /**
     * 获取{com.android.internal.R$dimen}dimen系统资源
     *
     * @param source  资源属性名称
     * @param clsName 类全名
     * @return 对应属性的值
     * @throws java.lang.ClassNotFoundException {@link java.lang.ClassNotFoundException}
     */
    public static int getInternalDimenResourcesId(String source, String clsName)
            throws ClassNotFoundException {
        Class<?> cls = Class.forName(clsName);
        return CommonUtils.getResourcesId(cls, source);
    }

    /**
     * 获取状态栏高度 ,此方法不能再onCreate中使用
     *
     * @return 状态栏高度, 已废弃
     * @deprecated {@link #getRealStatusBarHeight()};
     */
    @Deprecated
    public int getStatusBarHeight() {
        Rect frame = new Rect();
        View rootView = ((Activity) this.context).getWindow().findViewById(
                Window.ID_ANDROID_CONTENT);
        rootView.getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return getRealStatusBarHeight();
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public int getRealStatusBarHeight() {
        int statubarHeight = 0;
        try {
            int id = getInternalDimenResourcesId(ID_STATUS_BAR_HEIGHT,
                    CLASS_COM_ANDROID_INTERNAL_DIMEN);
            statubarHeight = this.context.getResources()
                    .getDimensionPixelSize(id);
        } catch (ClassNotFoundException e) {
            LogUtils.i("get status bar height fail");
            e.printStackTrace();
        }
        return statubarHeight;
    }

    @Override
    public void release() {
        if (mDeviceDimens != null) {
            mDeviceDimens = null;
        }
    }
}
