package com.icrane.quickmode.utils.common;

import android.os.Build;

@SuppressWarnings("ALL")
public final class SDKSupport {
	
	/**
	 * 兼容已经设定的最低默认版本
	 * 
	 * @return
	 */
	public static boolean isSupportDefaultTargetApi() {
		return isSupportTargetApiVersion(Build.VERSION_CODES.FROYO);
	}
	
	/**
	 * 获取当前sdk版本
	 * @return
	 */
	public static int getCurrentSystemApiVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 兼容版本
	 * 
	 * @param versionCode
	 * @return
	 */
	public static boolean isSupportTargetApiVersion(int versionCode) {
		if (getCurrentSystemApiVersion() >= versionCode) {
			return true;
		}
		return false;
	}
}
