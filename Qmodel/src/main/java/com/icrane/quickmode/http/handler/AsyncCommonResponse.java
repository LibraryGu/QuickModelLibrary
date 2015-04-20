package com.icrane.quickmode.http.handler;

import com.icrane.quickmode.http.exec.client.BasicClientExecutor;
import com.icrane.quickmode.utils.common.Streams;

import org.apache.http.HttpEntity;

import java.io.IOException;

public abstract class AsyncCommonResponse extends AsyncBasicResponse {

    @Override
    public Object onRead(BasicClientExecutor executor, HttpEntity entity) throws IOException {
        return Streams.read(Streams.obtainBufferedReader(entity.getContent(), executor.getRequestPacket().getCharset()));
    }
}
