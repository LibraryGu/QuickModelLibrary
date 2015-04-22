package com.icrane.quickmode.http;

import com.icrane.quickmode.http.HttpError;

/**
 * Created by gujiwen on 15/3/19.
 */
public interface IAsyncResponseCallback<T> {
    /**
     * 进行处理
     *
     * @param data data对象
     */
    public void onHandle(T data);

    /**
     * 异常处理
     *
     * @param error 错误信息
     * @param obj   返回的错误信息对象
     */
    public void onFailure(HttpError error, Object obj);
}
