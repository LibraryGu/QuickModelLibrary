package com.icrane.quickmode.model;

import com.icrane.quickmode.utils.reflect.JSONReflector;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by gujiwen on 15/4/21.
 */
public interface JSONConvert {

    /**
     * 将实现此接口的对象转换成JSONObject对象
     *
     * @return JSONObject对象
     * @throws IllegalAccessException {@link java.lang.IllegalAccessException}
     */
    public JSONObject convertToJSONObject() throws IllegalAccessException;

    /**
     * 将实现此接口的对象转换成JSONArray对象
     *
     * @return JSONArray对象
     * @throws IllegalAccessException {@link java.lang.IllegalAccessException}
     */
    public JSONArray convertToJSONArray() throws IllegalAccessException;

    /**
     * 将实现此接口的对象转换成字符串对象
     *
     * @param object
     * @return 字符串对象
     * @throws IllegalAccessException {@link java.lang.IllegalAccessException}
     */
    public String convertToJSONString(JSONReflector.ConvertObject object) throws IllegalAccessException;

}
