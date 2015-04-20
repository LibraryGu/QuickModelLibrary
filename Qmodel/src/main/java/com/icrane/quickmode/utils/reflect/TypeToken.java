package com.icrane.quickmode.utils.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类型创建者
 * 
 * @author Administrator
 * 
 * @param <T>
 */
@SuppressWarnings("ALL")
public final class TypeToken<T> {

	private Type type;

	public TypeToken() {
		type = getSuperclassParameterType(getClass());
	}

	/**
	 * 获取父类的(超类的)参数化泛型类型;
	 * 
	 * @param subClass
	 *            类类型
	 * @return 返回父类的(超类的)参数化泛型类型;
	 */
	public Class<?> getSuperclassParameterType(Class<?> subClass) {
		Type superType = subClass.getGenericSuperclass();
		if (superType instanceof Class) {
			throw new IllegalArgumentException("Missing super class!");
		}
		ParameterizedType pt = (ParameterizedType) superType;
		Type[] parameterTypes = pt.getActualTypeArguments();
		return (Class<?>) parameterTypes[0];
	}

	public Type getType() {
		return type;
	}

}