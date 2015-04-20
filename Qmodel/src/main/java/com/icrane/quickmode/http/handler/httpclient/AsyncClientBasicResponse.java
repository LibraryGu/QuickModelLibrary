package com.icrane.quickmode.http.handler.httpclient;

import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.OnResponseListener;
import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.http.exec.client.HttpExecutorManager;
import com.icrane.quickmode.http.exec.data.packet.AbRequestPacket;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpResponsePacket;
import com.icrane.quickmode.utils.common.CommonUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.IOException;

@SuppressWarnings("ALL")
public abstract class AsyncClientBasicResponse implements OnResponseListener<HttpResponse> {

    @Override
    public AbResponsePacket handleResponse(AbClientExecutor executor, HttpResponse response) {
        // 请求数据包
        AbRequestPacket requestPacket = executor.getRequestPacket();
        // 响应数据包
        AbResponsePacket responsePacket = HttpResponsePacket.create();
        // 获取状态码
        int statusCode = response.getStatusLine().getStatusCode();

        // 如果状态码为200，则进行对网络数据的传输
        if (HttpStatus.SC_OK == statusCode) {

            // 获取网络实体
            HttpEntity mHttpEntity = response.getEntity();
            if (!CommonUtils.isEmpty(mHttpEntity)) {

                try {
                    // 读取数据
                    Object responseContent = onRead(executor, mHttpEntity);

                    // 将数据类型，数据长度，数据实体填入HttpRespDataPacket中
                    responsePacket
                            .setDataType(HttpDataType.CONTENT)
                            .setContentType(mHttpEntity.getContentType().getValue())
                            .setContentLength(mHttpEntity.getContentLength())
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
                            .setClientHeaders(response.getAllHeaders());

                    if (executor.getRequestPacket().isUseMemoryCache()) {
                        executor.getLruCache().put(requestPacket.getCompleteUri(), responseContent);
                    }

                } catch (IllegalStateException e) {
                    executor.getHttpExecutorManager().commitErrorMessage(HttpExecutorManager.ERROR,
                            HttpError.ERROR_EXCEPTION, responsePacket, e);
                } catch (IOException e) {
                    executor.getHttpExecutorManager().commitErrorMessage(HttpExecutorManager.ERROR,
                            HttpError.ERROR_EXCEPTION, responsePacket, e);
                } finally {
                    // 关闭池中连接
                    executor.closeConnections();
                }

            } else {
                executor.getHttpExecutorManager().commitErrorMessage(HttpExecutorManager.ERROR,
                        HttpError.ERROR_STR, responsePacket, "HttpEntity is null!");
            }
        }
        return responsePacket;

    }

    protected abstract Object onRead(AbClientExecutor executor, HttpEntity entity) throws IOException;

}
