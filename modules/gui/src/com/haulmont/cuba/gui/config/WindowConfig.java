/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.components.Window;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;
import org.springframework.core.io.Resource;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GenericUI class holding information about all registered in <code>screens.xml</code> screens.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(WindowConfig.NAME)
public class WindowConfig {

    public static final String NAME = "cuba_WindowConfig";

    public static final String WINDOW_CONFIG_XML_PROP = "cuba.windowConfig";

    public static final Pattern ENTITY_SCREEN_PATTERN = Pattern.compile("([_A-Za-z]+\\$[A-Z][_A-Za-z0-9]*)\\..+");

    private static Logger log = LoggerFactory.getLogger(WindowConfig.class);

    protected Map<String, WindowInfo> screens = new HashMap<>();

    @Inject
    protected Resources resources;

    @Inject
    protected Metadata metadata;

    protected volatile boolean initialized;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected void checkInitialized() {
        if (!initialized) {
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if (!initialized) {
                    init();
                    initialized = true;
                }
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        }
    }

    protected void init() {
        screens.clear();

        String configName = AppContext.getProperty(WINDOW_CONFIG_XML_PROP);
        StrTokenizer tokenizer = new StrTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    loadConfig(Dom4j.readDocument(stream).getRootElement());
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

    @SuppressWarnings("unchecked")
    protected void loadConfig(Element rootElem) {
        for (Element element : (List<Element>) rootElem.elements("include")) {
            String fileName = element.attributeValue("file");
            if (!StringUtils.isBlank(fileName)) {
                String incXml = resources.getResourceAsString(fileName);
                if (incXml == null) {
                    log.warn("File " + fileName + " not found, ignore it");
                    continue;
                }
                loadConfig(Dom4j.readDocument(incXml).getRootElement());
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

    /**
     * Make the config to reload screens on next request.
     */
    public void reset() {
        initialized = false;
    }

    /**
     * Get screen information by screen ID.
     *
     * @param id screen ID as set up in <code>screens.xml</code>
     * @return screen's registration information or null if not found
     */
    @Nullable
    public WindowInfo findWindowInfo(String id) {
        lock.readLock().lock();
        try {
            checkInitialized();

            WindowInfo windowInfo = screens.get(id);
            if (windowInfo == null) {
                Matcher matcher = ENTITY_SCREEN_PATTERN.matcher(id);
                if (matcher.matches()) {
                    MetaClass metaClass = metadata.getClass(matcher.group(1));
                    if (metaClass == null)
                        return null;
                    MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                    if (originalMetaClass != null) {
                        String originalId = new StringBuilder(id)
                                .replace(matcher.start(1), matcher.end(1), originalMetaClass.getName()).toString();
                        windowInfo = screens.get(originalId);
                    }
                }
            }
            return windowInfo;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Get screen information by screen ID.
     *
     * @param id screen ID as set up in <code>screens.xml</code>
     * @return screen's registration information
     * @throws NoSuchScreenException if the screen with specified ID is not registered
     */
    public WindowInfo getWindowInfo(String id) {
        WindowInfo windowInfo = findWindowInfo(id);
        if (windowInfo == null) {
            throw new NoSuchScreenException(id);
        }
        return windowInfo;
    }

    /**
     * @return true if the configuration contains a screen with provided ID
     */
    public boolean hasWindow(String id) {
        return findWindowInfo(id) != null;
    }

    /**
     * All registered screens
     */
    public Collection<WindowInfo> getWindows() {
        lock.readLock().lock();
        try {
            checkInitialized();
            return screens.values();
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getMetaClassScreenId(MetaClass metaClass, String suffix) {
        MetaClass screenMetaClass = metaClass;
        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
        if (originalMetaClass != null) {
            screenMetaClass = originalMetaClass;
        }

        return screenMetaClass.getName() + suffix;
    }

    public String getBrowseScreenId(MetaClass metaClass) {
        return getMetaClassScreenId(metaClass, Window.BROWSE_WINDOW_SUFFIX);
    }

    public String getLookupScreenId(MetaClass metaClass) {
        return getMetaClassScreenId(metaClass, Window.LOOKUP_WINDOW_SUFFIX);
    }

    public String getEditorScreenId(MetaClass metaClass) {
        return getMetaClassScreenId(metaClass, Window.EDITOR_WINDOW_SUFFIX);
    }

    public String getAvailableLookupScreenId(MetaClass metaClass) {
        String id = getLookupScreenId(metaClass);
        if (!hasWindow(id)) {
            id = getBrowseScreenId(metaClass);
        }
        return id;
    }
}