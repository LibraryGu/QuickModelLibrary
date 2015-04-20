package com.icrane.quickmode.utils.xml.attribute;

@SuppressWarnings("ALL")
public class Attribute {

	private String name;
	private String namespace;
	private String value;
	private String prefix;
	private String type;
	
	public Attribute(){}
	
	public Attribute(String name ,String value) {
		this.setName(name);
		this.setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Attribute [name=" + name + ", namespace=" + namespace
				+ ", value=" + value + ", prefix=" + prefix + ", type=" + type
				+ "]";
	}

}
