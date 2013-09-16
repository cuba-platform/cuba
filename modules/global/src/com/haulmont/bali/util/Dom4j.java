/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.bali.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Dom4j {

    public static Document readDocument(String xmlString) {
        return readDocument(new StringReader(xmlString));
    }

    public static Document readDocument(Reader reader) {
        SAXReader xmlReader = new SAXReader();
        try {
            return xmlReader.read(reader);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document readDocument(InputStream stream) {
        SAXReader xmlReader = new SAXReader();
        try {
            return xmlReader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document readDocument(File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return Dom4j.readDocument(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //
                }
        }
    }

    public static String writeDocument(Document doc, boolean prettyPrint) {
        StringWriter writer = new StringWriter();
        writeDocument(doc, prettyPrint, writer);
        return writer.toString();
    }

    public static void writeDocument(Document doc, boolean prettyPrint, Writer writer) {
        XMLWriter xmlWriter;
        try {
            if (prettyPrint) {
                OutputFormat format = OutputFormat.createPrettyPrint();
                xmlWriter = new XMLWriter(writer, format);
            } else {
                xmlWriter = new XMLWriter(writer);
            }
            xmlWriter.write(doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeDocument(Document doc, boolean prettyPrint, OutputStream stream) {
        XMLWriter xmlWriter;
        try {
            if (prettyPrint) {
                OutputFormat format = OutputFormat.createPrettyPrint();
                xmlWriter = new XMLWriter(stream, format);
            } else {
                xmlWriter = new XMLWriter(stream);
            }
            xmlWriter.write(doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Element> elements(Element element) {
        return element.elements();
    }

    public static List<Element> elements(Element element, String name) {
        return element.elements(name);
    }

    public static List<Attribute> attributes(Element element) {
        return element.attributes();
    }

    public static void storeMap(Element parentElement, Map<String, String> map) {
        if (map == null)
            return;
        
        Element mapElem = parentElement.addElement("map");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Element entryElem = mapElem.addElement("entry");
            entryElem.addAttribute("key", entry.getKey());
            Element valueElem = entryElem.addElement("value");
            if (entry.getValue() != null) {
                String value = StringEscapeUtils.escapeXml(entry.getValue());
                valueElem.setText(value);
            }
        }
    }

    public static void loadMap(Element mapElement, Map<String, String> map) {
        if (map == null)
            throw new IllegalArgumentException("map is null");
        
        for (Element entryElem : elements(mapElement, "entry")) {
            String key = entryElem.attributeValue("key");
            if (key == null)
                throw new IllegalStateException("No 'key' attribute");

            String value = null;
            Element valueElem = entryElem.element("value");
            if (valueElem != null)
                value = StringEscapeUtils.unescapeXml(valueElem.getText());

            map.put(key, value);
        }
    }
}
