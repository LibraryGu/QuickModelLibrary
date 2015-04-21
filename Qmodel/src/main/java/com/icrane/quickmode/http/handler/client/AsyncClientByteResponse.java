package com.icrane.quickmode.http.handler.client;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.utils.common.Streams;

import org.apache.http.HttpEntity;

import java.io.IOException;

/**
 * Created by gujiwen on 15/3/18.
 */
@SuppressWarnings("ALL")
public abstract class AsyncClientByteResponse extends AsyncClientBasicResponse {

    /**
     * 默认缓冲大小
     */
    public static final int DEFAULT_BUFFSIZE = 1024 * 1024;

    @Override
    public Object onRead(AbClientExecutor executor, HttpEntity entity) throws IOException {
        return Streams.read(entity.getContent(), DEFAULT_BUFFSIZE, entity.getContentLength());
    }
}
