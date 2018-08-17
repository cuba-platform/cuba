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
package com.haulmont.cuba.gui.config;

import com.google.common.base.Strings;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.gui.sys.ScreenDescriptorUtils;
import com.haulmont.cuba.gui.sys.ScreensConfiguration;
import com.haulmont.cuba.gui.sys.ScreensConfiguration.UiControllerDefinition;
import com.haulmont.cuba.gui.xml.layout.ScreenXmlLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GenericUI class holding information about all registered in <code>screens.xml</code> screens.
 */
@Component(WindowConfig.NAME)
public class WindowConfig {

    // todo Support FrameOwner here instead of Screen

    public static final String NAME = "cuba_WindowConfig";

    public static final String WINDOW_CONFIG_XML_PROP = "cuba.windowConfig";

    public static final Pattern ENTITY_SCREEN_PATTERN = Pattern.compile("([A-Za-z0-9]+[$_][A-Z][_A-Za-z0-9]*)\\..+");

    private final Logger log = LoggerFactory.getLogger(WindowConfig.class);

    protected Map<String, WindowInfo> screens = new HashMap<>();

    @Inject
    protected Resources resources;
    @Inject
    protected Scripting scripting;
    @Inject
    protected Metadata metadata;
    @Inject
    protected List<ScreensConfiguration> screensConfigurations;
    @Inject
    protected ScreenXmlLoader screenXmlLoader;

    protected volatile boolean initialized;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected WindowAttributesProvider windowAttributesProvider = new WindowAttributesProvider() {
        @Override
        public WindowInfo.Type getType(WindowInfo windowInfo) {
            return extractWindowInfoType(windowInfo);
        }

        @Nullable
        @Override
        public String getTemplate(WindowInfo windowInfo) {
            return extractWindowTemplate(windowInfo);
        }

        @Override
        public boolean isMultiOpen(WindowInfo windowInfo) {
            return extractMultiOpen(windowInfo);
        }

        @Nonnull
        @Override
        public Class<? extends Screen> getScreenClass(WindowInfo windowInfo) {
            return extractScreenClass(windowInfo);
        }
    };

