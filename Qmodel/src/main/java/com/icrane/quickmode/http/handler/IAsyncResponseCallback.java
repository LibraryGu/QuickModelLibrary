package com.icrane.quickmode.http.handler;

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
     * @param error
     * @param obj
     */
    public void onFailure(HttpError error, Object obj);
}
