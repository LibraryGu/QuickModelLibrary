package com.icrane.quickmode.utils.reflect;

import com.icrane.quickmode.utils.common.CommonUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public final class ObjectReflector {

    private Object object;
    private Type objectType;
    private Reflector.ReflectType reflectType = Reflector.ReflectType.DEFAULT;
    private Class<?> objectClass;

    public ObjectReflector() {
        this(Object.class);
    }

    /**
     * 构造函数
     *
     * @param type 使用new TypeToken(){}.getType()获取
     */
    public ObjectReflector(Type type) {
        this.setReflectObjectType(type);
    }

    /**
     * 创建实例对象
     *
     * @param extendsClass 继承此类型
     * @param args         构造参数
     */
    public void createInstance(Class<?> extendsClass, Object... args) {

        // 设置对象类型Class对象
        this.setObjectClass((Class<?>) this.objectType);
        // 判断将要实例化的对象衍生至何类型
        if (Reflector.isAssignableFrom(extendsClass, this.objectClass)) {
            try {
                // 获取参数类型数组
                Class<?>[] parameterTypes = Reflector
                        .convertToParamsType(args);
                // 获取对象构造函数
                Constructor<?> constructor = Reflector.getConstructor(
                        this.objectClass, reflectType, parameterTypes);
                // 实例化对象
                this.object = Reflector.newInstanceFromConstructor(
                        constructor, args);
                // 判断对象是否为null,如果为true，则反射实例化对象失败
                if (CommonUtils.isEmpty(this.object)) {
                    throw new NullPointerException(
                            "create the object execute 'new' operation is failed and it is null,should be ensure type of the object!");
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            // 不符合设置的基类衍生而来，则会抛出异常
            throw new ClassCastException(this.getReflectObjectClass().getName()
                    + " cannot be cast to " + Object.class.getName());
        }
    }

    /**
     * 调用相应方法
     *
     * @param methodName 方法名
     * @param args       方法参数
     */
    public void invokeObjectMethod(String methodName, Object... args) {
        try {
            // 获取参数类型数组
            Class<?>[] parameterTypes = Reflector
                    .convertToParamsType(args);
            Method method = Reflector.getMethod(
                    this.getReflectObjectClass(), methodName, reflectType,
                    parameterTypes);
            Reflector
                    .invokeMethod(this.getReflectObject(), method, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取反射类型
     *
     * @return 反射类型
     */
    public Reflector.ReflectType getReflectType() {
        return this.reflectType;
    }

    /**
     * 设置反射类型
     *
     * @param reflectType 反射类型
     */
    public void setReflectType(Reflector.ReflectType reflectType) {
        this.reflectType = reflectType;
    }

    /**
     * 获取反射对象
     *
     * @return 反射对象
     */
    public Object getReflectObject() {
        return object;
    }

    /**
     * 获取反射对象的类型
     *
     * @return 反射对象的类型
     */
    public Type getReflectObjectType() {
        return objectType;
    }

    /**
     * 设置反射对象的类型
     *
     * @param objectType 反射对象的类型
     */
    public void setReflectObjectType(Type objectType) {
        this.objectType = objectType;
    }

    /**
     * 获取反射对象的Class类型
     *
     * @return 反射对象的Class类型
     */
    public Class<?> getReflectObjectClass() {
        return objectClass;
    }

    /**
     * 设置反射对象的Class类型
     *
     * @param objectClass 反射对象的Class类型
     */
    public void setObjectClass(Class<?> objectClass) {
        this.objectClass = objectClass;
    }
}
