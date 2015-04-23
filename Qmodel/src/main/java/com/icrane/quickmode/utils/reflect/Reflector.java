package com.icrane.quickmode.utils.reflect;

import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.common.LogUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class Reflector {

    /**
     * 获取指定类型的域实例；
     *
     * @param cls         指定Class类型
     * @param fieldName   域名称
     * @param reflectType 指定获取域的类型(DEFAULT or DECLARED)
     * @return 域实例
     * @throws NoSuchFieldException {@link java.lang.NoSuchFieldException}
     */
    public static Field getField(Class<?> cls, String fieldName, ReflectType reflectType) throws NoSuchFieldException {
        Field field = isDefaultType(reflectType) ? cls.getField(fieldName) : cls
                .getDeclaredField(fieldName);
        field.setAccessible(Modifier.isPrivate(getModifier(field.getClass())));
        return field;
    }

    /**
     * 获取指定类型的方法实例；
     *
     * @param cls            指定Class类型
     * @param methodName     方法名称
     * @param reflectType    指定获取方法的类型(DEFAULT or DECLARED)
     * @param parameterTypes 方法参数类型，可为null，当为null时，即获取无参数的方法
     * @return 方法实例
     * @throws java.lang.NoSuchMethodException {@link java.lang.NoSuchMethodException}
     */

    public static Method getMethod(Class<?> cls, String methodName, ReflectType reflectType, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = isDefaultType(reflectType) ? cls.getMethod(methodName,
                parameterTypes) : cls.getDeclaredMethod(methodName,
                parameterTypes);
        method.setAccessible(Modifier.isPrivate(getModifier(method.getClass())));
        return method;
    }

    /**
     * 获取指定类型的构造方法
     *
     * @param cls            指定Class类型
     * @param reflectType    指定获取构造方法的类型(DEFAULT or DECLARED)
     * @param parameterTypes 构造方法参数类型，可为null，当为null时，即获取无参数的构造方法
     * @return 构造方法实例
     * @throws NoSuchMethodException {@link java.lang.NoSuchMethodException}
     */
    public static Constructor<?> getConstructor(Class<?> cls, ReflectType reflectType, Class<?>... parameterTypes) throws NoSuchMethodException {
        Constructor<?> constructor = isDefaultType(reflectType) ? cls
                .getConstructor(parameterTypes) : cls
                .getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(Modifier.isPrivate(getModifier(constructor
                .getClass())));
        return constructor;
    }

    /**
     * 获取修饰符
     *
     * @param cls 类类型
     * @return 返回这修饰符
     */
    public static int getModifier(Class<?> cls) {
        int modifier = cls.getModifiers();
        return modifier;
    }

    /**
     * 获取全部域
     *
     * @param cls         类类型
     * @param reflectType 反射类型
     * @return 返回域数组
     */
    public static Field[] getFields(Class<?> cls, ReflectType reflectType) {
        Field[] fields = isDefaultType(reflectType) ? cls.getFields() : cls
                .getDeclaredFields();
        return fields;
    }

    /**
     * 获取全部方法
     *
     * @param cls         类类型
     * @param reflectType 反射类型
     * @return 返回方法数组
     */
    public static Method[] getMethods(Class<?> cls, ReflectType reflectType) {
        Method[] methods = isDefaultType(reflectType) ? cls.getMethods() : cls
                .getDeclaredMethods();
        return methods;
    }

    /**
     * 获取全部构造方法
     *
     * @param cls         类类型
     * @param reflectType 反射类型
     * @return 返回构造方法数组
     */
    public static Constructor<?>[] getConstructors(Class<?> cls, ReflectType reflectType) {
        Constructor<?>[] constructors = isDefaultType(reflectType) ? cls
                .getConstructors() : cls.getDeclaredConstructors();
        return constructors;

    }

    /**
     * 调用构造函数
     *
     * @param type           要实例化的类型
     * @param reflectType    反射类型
     * @param parameterTypes 参数类型
     * @param args           参数
     * @param <T>            泛型
     * @return 返回实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstanceFromConstructor(Type type, ReflectType reflectType, Class<?>[] parameterTypes, Object[] args) {
        T object = null;
        Class<?> typeClass = (Class<?>) type;
        try {
            Constructor<?> constructor = getConstructor(typeClass, reflectType,
                    parameterTypes);
            object = (T) constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 通过调用构造函数创建一个新实例
     *
     * @param constructor 构造函数
     * @param args        参数
     * @param <T>         泛型
     * @return 新实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstanceFromConstructor(Constructor<?> constructor, Object... args) {
        T object = null;
        try {
            object = (T) constructor.newInstance(args);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 默认调用构造一个新对象
     *
     * @param cls 类类型
     * @param <T> 泛型
     * @return 新实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> cls) {
        T object = null;
        try {
            object = (T) cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 判断当前反射类型是否是DEFAULT类型
     *
     * @param reflectType 类型
     * @return 如果为true，则为DEFAULT类型，反之亦然；
     */
    public static boolean isDefaultType(ReflectType reflectType) {
        return (reflectType == ReflectType.DEFAULT);
    }

    /**
     * 判断当前反射类型是否是DECLARED类型
     *
     * @param reflectType 类型
     * @return 如果为true，则为DECALRED类型，反之亦然；
     */
    public static boolean isDeclaredType(ReflectType reflectType) {
        return (reflectType == ReflectType.DECLARED);
    }

    /**
     * 调用方法
     *
     * @param obj    实例对象
     * @param method 方法实例
     * @param args   方法参数
     * @return Object
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用方法
     *
     * @param obj            对象
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @param reflectType    反射类型
     * @param args           参数
     * @return Object
     */
    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, ReflectType reflectType, Object... args) {
        Method method = null;
        try {
            method = getMethod(obj.getClass(), methodName, reflectType,
                    parameterTypes);
            return invokeMethod(obj, method, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 寻找合适的方法
     *
     * @param cls             类类型
     * @param methodName      方法名
     * @param reflectType     反射类型
     * @param _parameterTypes 参数类型
     * @return 反射Method
     * @throws NoSuchMethodException {@link java.lang.NoSuchMethodException}
     */
    public static Method findMethod(Class<?> cls, String methodName, ReflectType reflectType, Class<?>... _parameterTypes) throws NoSuchMethodException {
        // 获取所有此Class的所有方法
        Method[] methods = getMethods(cls, reflectType);
        for (Method method : methods) {
            // 获取方法名称
            String _methodName = method.getName();
            // 获取方法参数数组
            Class<?>[] parameterTypes = method.getParameterTypes(); // 判断方法名称是否相等
            if (_methodName.equals(methodName)) { // 传入参数类型长度
                int mParameterTypesLength = _parameterTypes.length; // 当前构造函数参数类型长度
                int parameterTypesLength = parameterTypes.length;
                boolean isExist = false;
                if (mParameterTypesLength == parameterTypesLength) {

                    for (int i = 0; i < mParameterTypesLength; i++) {
                        Class<?> mParameterType = _parameterTypes[i];
                        Class<?> parameter = parameterTypes[i];
                        LogUtils.d(mParameterType.getName() + ","
                                + parameter.getName());
                        isExist = isAssignableFrom(parameter, mParameterType);
                    }
                    if (isExist) {
                        return method;
                    }
                }
            }
        }
        throw new NoSuchMethodException();
    }

    /**
     * 判断是否是从这个超类继承而来
     *
     * @param superClass 父类类型
     * @param subClass   子类类型
     * @return true表示是从此superClass继承而来，反之false
     */
    public static boolean isAssignableFrom(Class<?> superClass, Class<?> subClass) {
        return superClass.isAssignableFrom(subClass);
    }

    /**
     * 根据对象获取类型
     *
     * @param args 对象数组
     * @return Class[]数组
     */
    public static Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return parameterTypes;
    }

    /**
     * 判断是否是Class类型对象
     *
     * @param obj 需要判断的对象
     * @return true表示是Class类型，反之返回false
     */
    public static boolean isClass(Object obj) {
        return obj instanceof Class;
    }

    /**
     * 判断基础类型
     *
     * @param data Object对象
     * @return true表示是基本数据类型，反之返回false
     */
    public static boolean isBasicTypes(Object data) {
        if (data instanceof Integer) {
            return true;
        } else if (data instanceof Short) {
            return true;
        } else if (data instanceof Long) {
            return true;
        } else if (data instanceof Double) {
            return true;
        } else if (data instanceof Character) {
            return true;
        } else if (data instanceof Float) {
            return true;
        } else if (data instanceof String) {
            return true;
        } else if (data instanceof Boolean) {
            return true;
        }
        return false;
    }

    /**
     * 转换参数为参数数组
     *
     * @param args 参数数组
     * @return 参数数组中对象类型数组
     */
    public static Class<?>[] convertToParamsType(Object... args) {
        // 创建参数类型数组
        Class<?>[] parameterTypes = new Class<?>[args.length];
        // 获取参数的类型并添加到参数类型数组中
        for (int i = 0; i < parameterTypes.length; i++) {
            Object argument = args[i];
            parameterTypes[i] = convertToBasicTypes(argument);
        }
        return parameterTypes;
    }

    /**
     * 转换参数为参数数组
     *
     * @param args 参数数组
     * @return 参数数组中对象类型数组
     */
    public static Class<?>[] convertToParamsSuperType(Object... args) {
        // 创建参数类型数组
        Class<?>[] parameterTypes = new Class<?>[args.length];
        // 获取参数的类型并添加到参数类型数组中
        for (int i = 0; i < parameterTypes.length; i++) {
            Object argument = args[i];
            parameterTypes[i] = convertToSuperClass(argument);
        }
        return parameterTypes;
    }

    /**
     * 转换域数组为Map
     *
     * @param fields 属性数组
     * @return 属性集合
     */
    public static Map<String, Field> convertFieldsToMap(Field[] fields) {
        Map<String, Field> fieldsMap = new HashMap<String, Field>();
        for (Field field : fields) {
            String name = field.getName();
            fieldsMap.put(name, field);
        }
        return fieldsMap;
    }

    /**
     * 转换基础类型
     *
     * @param data 需要转换的对象
     * @return Class对象
     */
    public static Class<?> convertToBasicTypes(Object data) {
        if (data instanceof Integer) {
            return int.class;
        } else if (data instanceof Short) {
            return short.class;
        } else if (data instanceof Long) {
            return long.class;
        } else if (data instanceof Double) {
            return double.class;
        } else if (data instanceof Character) {
            return char.class;
        } else if (data instanceof Float) {
            return float.class;
        } else if (data instanceof Boolean) {
            return boolean.class;
        } else if (isClass(data)) {
            return (Class<?>) data;
        }
        return data.getClass();
    }

    /**
     * 转换为父类类型
     *
     * @param data 需要转换的对象
     * @return Class对象
     */
    public static Class<?> convertToSuperClass(Object data) {
        Class<?> superClass = data.getClass().getSuperclass();
        if (isBasicTypes(data) || CommonUtils.isEmpty(superClass))
            return convertToBasicTypes(data);
        return superClass;
    }

    /**
     * 反射的类型枚举
     */
    public enum ReflectType {
        DEFAULT, DECLARED;
    }

}
