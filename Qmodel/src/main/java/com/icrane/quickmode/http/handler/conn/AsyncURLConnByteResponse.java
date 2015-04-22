package com.icrane.quickmode.http.handler.conn;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.utils.common.Streams;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by gujiwen on 15/3/18.
 */
public abstract class AsyncURLConnByteResponse extends AsyncURLConnBasicResponse {

    /**
     * 默认缓冲大小
     */
    public static final int DEFAULT_BUFFSIZE = 1024 * 1024;

    @Override
    public Object onRead(AbClientExecutor executor, HttpURLConnection urlConnection) throws IOException {
        return Streams.read(urlConnection.getInputStream(), DEFAULT_BUFFSIZE, urlConnection.getContentLength());
    }
}
