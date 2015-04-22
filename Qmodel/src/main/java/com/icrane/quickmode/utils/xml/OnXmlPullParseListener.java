package com.icrane.quickmode.utils.xml;

import com.icrane.quickmode.utils.xml.attribute.XmlPullAttributes;

public interface OnXmlPullParseListener {

    /**
     * 从xml文档开头开始读取
     */
    public void startPullDocument();

    /**
     * 读到xml文档结尾时调用
     */
    public void endPullDocument();

    /**
     * 开始读取xml标签
     *
     * @param tagName 标签名
     * @param attrs   xml属性
     */
    public void startElement(String tagName, XmlPullAttributes attrs);

    /**
     * 读取内容
     *
     * @param text 内容文本
     */
    public void character(String text);

    /**
     * 读取到标签结尾
     *
     * @param tagName 标签名
     */
    public void endElement(String tagName);
}
