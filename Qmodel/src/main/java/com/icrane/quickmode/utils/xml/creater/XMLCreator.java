package com.icrane.quickmode.utils.xml.creater;

import android.util.Xml;

import com.icrane.quickmode.utils.Charset;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.common.Streams;
import com.icrane.quickmode.utils.xml.attribute.Attribute;
import com.icrane.quickmode.utils.xml.attribute.Element;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

@SuppressWarnings("ALL")
public class XMLCreator {

	private XmlSerializer serializer = null;

    private XMLCreator() {
        serializer = Xml.newSerializer();
    }

    public static final XMLCreator create() {
        return new XMLCreator();
    }

    /**
     * 写入xml
     * @param rootElement
     * @param writer
     */
	public void writeXML(Element rootElement, Writer writer) {

		try {
			serializer.setOutput(writer);
			serializer.startDocument(Charset.UTF_8.obtain(), true);
			writeElement(rootElement);
			serializer.endDocument();
			writer.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
                serializer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

	}

    /**
     * 写入xml
     * @param rootElement
     * @param outputStream
     * @param charset
     */
	public void writeXML(Element rootElement, OutputStream outputStream,
			Charset charset) {
		writeXML(rootElement, Streams.obtainBufferedWriter(outputStream));
	}

    /**
     * 写入标签
     * @param element
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws java.io.IOException
     */
	protected void writeElement(Element element) throws IllegalArgumentException,
			IllegalStateException, IOException {
		
		String namespace = element.getNamespace();
		String elementName = element.getElementName();
		Attribute[] attributes = element.getAttributes();
		
		serializer.startTag(namespace, elementName);
		for (Attribute attr : attributes) {
			serializer.attribute(namespace, attr.getName(), attr.getValue());
		}

		String text = element.getText();
		String cd_sect = element.getCdsect();
		String comment = element.getComment();
		
		if (!CommonUtils.isEmpty(comment))
			serializer.comment(comment);
		if (!CommonUtils.isEmpty(cd_sect))
			serializer.cdsect(cd_sect);
		if (!CommonUtils.isEmpty(text))
			serializer.text(text);
		if (element.isIncludeOther()) {
			Element[] elements = element.getIncludeElements();
			for (Element ele : elements) {
				writeElement(ele);
			}
		}
		serializer.endTag(namespace, elementName);
	}

}
