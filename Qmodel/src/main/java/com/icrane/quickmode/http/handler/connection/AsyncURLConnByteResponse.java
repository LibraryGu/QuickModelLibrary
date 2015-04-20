package com.icrane.quickmode.http.handler.connection;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.utils.common.Streams;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gujiwen on 15/3/18.
 */
@SuppressWarnings("ALL")
public abstract class AsyncURLConnByteResponse extends AsyncURLConnBasicResponse {

    /**
     * 默认缓冲大小
     */
    public static final int DEFAULT_BUFFSIZE = 1024 * 1024;

    @Override
    public Object onRead(AbClientExecutor executor, InputStream is, int contentLength) throws IOException {
        return Streams.read(is, DEFAULT_BUFFSIZE, contentLength);
    }
}