    protected WindowInfo.Type extractWindowInfoType(WindowInfo windowInfo) {
        return WindowInfo.Type.SCREEN; // todo support fragment
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    protected Class<? extends Screen> extractScreenClass(WindowInfo windowInfo) {
        if (windowInfo.getDescriptor() != null) {
            String className = windowInfo.getDescriptor().attributeValue("class");

            if (Strings.isNullOrEmpty(className)) {
                Element screenXml = screenXmlLoader.load(windowInfo.getTemplate(),
                        windowInfo.getId(), Collections.emptyMap());
                className = screenXml.attributeValue("class");
            }

            if (Strings.isNullOrEmpty(className)) {
                throw new IllegalStateException("Window descriptor does not declare class attribute");
            }

            return (Class<? extends Screen>) scripting.loadClassNN(className);
        }

        if (windowInfo.getScreenClassName() != null) {
            return loadDefinedScreenClass(windowInfo.getScreenClassName());
        }

        throw new IllegalStateException("Neither screen class nor descriptor is set for WindowInfo");
    }

    protected boolean extractMultiOpen(WindowInfo windowInfo) {
        if (windowInfo.getDescriptor() != null) {
            return Boolean.parseBoolean(windowInfo.getDescriptor().attributeValue("multipleOpen"));
        }

        if (windowInfo.getScreenClassName() != null) {
            Class<? extends Screen> screenClass = loadDefinedScreenClass(windowInfo.getScreenClassName());

            UiController uiController = screenClass.getAnnotation(UiController.class);
            if (uiController == null) {
                // default is false
                return false;
            }
            return uiController.multipleOpen();
        }

        throw new IllegalStateException("Neither screen class nor descriptor is set for WindowInfo");
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected String extractWindowTemplate(WindowInfo windowInfo) {
        if (windowInfo.getDescriptor() != null) {
            return windowInfo.getDescriptor().attributeValue("template");
        }

        if (windowInfo.getScreenClassName() != null) {
            Class<? extends Screen> screenClass = loadDefinedScreenClass(windowInfo.getScreenClassName());

            UiDescriptor annotation = screenClass.getAnnotation(UiDescriptor.class);
            if (annotation == null) {
                return null;
            }
            String template = ScreenDescriptorUtils.getInferredDesignTemplate(annotation, screenClass);
            if (!template.startsWith("/")) {
                String packageName = screenClass.getPackage().getName();
                if (StringUtils.isNotEmpty(packageName)) {
                    String relativePath = packageName.replace('.', '/');
                    template = "/" + relativePath + "/" + template;
                }
            }

            return template;
        }

        throw new IllegalStateException("Neither screen class nor descriptor is set for WindowInfo");
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends Screen> loadDefinedScreenClass(String className) {
        return (Class<? extends Screen>) scripting.loadClassNN(className);
    }

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
        long startTime = System.currentTimeMillis();

        screens.clear();

        loadScreenConfigurations();
        loadScreensXml();

        log.info("WindowConfig initialized in {} ms", System.currentTimeMillis() - startTime);
    }

    protected void loadScreenConfigurations() {
        for (ScreensConfiguration provider : screensConfigurations) {
            List<UiControllerDefinition> uiControllers = provider.getUIControllers();

            for (UiControllerDefinition definition : uiControllers) {
                WindowInfo windowInfo = new WindowInfo(definition.getId(), windowAttributesProvider,
                        definition.getControllerClass());
                screens.put(definition.getId(), windowInfo);
            }
        }
    }

    protected void loadScreensXml() {
        String configName = AppContext.getProperty(WINDOW_CONFIG_XML_PROP);
        StringTokenizer tokenizer = new StringTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    loadConfig(Dom4j.readDocument(stream).getRootElement());
                } catch (IOException e) {
                    throw new RuntimeException("Unable to read window config from " + location, e);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            } else {
                log.warn("Resource {} not found, ignore it", location);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void loadConfig(Element rootElem) {
        for (Element element : rootElem.elements("include")) {
            String fileName = element.attributeValue("file");
            if (!StringUtils.isBlank(fileName)) {
                String incXml = resources.getResourceAsString(fileName);
                if (incXml == null) {
                    log.warn("File {} not found, ignore it", fileName);
                    continue;
                }
                loadConfig(Dom4j.readDocument(incXml).getRootElement());
            }
        }
        for (Element element : rootElem.elements("screen")) {
            String id = element.attributeValue("id");
            if (StringUtils.isBlank(id)) {
                log.warn("Invalid window config: 'id' attribute not defined");
                continue;
            }
            WindowInfo windowInfo = new WindowInfo(id, windowAttributesProvider, element);
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
     * @param id         screen ID as set up in <code>screens.xml</code>
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
     * @param id         screen ID as set up in <code>screens.xml</code>
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

    public WindowInfo getEditorScreen(Entity entity) {
        MetaClass metaClass = entity.getMetaClass();
        String editorScreenId = getEditorScreenId(metaClass);
        return getWindowInfo(editorScreenId);
    }

    /**
     * Get available lookup screen by class of entity
     *
     * @param entityClass entity class
     * @return id of lookup screen
     * @throws NoSuchScreenException if the screen with specified ID is not registered
     */
    public WindowInfo getLookupScreen(Class<? extends Entity> entityClass) {
        MetaClass metaClass = metadata.getSession().getClass(entityClass);
        String lookupScreenId = getAvailableLookupScreenId(metaClass);
        return getWindowInfo(lookupScreenId);
    }

    public String getAvailableLookupScreenId(MetaClass metaClass) {
        String id = getLookupScreenId(metaClass);
        if (!hasWindow(id)) {
            id = getBrowseScreenId(metaClass);
        }
        return id;
    }
}