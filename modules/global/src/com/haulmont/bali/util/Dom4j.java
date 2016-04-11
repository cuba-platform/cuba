/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.bali.util;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.List;
import java.util.Map;

public final class Dom4j {

    private static final ThreadLocal<SAXParser> saxParserHolder = new ThreadLocal<>();

    private Dom4j() {
    }

    public static Document readDocument(String xmlString) {
        return readDocument(new StringReader(xmlString));
    }

    public static Document readDocument(Reader reader) {
        SAXReader xmlReader = getSaxReader();
        try {
            return xmlReader.read(reader);
        } catch (DocumentException e) {
            throw new RuntimeException("Unable to read XML from reader", e);
        }
    }

    private static SAXReader getSaxReader() {
        String useThreadLocalCache = AppContext.getProperty("cuba.saxParser.threadLocalCache");
        if (useThreadLocalCache == null || Boolean.parseBoolean(useThreadLocalCache)) {
            try {
                return new SAXReader(getParser().getXMLReader());
            } catch (SAXException e) {
                throw new RuntimeException("Unable to create SAX reader", e);
            }
        } else {
            return new SAXReader();
        }
    }

    public static SAXParser getParser() {
        SAXParser parser = saxParserHolder.get();
        if (parser == null) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            try {
                parser = factory.newSAXParser();
            } catch (ParserConfigurationException | SAXException e) {
                throw new RuntimeException("Unable to create SAX parser", e);
            }
            saxParserHolder.set(parser);
        }
        return parser;
    }

    public static Document readDocument(InputStream stream) {
        SAXReader xmlReader = getSaxReader();
        try {
            return xmlReader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException("Unable to read XML from stream", e);
        }
    }

    public static Document readDocument(File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return Dom4j.readDocument(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to read XML from file", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
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
            throw new RuntimeException("Unable to write XML", e);
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
            throw new RuntimeException("Unable to write XML", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Element> elements(Element element) {
        return element.elements();
    }

    @SuppressWarnings("unchecked")
    public static List<Element> elements(Element element, String name) {
        return element.elements(name);
    }

    @SuppressWarnings("unchecked")
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
        Preconditions.checkNotNullArgument(map, "map is null");

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

    public static void walkAttributesRecursive(Element element, ElementAttributeVisitor visitor) {
        walkAttributes(element, visitor);

        for (Element childElement : elements(element)) {
            walkAttributesRecursive(childElement, visitor);
        }
    }

    public static void walkAttributes(Element element, ElementAttributeVisitor visitor) {
        for (Attribute attribute : attributes(element)) {
            visitor.onVisit(element, attribute);
        }
    }

    public interface ElementAttributeVisitor {
        void onVisit(Element element, Attribute attribute);
    }
}