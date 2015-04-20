package com.icrane.quickmode.http.handler.connection;

import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.OnResponseListener;
import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.http.exec.client.HttpExecutorManager;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpResponsePacket;
import com.icrane.quickmode.utils.common.CommonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by gujiwen on 15/4/21.
 */
@SuppressWarnings("ALL")
public abstract class AsyncURLConnBasicResponse implements OnResponseListener<HttpURLConnection> {

    @Override
    public AbResponsePacket handleResponse(AbClientExecutor executor, HttpURLConnection response) {

        // 请求数据包
        AbRequestPacket requestPacket = executor.getRequestPacket();
        // 响应数据包
        AbResponsePacket responsePacket = HttpResponsePacket.create();
        // 获取状态码
        int statusCode = 0;
        try {
            onWrite(executor, response);

            InputStream is = null;
            is = response.getInputStream();
            statusCode = response.getResponseCode();
            // 如果状态码为200，则进行对网络数据的传输
            if (HttpURLConnection.HTTP_OK == statusCode) {

                if (!CommonUtils.isEmpty(is)) {

                    // 读取数据
                    Object responseContent = onRead(executor, is, response.getContentLength());
                    // 将数据类型，数据长度，数据实体填入HttpRespDataPacket中
                    responsePacket
                            .setDataType(HttpDataType.CONTENT)
                            .setContentType(response.getContentType())
                            .setContentLength(response.getContentLength())
                            .setContent(responseContent)
                            .setUri(requestPacket.getUri())
                            .setStatusCode(statusCode)
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

                }
                return responsePacket;
            }

        } catch (IOException e) {
            executor.getHttpExecutorManager().commitErrorMessage(HttpExecutorManager.ERROR,
                    HttpError.ERROR_EXCEPTION, responsePacket, e);
        } finally {
            executor.closeConnections();
        }

        return HttpResponsePacket.create();
    }

    protected abstract void onWrite(AbClientExecutor executor, HttpURLConnection response) throws IOException;

    protected abstract Object onRead(AbClientExecutor executor, InputStream is, int contentLength) throws IOException;
}
