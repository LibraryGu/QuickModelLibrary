package com.icrane.quickmode.model;

import com.icrane.quickmode.utils.reflect.JSONReflector;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by gujiwen on 15/4/21.
 */
@SuppressWarnings("ALL")
public interface JSONConvert {

    public JSONObject convertToJSONObject() throws IllegalAccessException;

    public JSONArray convertToJSONArray();

    public String convertToJSONString(JSONReflector.ConvertObject object) throws IllegalAccessException;

}
