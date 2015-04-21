package com.icrane.quickmode.utils.reflect;

import com.icrane.quickmode.model.JSONConvert;
import com.icrane.quickmode.model.JSONConvertModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * 反射类，利用反射机制将json反射为对象
 */
@SuppressWarnings("ALL")
public final class JSONReflector<T> {

    // 测试使用
    public static final String TAG = "JSONReflector";

    /**
     * 转化为对象
     *
     * @param jsonObj JSONObject对象
     * @param type    对象类型
     * @param f_type  反射类型
     * @return <T> 返回指定类型的对象
     */
    public static <T> T toModel(JSONObject jsonObj, Type type, AMPlusReflector.ReflectType f_type) {
        T object = null;
        if (jsonObj != null) {
            // 获取迭代器
            Iterator<String> keys = jsonObj.keys();
            // 获取实例
            object = AMPlusReflector.newInstance((Class<?>) type);
            // 获取实例类型
            Class<?> cls = object.getClass();
            // 获取域数组
            Field[] fields = AMPlusReflector.getFields(cls, f_type);
            // 生成Map实例
            Map<String, Field> fieldsMap = AMPlusReflector
                    .convertFieldsToMap(fields);
            while (keys.hasNext()) {
                String key = keys.next();
                final Field field = fieldsMap.get(key);
                if (field != null) {
                    try {
                        final Object value = jsonObj.get(key);
                        field.set(object, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return object;
    }

    /**
     * 转化为集合
     *
     * @param jsonArray JSONArray对象
     * @param type      指定对象类型
     * @param f_type    反射类型
     * @return List<T>对象
     */
    public static <T> List<T> toModel(JSONArray jsonArray, Type type,
                                      AMPlusReflector.ReflectType f_type) {
        List<T> objList = new ArrayList<T>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    T object = toModel(jsonArray.getJSONObject(i), type, f_type);
                    objList.add(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return objList;
    }

    /**
     * 转换成JSONObject
     *
     * @param obj    指定对象
     * @param f_type 反射类型
     * @return JSONObject对象
     */
    public static JSONObject toModel(Object obj, AMPlusReflector.ReflectType f_type) {

        Map<String, Object> objMap = new HashMap<String, Object>();
        Object object = obj;
        Field[] fields = AMPlusReflector.getFields(object.getClass(), f_type);

        for (Field field : fields) {
            try {
                Object value = field.get(object);
                if (value instanceof JSONConvertModel) {
                    objMap.put(field.getName(), ((JSONConvert) value).convertToJSON());
                } else {
                    objMap.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        JSONObject jsonObject = new JSONObject(objMap);

        return jsonObject;
    }
}
