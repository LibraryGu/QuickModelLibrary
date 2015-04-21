package com.icrane.quickmode.http.handler.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.icrane.quickmode.http.HttpDataType;
import com.icrane.quickmode.http.HttpError;
import com.icrane.quickmode.http.IAsyncResponseCallback;
import com.icrane.quickmode.http.exec.client.AbClientExecutor;
import com.icrane.quickmode.http.exec.data.packet.AbResponsePacket;

import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by gujiwen on 15/3/19.
 */
@SuppressWarnings("ALL")
public abstract class AsyncClientImageResponse extends AsyncClientBasicResponse implements IAsyncResponseCallback<Bitmap> {

    @Override
    public Object onRead(AbClientExecutor executor, HttpResponse response) throws IOException {
        return BitmapFactory.decodeStream(response.getEntity().getContent());
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
