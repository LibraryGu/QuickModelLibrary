package com.icrane.quickmode.app;

import android.content.Context;

@SuppressWarnings("ALL")
public interface IContextConvert<T extends Context> {
	/**
	 * 转换为泛型对应的上下文对象
	 * @return
	 */
	public T getActivityContext();
}
