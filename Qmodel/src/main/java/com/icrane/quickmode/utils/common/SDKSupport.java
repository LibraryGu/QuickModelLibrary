package com.icrane.quickmode.utils.common;

import android.os.Build;

public final class SDKSupport {

    /**
     * 兼容已经设定的最低默认版本
     *
     * @return true表示兼容，反之返回false
     */
    public static boolean isSupportDefaultTargetApi() {
        return isSupportTargetApiVersion(Build.VERSION_CODES.FROYO);
    }

    /**
     * 获取当前sdk版本
     *
     * @return 当前sdk版本
     */
    public static int getCurrentSystemApiVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 兼容版本
     *
     * @param versionCode 指定版本号
     * @return true表示兼容，反之返回false
     */
    public static boolean isSupportTargetApiVersion(int versionCode) {
        if (getCurrentSystemApiVersion() >= versionCode) {
            return true;
        }
        return false;
    }
}
