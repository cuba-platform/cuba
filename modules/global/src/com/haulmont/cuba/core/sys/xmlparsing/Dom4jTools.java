/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.core.sys.xmlparsing;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.xml.parsers.SAXParser;
import java.io.*;
import java.util.Map;
import java.util.function.Function;

/**
 * Helper bean for XML parsing.
 * Caches SAXParser instances in the pool.
 * Pool size and timeout to borrow can be configured
 * with the application properties {@code cuba.dom4j.maxPoolSize} and {@code cuba.dom4j.maxBorrowWaitMillis}
 */
@Component(Dom4jTools.NAME)
public class Dom4jTools {

    public static final String NAME = "cuba_Dom4jTools";

    protected Dom4jToolsConfig config;

    protected GlobalConfig globalConfig;

    protected GenericObjectPool<SAXParser> pool;

    /**
     * INTERNAL
     */
    @Inject
    public Dom4jTools(GlobalConfig globalConfig, Dom4jToolsConfig config) {
        this.globalConfig = globalConfig;
        this.config = config;

        initPool();
    }

    protected void initPool() {
        int poolSize = config.getMaxPoolSize();
        GenericObjectPoolConfig<SAXParser> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(poolSize);
        poolConfig.setMaxTotal(poolSize);
        poolConfig.setMaxWaitMillis(config.getMaxBorrowWaitMillis());

        String jmxName = "dom4JTools-" + globalConfig.getWebContextName();
        poolConfig.setJmxNamePrefix(jmxName);

        PooledObjectFactory<SAXParser> factory = new SAXParserObjectFactory();
        pool = new GenericObjectPool<>(factory, poolConfig);
    }

    /**
     * Shuts down the pool, unregisters JMX.
     */
    public void shutdown() {
        if (pool != null) {
            pool.close();
            pool = null;
        }
    }

    public String writeDocument(Document doc, boolean prettyPrint) {
        return Dom4j.writeDocument(doc, prettyPrint);
    }

    public void writeDocument(Document doc, boolean prettyPrint, Writer writer) {
        Dom4j.writeDocument(doc, prettyPrint, writer);
    }

    public void writeDocument(Document doc, boolean prettyPrint, OutputStream stream) {
        Dom4j.writeDocument(doc, prettyPrint, stream);
    }

    public Document readDocument(File file) {
        return withSAXParserFromPool(saxParser -> Dom4j.readDocument(file, getSaxReader(saxParser)));
    }

    public Document readDocument(InputStream stream) {
        return withSAXParserFromPool(saxParser -> Dom4j.readDocument(stream, getSaxReader(saxParser)));
    }

    public Document readDocument(Reader reader) {
        return withSAXParserFromPool(saxParser -> Dom4j.readDocument(reader, getSaxReader(saxParser)));
    }

    public Document readDocument(String xmlString) {
        return withSAXParserFromPool(saxParser -> Dom4j.readDocument(xmlString, getSaxReader(saxParser)));
    }

    public void storeMap(Element parentElement, Map<String, String> map) {
        Dom4j.storeMap(parentElement, map);
    }

    public void loadMap(Element mapElement, Map<String, String> map) {
        Dom4j.loadMap(mapElement, map);
    }

    public void walkAttributesRecursive(Element element, Dom4j.ElementAttributeVisitor visitor) {
        Dom4j.walkAttributesRecursive(element, visitor);
    }

    public void walkAttributes(Element element, Dom4j.ElementAttributeVisitor visitor) {
        Dom4j.walkAttributes(element, visitor);
    }

    protected SAXReader getSaxReader(SAXParser saxParser) {
        try {
            return new SAXReader(saxParser.getXMLReader());
        } catch (SAXException e) {
            throw new RuntimeException("Unable to create SAX reader", e);
        }
    }

    protected  <T> T withSAXParserFromPool(Function<SAXParser, T> action) {
        SAXParser parser;
        try {
            parser = pool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to borrow SAXParser object from pool", e);
        }
        try {
            return action.apply(parser);
        } finally {
            pool.returnObject(parser);
        }
    }

}
