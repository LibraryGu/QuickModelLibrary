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

    public enum ConvertObject {
        OBJECT, ARRAY
    }

    /**
     * 转化为对象
     *
     * @param jsonObj JSONObject对象
     * @param type    对象类型
     * @param f_type  反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰。
     * @return <T> 返回指定类型的对象
     */
    public static <T> T toModel(JSONObject jsonObject, Type type, AMPlusReflector.ReflectType f_type) throws JSONException, IllegalAccessException {

        if (jsonObject == null) throw new NullPointerException("JSONObject is null");
        // 获取迭代器
        Iterator<String> keys = jsonObject.keys();
        // 获取实例
        T object = AMPlusReflector.newInstance((Class<?>) type);
        // 获取实例类型
        Class<?> cls = object.getClass();
        // 获取域数组
        Field[] fields = AMPlusReflector.getFields(cls, f_type);
        // 生成Map实例
        Map<String, Field> fieldsMap = AMPlusReflector.convertFieldsToMap(fields);

        while (keys.hasNext()) {
            String key = keys.next();
            final Field field = fieldsMap.get(key);
            if (field == null)
                throw new NullPointerException("key is " + key + " field " + " is null");
            final Object value = jsonObject.get(key);
            field.set(object, value);
        }
        return object;

    }

    /**
     * 转化为集合
     *
     * @param jsonArray JSONArray对象
     * @param type      指定对象类型
     * @param f_type    反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰
     * @return List<T>对象
     */
    public static <T> List<T> toModel(JSONArray jsonArray, Type type, AMPlusReflector.ReflectType f_type) throws JSONException, IllegalAccessException {

        if (jsonArray == null) throw new NullPointerException("JSONArray is null");
        List<T> objectList = new ArrayList<T>();
        for (int i = 0; i < jsonArray.length(); i++) {
            T object = toModel(jsonArray.getJSONObject(i), type, f_type);
            objectList.add(object);
        }
        return objectList;

    }

    /**
     * 转换成Map
     *
     * @param jsonObject    指定对象
     * @param f_type 反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰
     * @return Map对象
     */
    public static Map<String, Object> toModel(JSONObject jsonObject, AMPlusReflector.ReflectType f_type) throws JSONException, IllegalAccessException {

        if (jsonObject == null) throw new NullPointerException("JSONObject is null");
        Map<String, Object> categorys = new HashMap<String, Object>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.opt(key);
            if (value instanceof JSONArray) {
                value = toModel((JSONArray) value, Object.class, f_type);
            } else if (value instanceof JSONArray) {
                value = toModel((JSONObject) value, f_type);
            }
            categorys.put(key, value);
        }
        return categorys;

    }

    /**
     * 转换成JSONObject
     *
     * @param object    指定对象
     * @param f_type 反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰
     * @return JSONObject对象
     */
    public static JSONObject toJSONObject(Object object, AMPlusReflector.ReflectType f_type) throws IllegalAccessException {

        if (object == null) throw new NullPointerException("object is null");
        Map<String, Object> categorys = new HashMap<String, Object>();
        Field[] fields = AMPlusReflector.getFields(object.getClass(), f_type);

        for (Field field : fields) {
            Object value = field.get(object);
            Object result = (value instanceof JSONConvertModel) ?
                    categorys.put(field.getName(), ((JSONConvert) value).convertToJSONObject())
                    : categorys.put(field.getName(), toJSONObject(value, f_type));
        }
        JSONObject jsonObject = new JSONObject(categorys);

        return jsonObject;
    }

    /**
     * 转换成JSONArray
     *
     * @param object    指定对象
     * @param f_type 反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰
     * @return JSONArray对象
     */
    public static JSONArray toJSONArray(Object object, AMPlusReflector.ReflectType f_type) throws IllegalAccessException {

        if (object == null) throw new NullPointerException("object is null");

        List<Object> categorys = new ArrayList<Object>();
        Field[] fields = AMPlusReflector.getFields(object.getClass(), f_type);

        for (Field field : fields) {
            Object value = field.get(object);
            boolean result = (value instanceof JSONConvertModel) ?
                    categorys.add(((JSONConvert) value).convertToJSONObject())
                    : categorys.add(toJSONObject(value, f_type));
        }
        JSONArray array = new JSONArray(categorys);

        return array;
    }


}