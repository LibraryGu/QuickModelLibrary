package com.icrane.quickmode.utils.reflect;

import com.icrane.quickmode.model.JSONConvert;
import com.icrane.quickmode.model.JSONConvertModel;
import com.icrane.quickmode.utils.common.CommonUtils;

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
public final class JSONReflector<T> {

    // 测试使用
    public static final String TAG = "JSONReflector";

    public enum ConvertObject {
        OBJECT, ARRAY
    }

    /**
     * 将JSONObject对象转换为指定类型对象
     *
     * @param jsonObject JSONObject对象
     * @param type       对象类型
     * @param f_type     反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰。
     * @param <T>        泛型
     * @return 执行泛型类型T的对象
     * @throws org.json.JSONException           {@link org.json.JSONException}
     * @throws java.lang.IllegalAccessException {@link java.lang.IllegalAccessException}
     */
    public static <T> T toModel(JSONObject jsonObject, Type type, Reflector.ReflectType f_type) throws JSONException, IllegalAccessException {

        if (jsonObject == null) throw new NullPointerException("JSONObject is null");
        // 获取迭代器
        Iterator<String> keys = jsonObject.keys();
        // 获取实例
        T object = Reflector.newInstance((Class<?>) type);
        // 获取实例类型
        Class<?> cls = object.getClass();
        // 获取域数组
        Field[] fields = Reflector.getFields(cls, f_type);
        // 生成Map实例
        Map<String, Field> fieldsMap = Reflector.convertFieldsToMap(fields);

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
     * 将JSONArray转换成List,指定列表中可容纳的对象类型
     *
     * @param jsonArray JSONArray对象
     * @param type      指定对象类型
     * @param f_type    反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰
     * @param <T>       泛型
     * @return 指定泛型T对象的一个列表对象
     * @throws org.json.JSONException           {@link org.json.JSONException}
     * @throws java.lang.IllegalAccessException {@link java.lang.IllegalAccessException}
     */
    public static <T> List<T> toModel(JSONArray jsonArray, Type type, Reflector.ReflectType f_type) throws JSONException, IllegalAccessException {

        if (jsonArray == null) throw new NullPointerException("JSONArray is null");
        List<T> objectList = new ArrayList<T>();
        for (int i = 0; i < jsonArray.length(); i++) {
            T object = toModel(jsonArray.getJSONObject(i), type, f_type);
            objectList.add(object);
        }
        return objectList;

    }

    /**
     * 将JSONObject对象转换成Map对象
     *
     * @param jsonObject 指定对象
     * @param f_type     反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰
     * @return Map对象
     * @throws org.json.JSONException           {@link org.json.JSONException}
     * @throws java.lang.IllegalAccessException {@link java.lang.IllegalAccessException}
     */
    public static Map<String, Object> toModel(JSONObject jsonObject, Reflector.ReflectType f_type) throws JSONException, IllegalAccessException {

        if (jsonObject == null) throw new NullPointerException("JSONObject is null");
        Map<String, Object> category = new HashMap<String, Object>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.opt(key);
            if (value instanceof JSONArray) {
                value = toModel((JSONArray) value, Object.class, f_type);
            } else if (value instanceof JSONArray) {
                value = toModel((JSONObject) value, f_type);
            }
            category.put(key, value);
        }
        return category;

    }

    /**
     * 将Object对象转换成JSONObject
     *
     * @param object 指定对象
     * @param f_type 反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰
     * @return JSONObject对象
     * @throws java.lang.IllegalAccessException {@link java.lang.IllegalAccessException}
     */
    public static JSONObject toJSONObject(Object object, Reflector.ReflectType f_type) throws IllegalAccessException {

        if (object == null) throw new NullPointerException("object is null");
        Map<String, Object> category = new HashMap<String, Object>();
        Field[] fields = Reflector.getFields(object.getClass(), f_type);

        for (Field field : fields) {
            Object value = field.get(object);
            if (value instanceof JSONConvertModel) {
                category.put(field.getName(), ((JSONConvert) value).convertToJSONObject());
            } else {
                category.put(field.getName(), value);
            }
        }
        JSONObject jsonObject = new JSONObject(category);
        return jsonObject;

    }

    /**
     * 将Object对象转换成JSONArray
     *
     * @param object 指定对象
     * @param f_type 反射类型,推荐设置为DEFAULT,并且要转换对象内部属性都用public修饰符修饰
     * @return JSONArray对象
     * @throws java.lang.IllegalAccessException {@link java.lang.IllegalAccessException}
     */
    public static JSONArray toJSONArray(Object object, Reflector.ReflectType f_type) throws IllegalAccessException {

        if (object == null) throw new NullPointerException("object is null");
        List<Object> category = new ArrayList<Object>();
        Field[] fields = Reflector.getFields(object.getClass(), f_type);

        for (Field field : fields) {
            Object value = field.get(object);
            if (!CommonUtils.isEmpty(value)) {
                if (value instanceof JSONConvertModel) {
                    category.add(((JSONConvert) value).convertToJSONObject());
                } else {
                    category.add(value);
                }
            }
        }
        JSONArray array = new JSONArray(category);
        return array;

    }


}