package com.icrane.quickmode.http.handler;

import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.OnResponseListener;
import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.http.exec.client.HttpExecutorManager;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpResponsePacket;
import com.icrane.quickmode.utils.common.CommonUtils;

import org.apache.http.HttpStatus;

import java.io.IOException;

/**
 * Created by gujiwen on 15/4/21.
 */
public abstract class AsyncResponseHandler<T> implements OnResponseListener<T> {

    public abstract int getResponseCode(T response) throws IOException;

    @Override
    public AbResponsePacket handleResponse(AbClientExecutor executor, T response) {

        // 响应数据包
        AbResponsePacket responsePacket = HttpResponsePacket.create();
        try {
            this.onWrite(executor, response);
            // 如果状态码为200，则进行对网络数据的传输
            int statusCode = getResponseCode(response);
            if (HttpStatus.SC_OK == statusCode) {
                responsePacket = onPacked(executor, response);
            } else {
                responsePacket.setDataType(HttpDataType.CONTENT).setContent("");
            }
        } catch (IllegalStateException e) {
            executor.getHttpExecutorManager().commitErrorMessage(HttpExecutorManager.ERROR,
                    HttpError.ERROR_EXCEPTION, responsePacket, e);
        } catch (IOException e) {
            executor.getHttpExecutorManager().commitErrorMessage(HttpExecutorManager.ERROR,
                    HttpError.ERROR_EXCEPTION, responsePacket, e);
        }

        return CommonUtils.isEmpty(responsePacket) ? HttpResponsePacket.create() : responsePacket;

    }
}
