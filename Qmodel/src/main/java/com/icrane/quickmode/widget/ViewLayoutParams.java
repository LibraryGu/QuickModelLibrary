package com.icrane.quickmode.widget;

import android.view.ViewGroup.LayoutParams;

import com.icrane.quickmode.utils.reflect.Reflector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 * 这是一个获取视图的LayoutParams代表枚举
 *
 * @author gujiwen
 */
public enum ViewLayoutParams {

    FULL_MATCH(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT), FULL_FILL(
            LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT), FULL_WRAP(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT), FILL_AND_WRAP(
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT), MATCH_AND_WRAP(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    private int w;
    private int h;

    private ViewLayoutParams(int width, int height) {
        this.w = width;
        this.h = height;
    }

    /**
     * 获取对应类型的LayoutParams对象
     *
     * @param type LayoutParams对象类型
     * @param <T>  泛型
     * @return LayoutParams对象
     */
    public <T extends LayoutParams> T obtain(Type type) {
        return obtain(type, w, h);
    }

    /**
     * 获取对应类型的LayoutParams对象
     *
     * @param type LayoutParams对象类型
     * @param obj  参数数组
     * @param <T>  泛型
     * @return LayoutParams对象
     */
    public <T extends LayoutParams> T obtain(Type type, Object... obj) {
        Class<?> typeClass = (Class<?>) type;
        T layoutParams = null;
        try {
            Class<?>[] parameterTypes = obj != null ? Reflector.convertToParamsType(obj) : Reflector.convertToParamsType(w, h);
            Constructor<?> constructor = Reflector.getConstructor(
                    typeClass, Reflector.ReflectType.DEFAULT, parameterTypes);
            layoutParams = Reflector.newInstanceFromConstructor(
                    constructor, obj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return layoutParams;
    }
}
