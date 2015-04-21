package com.icrane.quickmode.model;

import android.os.Parcel;

import com.icrane.quickmode.utils.reflect.AMPlusReflector;
import com.icrane.quickmode.utils.reflect.JSONReflector;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by gujiwen on 15/4/21.
 */
@SuppressWarnings("ALL")
public abstract class JSONConvertModel extends ParcelableModel implements JSONConvert {

    public JSONConvertModel(Parcel source) {
        super(source);
    }

    @Override
    public JSONObject convertToJSONObject() throws IllegalAccessException {
        return JSONReflector.toJSONObject(this, AMPlusReflector.ReflectType.DEFAULT);
    }

    @Override
    public JSONArray convertToJSONArray() throws IllegalAccessException {
        return JSONReflector.toJSONArray(this, AMPlusReflector.ReflectType.DEFAULT);
    }

    @Override
    public String convertToJSONString(JSONReflector.ConvertObject object) throws IllegalAccessException {
        switch (object) {
            case OBJECT:
                return convertToJSONObject().toString();
            case ARRAY:
                return convertToJSONArray().toString();
        }
        return null;
    }
}
