package com.icrane.quickmode.utils.xml.parser;

import android.util.Xml;

import com.icrane.quickmode.utils.xml.OnXmlPullParseListener;
import com.icrane.quickmode.utils.xml.attribute.Attribute;
import com.icrane.quickmode.utils.xml.attribute.XmlPullAttributes;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLParsers {

    private String input;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    /**
     * 使用sax解析xml
     *
     * @param handler Handler对象
     */
    public void useSAXParse(ContentHandler handler) {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader reader = saxParser.getXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(input)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void useSAXParse(String input, ContentHandler handler) {
        this.setInput(input);
        this.useSAXParse(handler);
    }

    /**
     * 使用Pull解析xml
     *
     * @param listener xml解析监听
     */
    public void usePullParse(OnXmlPullParseListener listener) {

        if (listener == null) {
            throw new NullPointerException(
                    "Cause By: 'OnXmlPullParseListener' is not set!");
        } else {
            XmlPullParser pullParser = Xml.newPullParser();
            try {
                pullParser.setInput(new StringReader(input));
                int eventType = pullParser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = null;
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            listener.startPullDocument();
                            break;
                        case XmlPullParser.START_TAG:

                            tagName = pullParser.getName();
                            int attrCount = pullParser.getAttributeCount();
                            XmlPullAttributes attributes = new XmlPullAttributes();
                            for (int i = 0; i < attrCount; i++) {
                                Attribute attr = new Attribute();
                                attr.setName(pullParser.getAttributeName(i));
                                attr.setNamespace(pullParser
                                        .getAttributeNamespace(i));
                                attr.setPrefix(pullParser.getAttributePrefix(i));
                                attr.setType(pullParser.getAttributeType(i));
                                attr.setValue(pullParser.getAttributeValue(i));
                                attributes.putAttribute(attr);
                            }
                            listener.startElement(tagName, attributes);

                            break;
                        case XmlPullParser.TEXT:
                            listener.character(pullParser.getText());
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = pullParser.getName();
                            listener.endElement(tagName);
                            break;
                    }
                    eventType = pullParser.next();
                }
                listener.endPullDocument();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void usePullParse(String input, OnXmlPullParseListener listener) {
        this.setInput(input);
        this.usePullParse(listener);
    }

}
