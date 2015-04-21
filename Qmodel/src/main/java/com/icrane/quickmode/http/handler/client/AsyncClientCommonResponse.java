package com.icrane.quickmode.http.handler.client;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.utils.common.Streams;

import org.apache.http.HttpEntity;

import java.io.IOException;

public abstract class AsyncClientCommonResponse extends AsyncClientBasicResponse {

    @Override
    public Object onRead(AbClientExecutor executor, HttpEntity entity) throws IOException {
        return Streams.read(Streams.obtainBufferedReader(entity.getContent(), executor.getRequestPacket().getCharset()));
    }
}
