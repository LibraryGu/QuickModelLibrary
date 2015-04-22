package com.icrane.quickmode.utils.xml.attribute;

import com.icrane.quickmode.utils.common.CommonUtils;

public class Element {
	// 标签名
	private String elementName;
	// 命名空间
	private String namespace;
	// 包含值
	private String text;
	// 包含的CDATA值
	private String cdsect;
	// 注释
	private String comment;
	// 标签属性
	private Attribute[] attributes;
	// 包含的标签
	private Element[] includeElements;
	// 判断是否有包含其他子标签
	private boolean isIncludeOther;

	public Element() {
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCdsect() {
		return cdsect;
	}

	public void setCdsect(String cdsect) {
		this.cdsect = cdsect;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Attribute[] getAttributes() {
		return attributes;
	}

	public void setAttributes(Attribute...attribute) {
		this.attributes = attribute;
	}

	public Element[] getIncludeElements() {
		return includeElements;
	}

	public void include(Element... element) {
		this.includeElements = element;
	}

	public boolean isIncludeOther() {
		Element[] elements = getIncludeElements();
		this.isIncludeOther = (!CommonUtils.isEmpty(elements) && elements.length > 0);
		return this.isIncludeOther;
	}

}
