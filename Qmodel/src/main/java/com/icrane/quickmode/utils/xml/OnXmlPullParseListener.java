package com.icrane.quickmode.utils.xml;

import com.icrane.quickmode.utils.xml.attribute.XmlPullAttributes;

public interface OnXmlPullParseListener {
	public void startPullDocument();
	public void endPullDocument();
	public void startElement(String tagName, XmlPullAttributes attrs);
	public void character(String text);
	public void endElement(String tagName);
}
