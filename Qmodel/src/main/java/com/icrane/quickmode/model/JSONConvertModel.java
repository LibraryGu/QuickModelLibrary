package com.icrane.quickmode.model;

import com.icrane.quickmode.utils.reflect.Reflector;
import com.icrane.quickmode.utils.reflect.JSONReflector;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by gujiwen on 15/4/21.
 */
public abstract class JSONConvertModel implements JSONConvert {

    @Override
    public JSONObject convertToJSONObject() throws IllegalAccessException {
        return JSONReflector.toJSONObject(this, Reflector.ReflectType.DEFAULT);
    }

    @Override
    public JSONArray convertToJSONArray() throws IllegalAccessException {
        return JSONReflector.toJSONArray(this, Reflector.ReflectType.DEFAULT);
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
