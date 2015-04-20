package com.icrane.quickmode.utils.xml;

import com.icrane.quickmode.utils.xml.attribute.XmlPullAttributes;

/**
 * 里面都是空方法,是pull解析监听器,可选择重写方法,实现想要的
 */
@SuppressWarnings("ALL")
public abstract class SimpleXmlPullParseListener implements
		OnXmlPullParseListener {

	@Override
	public void startPullDocument() {
		// TODO Auto-generated method stub
	}

	@Override
	public void endPullDocument() {
		// TODO Auto-generated method stub
	}

	@Override
	public void startElement(String tagName, XmlPullAttributes attrs) {
		// TODO Auto-generated method stub
	}

	@Override
	public void character(String text) {
		// TODO Auto-generated method stub
	}

	@Override
	public void endElement(String tagName) {
		// TODO Auto-generated method stub
	}

}
