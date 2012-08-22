/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.02.2009 16:19:06
 *
 * $Id$
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.ConfigurationResourceLoader;
import com.haulmont.cuba.gui.NoSuchScreenException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GenericUI class holding information about all registered screens.
 */
@ManagedBean("cuba_WindowConfig")
public class WindowConfig
{
    public static final String WINDOW_CONFIG_XML_PROP = "cuba.windowConfig";

    protected Map<String, WindowInfo> screens = new HashMap<String, WindowInfo>();

    private static Log log = LogFactory.getLog(WindowConfig.class);

    private Scripting scripting;

    @Inject
    private Metadata metadata;

    public static final Pattern ENTITY_SCREEN_PATTERN = Pattern.compile("([_A-Za-z]+\\$[A-Z][_A-Za-z0-9]*)\\..+");

    @Inject
    public WindowConfig(Scripting scripting) {
        this.scripting = scripting;

        final String configName = AppContext.getProperty(WINDOW_CONFIG_XML_PROP);

        ConfigurationResourceLoader resourceLoader = new ConfigurationResourceLoader();
        StrTokenizer tokenizer = new StrTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resourceLoader.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    loadConfig(stream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            } else {
                log.warn("Resource " + location + " not found, ignore it");
            }
        }
    }

    public void loadConfig(Element rootElem) {
        for (Element element : (List<Element>) rootElem.elements("include")) {
            String fileName = element.attributeValue("file");
            if (!StringUtils.isBlank(fileName)) {
                String incXml = scripting.getResourceAsString(fileName);
                if (incXml == null) {
                    log.warn("File " + fileName + " not found, ignore it");
                    continue;
                }
                loadConfig(incXml);
            }
        }
        for (Element element : (List<Element>) rootElem.elements("screen")) {
            String id = element.attributeValue("id");
            if (StringUtils.isBlank(id)) {
                log.warn("Invalid window config: 'id' attribute not defined");
                continue;
            }
            WindowInfo windowInfo = new WindowInfo(id, element);
            screens.put(id, windowInfo);
        }
    }

    public void loadConfig(InputStream stream) {
        Document doc;
        try {
            SAXReader reader = new SAXReader();
            doc = reader.read(stream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        loadConfig(doc.getRootElement());
    }

    public void loadConfig(String xml) {
        Document doc;
        try {
            SAXReader reader = new SAXReader();
            doc = reader.read(new StringReader(xml));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        loadConfig(doc.getRootElement());
    }

    /**
     * Get screen information by screen ID.
     * Can be overridden for specific client type.
     * @param id screen ID as set up in <code>screen-config.xml</code>
     * @throws NoSuchScreenException if the screen with specified ID is not registered
     */
    public WindowInfo getWindowInfo(String id) {
        WindowInfo windowInfo = screens.get(id);
        if (windowInfo == null) {
            Matcher matcher = ENTITY_SCREEN_PATTERN.matcher(id);
            if (matcher.matches()) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(matcher.group(1));
                if (originalMetaClass != null) {
                    String originalId = new StringBuilder(id)
                            .replace(matcher.start(1), matcher.end(1), originalMetaClass.getName()).toString();
                    windowInfo = screens.get(originalId);
                }
            }
        }
        if (windowInfo == null)
            throw new NoSuchScreenException("Screen '" + id + "' is not defined");
        return windowInfo;
    }

    /**
     * All registered screens
     */
    public Collection<WindowInfo> getWindows() {
        return screens.values();
    }
}
