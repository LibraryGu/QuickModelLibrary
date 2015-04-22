package com.icrane.quickmode.http;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;

import java.io.IOException;

/**
 * Created by gujiwen on 15/4/21.
 */
public interface IAsyncWritable<T> {

    /**
     * 当实现此接口可以调用onWrite()方法实现往网络写入数据操作
     *
     * @param executor HTTP执行者
     * @param writable 可写入的数据
     * @throws IOException IO异常
     */
    public void onWrite(AbClientExecutor executor, T writable) throws IOException;
}
