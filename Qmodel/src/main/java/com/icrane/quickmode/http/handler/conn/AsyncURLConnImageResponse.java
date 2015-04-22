package com.icrane.quickmode.http.handler.conn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;
import com.icrane.quickmode.http.IAsyncResponseCallback;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by gujiwen on 15/3/19.
 */
public abstract class AsyncURLConnImageResponse extends AsyncURLConnBasicResponse implements IAsyncResponseCallback<Bitmap> {

    @Override
    public Object onRead(AbClientExecutor executor, HttpURLConnection urlConnection) throws IOException {
        return BitmapFactory.decodeStream(urlConnection.getInputStream());
    }

    @Override
    public Object onReceive(AbResponsePacket response) {

        Bitmap bitmap = null;
        HttpDataType dataType = response.getDataType();

        switch (dataType) {
            case CONTENT:
                bitmap = (Bitmap) response.getContent();
                break;
            case CACHE_CONTENT:
                bitmap = (Bitmap) response.getCacheContent();
                break;
            default:
                break;
        }
        return bitmap;

    }

    @Override
    public void onUpdate(Object obj) {
        this.onHandle((Bitmap) obj);
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
}
