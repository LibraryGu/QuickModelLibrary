package com.icrane.quickmode.http.handler.client;

import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.http.exec.client.HttpExecutorManager;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpResponsePacket;
import com.icrane.quickmode.http.handler.AsyncResponseHandler;
import com.icrane.quickmode.utils.common.CommonUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;

@SuppressWarnings("ALL")
public abstract class AsyncClientBasicResponse extends AsyncResponseHandler<HttpResponse, HttpEntity> {

    @Override
    public int getResponseCode(HttpResponse response) throws IOException {
        return response.getStatusLine().getStatusCode();
    }

    @Override
    public AbResponsePacket onPacked(AbClientExecutor executor, HttpResponse response) throws IOException {

        // 请求数据包
        AbRequestPacket requestPacket = executor.getRequestPacket();
        // 响应数据包
        AbResponsePacket responsePacket = HttpResponsePacket.create();
        try {
            // 获取网络实体
            HttpEntity mHttpEntity = response.getEntity();
            if (CommonUtils.isEmpty(mHttpEntity)) {
                executor.getHttpExecutorManager().commitErrorMessage(HttpExecutorManager.ERROR,
                        HttpError.ERROR_STR, responsePacket, "HttpEntity is null!");
                return CommonUtils.isEmpty(responsePacket) ? HttpResponsePacket.create() : responsePacket;
            }
            // 读取数据
            Object responseContent = onRead(executor, mHttpEntity);

            // 将数据类型，数据长度，数据实体填入HttpRespDataPacket中
            responsePacket = HttpResponsePacket.create()
                    .setDataType(HttpDataType.CONTENT)
                    .setContentType(mHttpEntity.getContentType().getValue())
                    .setContentLength(mHttpEntity.getContentLength())
                    .setContent(responseContent)
                    .setUri(executor.getRequestPacket().getUri())
                    .setStatusCode(getResponseCode(response))
                    .setCompleteUri(requestPacket.getCompleteUri())
                    .setUseMemoryCache(requestPacket.isUseMemoryCache())
                    .setUseHardWareCache(requestPacket.isUseHardWareCache())
                    .setCharset(requestPacket.getCharset())
                    .setRequestType(requestPacket.getRequestType())
                    .setHttpRequestForm(requestPacket.getHttpRequestForm())
                    .setCacheType(requestPacket.getCacheType())
                    .setClientHeaders(response.getAllHeaders());

            if (executor.getRequestPacket().isUseMemoryCache())
                executor.getLruCache().put(requestPacket.getCompleteUri(), responseContent);

        } finally {
            // 关闭连接
            executor.closeConnections();
        }
        return responsePacket;
    }
}
