package com.icrane.quickmode.http.handler.conn;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.utils.common.Streams;

import java.io.IOException;
import java.net.HttpURLConnection;

@SuppressWarnings("ALL")
public abstract class AsyncURLConnCommonResponse extends AsyncURLConnBasicResponse {

    @Override
    public Object onRead(AbClientExecutor executor, HttpURLConnection urlConnection) throws IOException {
        return Streams.read(Streams.obtainBufferedReader(urlConnection.getInputStream(), executor.getRequestPacket().getCharset()));
    }
}
