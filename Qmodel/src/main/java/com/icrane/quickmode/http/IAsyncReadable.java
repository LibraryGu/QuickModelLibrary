package com.icrane.quickmode.http;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;

import java.io.IOException;

/**
 * Created by gujiwen on 15/4/21.
 */
public interface IAsyncReadable<T> {
    /**
     * 当实现此接口可以调用onRead()方法实现从网络读取数据操作
     *
     * @param executor HTTP执行者
     * @param readable 可读取的数据
     * @return 返回读取的
     * @throws IOException IO异常
     */
    public Object onRead(AbClientExecutor executor, T readable) throws IOException;
}
