package com.icrane.quickmode.http.handler.client;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public abstract class AsyncClientCommonResponse extends AsyncClientBasicResponse {

    @Override
    public Object onRead(AbClientExecutor executor, HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity(), executor.getRequestPacket().getCharset().obtain());
    }
}
