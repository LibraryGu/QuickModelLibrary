package com.icrane.quickmode.http.handler.conn;

import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpResponsePacket;
import com.icrane.quickmode.http.handler.AsyncResponseHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by gujiwen on 15/4/21.
 */
public abstract class AsyncURLConnBasicResponse extends AsyncResponseHandler<HttpURLConnection> {

    @Override
    public int getResponseCode(HttpURLConnection response) throws IOException {
        return response.getResponseCode();
    }

    @Override
    public AbResponsePacket onPacked(AbClientExecutor executor, HttpURLConnection response) throws IOException {

        // 请求数据包
        AbRequestPacket requestPacket = executor.getRequestPacket();
        // 响应数据包
        AbResponsePacket responsePacket = HttpResponsePacket.create();
        try {
            // 读取数据
            Object responseContent = onRead(executor, response);
            // 将数据类型，数据长度，数据实体填入HttpRespDataPacket中
            responsePacket
                    .setDataType(HttpDataType.CONTENT)
                    .setContentType(response.getContentType())
                    .setContentLength(response.getContentLength())
                    .setContent(responseContent)
                    .setUri(requestPacket.getUri())
                    .setStatusCode(getResponseCode(response))
                    .setCompleteUri(requestPacket.getCompleteUri())
                    .setUseMemoryCache(requestPacket.isUseMemoryCache())
                    .setUseHardWareCache(requestPacket.isUseHardWareCache())
                    .setCharset(requestPacket.getCharset())
                    .setRequestType(requestPacket.getRequestType())
                    .setHttpRequestForm(requestPacket.getHttpRequestForm())
                    .setCacheType(requestPacket.getCacheType())
                    .setURLConnHeaders(response.getHeaderFields());

            if (executor.getRequestPacket().isUseMemoryCache()) {
                executor.getLruCache().put(requestPacket.getCompleteUri(), responseContent);
            }

        } finally {
            // 关闭连接
            executor.closeConnections();
        }
        return responsePacket;
    }
}
