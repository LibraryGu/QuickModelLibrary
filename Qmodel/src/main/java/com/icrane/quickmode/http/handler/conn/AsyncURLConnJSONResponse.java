package com.icrane.quickmode.http.handler.conn;

import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.exec.client.HttpExecutorManager;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.exec.data.packet.impl.HttpResponsePacket;
import com.icrane.quickmode.http.IAsyncResponseCallback;
import com.icrane.quickmode.utils.common.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ALL")
public abstract class AsyncURLConnJSONResponse extends AsyncURLConnCommonResponse implements IAsyncResponseCallback<JSONObject> {

    @Override
    public Object onReceive(AbResponsePacket response) {

        String jsonStr = "";
        JSONObject convertTo = null;

        try {
            if (CommonUtils.isEmpty(response)) {
                HttpExecutorManager.getInstance().commitErrorMessage(HttpExecutorManager.ERROR,
                        HttpError.ERROR_EXCEPTION, response, new NullPointerException("dataType is null"));
                return jsonStr;
            }
            HttpDataType dataType = response.getDataType();
            switch (dataType) {
                case CONTENT:
                    jsonStr = response.getContent().toString();
                    break;
                case CACHE_CONTENT:
                    jsonStr = response.getCacheContent().toString();
                    break;
            }
            convertTo = convertToJSONObject(jsonStr);

        } catch (JSONException e) {
            HttpExecutorManager.getInstance().commitErrorMessage(HttpExecutorManager.ERROR,
                    HttpError.ERROR_EXCEPTION, response, e);
        }
        return convertTo;
    }

    @Override
    public void onUpdate(Object obj) {
        if (CommonUtils.isEmpty(obj) || CommonUtils.isEmpty(obj.toString())) {
            NullPointerException nullPointerException = new NullPointerException("'obj' is null or obj cannot cast to JSONObject!");
            HttpExecutorManager.getInstance().commitErrorMessage(HttpExecutorManager.ERROR,
                    HttpError.ERROR_EXCEPTION, HttpResponsePacket.create(), nullPointerException);
        } else {
            this.onHandle((JSONObject) obj);
        }
    }

    @Override
    public void onError(AbResponsePacket response) {

        HttpError error = response.getHttpErrorMessage();

        switch (error) {
            case ERROR_EXCEPTION:
                this.onFailure(error, error.getException());
                break;
            case ERROR_STR:
                this.onFailure(error, error.getErrorMessage());
                break;
            default:
                break;
        }

    }

    /**
     * 转换为JSONObject对象
     *
     * @param jsonStr 读取回来的json字符串
     * @return JSONObject对象
     * @throws org.json.JSONException
     */
    protected JSONObject convertToJSONObject(String jsonStr)
            throws JSONException {
        JSONObject jsonData = null;
        if (!CommonUtils.isEmpty(jsonStr)) {
            jsonData = new JSONObject(jsonStr);
        }
        return jsonData;
    }

}
