package com.icrane.quickmode.utils.xml.attribute;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class XmlPullAttributes {
	
	private List<Attribute> attributes = new ArrayList<Attribute>();

	/**
	 * 添加属性
	 * @param attr
	 */
	public void putAttribute(Attribute attr) {
		attributes.add(attr);
	}
	
	/**
	 * 获取属性
	 * @param index
	 * @return
	 */
	public Attribute getAttribute(int index) {
		return attributes.get(index);
	}
	
	/**
	 * 获取所有属性
	 * @return
	 */
	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	/**
	 * 获取当前属性的数量
	 * @return
	 */
	public int getAttributeCount() {
		return attributes.size();
	}
	
}
