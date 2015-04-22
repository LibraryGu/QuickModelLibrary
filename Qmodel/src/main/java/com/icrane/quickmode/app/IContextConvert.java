package com.icrane.quickmode.app;

import android.content.Context;

public interface IContextConvert<T extends Context> {
    /**
     * 转换为泛型对应的上下文对象
     *
     * @return 泛型类型
     */
    public T getActivityContext();
}
