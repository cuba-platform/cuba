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

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.components.Window;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * GenericUI class holding information about all registered in <code>screens.xml</code> screens.
 */
@Component(WindowConfig.NAME)
public class WindowConfig {

    public static final String NAME = "cuba_WindowConfig";

    public static final String WINDOW_CONFIG_XML_PROP = "cuba.windowConfig";

    public static final Pattern ENTITY_SCREEN_PATTERN = Pattern.compile("([_A-Za-z]+\\$[A-Z][_A-Za-z0-9]*)\\..+");

    private final Logger log = LoggerFactory.getLogger(WindowConfig.class);

    protected Map<String, List<WindowInfo>> screens = new HashMap<>();

    @Inject
    protected Resources resources;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DeviceInfoProvider deviceInfoProvider;

    // Map alias -> ScreenAgent
    protected Map<String, ScreenAgent> activeScreenAgents;

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

        Map<String, ScreenAgent> agentMap = AppBeans.getAll(ScreenAgent.class);

        Map<String, ScreenAgent> screenAgents = new HashMap<>();
        for (ScreenAgent screenAgent : agentMap.values()) {
            screenAgents.put(screenAgent.getAlias(), screenAgent);
        }
        this.activeScreenAgents = screenAgents;

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
                log.warn("Resource {} not found, ignore it", location);
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
                    log.warn("File {} not found, ignore it", fileName);
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

            ScreenAgent targetAgent = null;
            String agent = element.attributeValue("agent");
            if (StringUtils.isNotEmpty(agent)) {
                targetAgent = activeScreenAgents.get(agent);

                if (targetAgent == null) {
                    throw new DevelopmentException("Unable to find target screen agent", "agent", agent);
                }
            }

            WindowInfo windowInfo = new WindowInfo(id, element, targetAgent);

            List<WindowInfo> screenInfos = screens.get(id);
            if (screenInfos == null) {
                screenInfos = new ArrayList<>();
                screens.put(id, screenInfos);
            } else {
                WindowInfo existingScreen = screenInfos.stream()
                        .filter(existingWindowInfo ->
                                existingWindowInfo.getScreenAgent() == windowInfo.getScreenAgent())
                        .findFirst()
                        .orElse(null);

                if (existingScreen != null) {
                    screenInfos.remove(existingScreen);
                }
            }

            screenInfos.add(windowInfo);
        }
    }

    /**
     * Make the config to reload screens on next request.
     */
    public void reset() {
        initialized = false;
    }

    public WindowInfo findWindowInfo(String id) {
        return findWindowInfo(id, null);
    }

    /**
     * Get screen information by screen ID.
     *
     * @param id         screen ID as set up in <code>screens.xml</code>
     * @param deviceInfo target device info
     * @return screen's registration information or null if not found
     */
    @Nullable
    public WindowInfo findWindowInfo(String id, @Nullable DeviceInfo deviceInfo) {
        lock.readLock().lock();
        try {
            checkInitialized();

            List<WindowInfo> infos = screens.get(id);

            if (infos == null) {
                Matcher matcher = ENTITY_SCREEN_PATTERN.matcher(id);
                if (matcher.matches()) {
                    MetaClass metaClass = metadata.getClass(matcher.group(1));
                    if (metaClass == null) {
                        return null;
                    }

                    MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                    if (originalMetaClass != null) {
                        String originalId = new StringBuilder(id)
                                .replace(matcher.start(1), matcher.end(1), originalMetaClass.getName()).toString();
                        infos = screens.get(originalId);
                    }
                }
            }

            List<WindowInfo> foundWindowInfos = infos;

            if (foundWindowInfos != null) {
                // do not perform stream processing in a simple case
                if (foundWindowInfos.size() == 1 && foundWindowInfos.get(0).getScreenAgent() == null) {
                    return foundWindowInfos.get(0);
                }

                if (deviceInfo == null) {
                    // find default screen
                    return foundWindowInfos.stream()
                            .filter(windowInfo -> windowInfo.getScreenAgent() == null)
                            .findFirst()
                            .orElse(null);
                } else {
                    return infos.stream().filter(wi ->
                            wi.getScreenAgent() != null
                                    && wi.getScreenAgent().isSupported(deviceInfo)
                    ).findFirst().orElseGet(() ->
                            foundWindowInfos.stream()
                                    .filter(windowInfo -> windowInfo.getScreenAgent() == null)
                                    .findFirst()
                                    .orElse(null)
                    );
                }
            }

            return null;
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
        return getWindowInfo(id, deviceInfoProvider.getDeviceInfo());
    }

    /**
     * Get screen information by screen ID.
     *
     * @param id         screen ID as set up in <code>screens.xml</code>
     * @param deviceInfo device info
     * @return screen's registration information
     * @throws NoSuchScreenException if the screen with specified ID is not registered
     */
    public WindowInfo getWindowInfo(String id, DeviceInfo deviceInfo) {
        WindowInfo windowInfo = findWindowInfo(id, deviceInfo);
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
            Collection<List<WindowInfo>> values = screens.values();
            return values.stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
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

    public WindowInfo getLookupScreen(Class<? extends Entity> entityClass) {
        MetaClass metaClass = metadata.getSession().getClass(entityClass);
        String lookupScreenId = getLookupScreenId(metaClass);
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