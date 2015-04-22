package com.icrane.quickmode.http.handler.client;

import com.icrane.quickmode.http.exec.client.AbClientExecutor;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by gujiwen on 15/3/18.
 */
public abstract class AsyncClientByteResponse extends AsyncClientBasicResponse {

    /**
     * 默认缓冲大小
     */
    public static final int DEFAULT_BUFFSIZE = 1024 * 1024;

    @Override
    public Object onRead(AbClientExecutor executor, HttpResponse response) throws IOException {
        return EntityUtils.toByteArray(response.getEntity());
    }
}
