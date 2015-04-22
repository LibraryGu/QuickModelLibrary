package com.icrane.quickmode.utils.xml.attribute;

import java.util.ArrayList;
import java.util.List;

public class XmlPullAttributes {

    private List<Attribute> attributes = new ArrayList<Attribute>();

    /**
     * 添加属性
     *
     * @param attr 属性
     */
    public void putAttribute(Attribute attr) {
        attributes.add(attr);
    }

    /**
     * 获取属性
     *
     * @param index 下标
     * @return 属性
     */
    public Attribute getAttribute(int index) {
        return attributes.get(index);
    }

    /**
     * 获取所有属性
     *
     * @return 所有属性
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * 获取当前属性的数量
     *
     * @return 当前属性的数量
     */
    public int getAttributeCount() {
        return attributes.size();
    }

}
