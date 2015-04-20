package com.icrane.quickmode.http.handler.connection;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.utils.common.Streams;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("ALL")
public abstract class AsyncURLConnCommonResponse extends AsyncURLConnBasicResponse {

    @Override
    public Object onRead(AbClientExecutor executor, InputStream is, int contentLength) throws IOException {
        return Streams.read(Streams.obtainBufferedReader(is, executor.getRequestPacket().getCharset()));
    }
}
