package com.icrane.quickmode.http.handler;

import com.icrane.quickmode.http.exec.client.BasicClientExecutor;
import com.icrane.quickmode.utils.common.Streams;

import org.apache.http.HttpEntity;

import java.io.IOException;

/**
 * Created by gujiwen on 15/3/18.
 */
@SuppressWarnings("ALL")
public abstract class AsyncByteResponse extends AsyncBasicResponse {
    @Override
    public Object onRead(BasicClientExecutor executor, HttpEntity entity) throws IOException {
        return Streams.read(entity.getContent(), 1024 * 1024, entity.getContentLength());
    }
}
