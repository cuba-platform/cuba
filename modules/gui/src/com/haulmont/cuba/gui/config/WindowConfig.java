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
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.xmlparsing.Dom4jTools;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.sys.*;
import com.haulmont.cuba.gui.xml.layout.ScreenXmlLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
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

    public static final String NAME = "cuba_WindowConfig";

    public static final String WINDOW_CONFIG_XML_PROP = "cuba.windowConfig";

    public static final Pattern ENTITY_SCREEN_PATTERN = Pattern.compile("([A-Za-z0-9]+[$_][A-Z][_A-Za-z0-9]*)\\..+");

    protected static final List<String> LOGIN_SCREEN_IDS = ImmutableList.of("login", "loginWindow");
    protected static final List<String> MAIN_SCREEN_IDS = ImmutableList.of("main", "mainWindow");

    protected static final String LOGIN_SCREEN_PROP = "cuba.web.loginScreenId";
    protected static final String MAIN_SCREEN_PROP = "cuba.web.mainScreenId";

    private final Logger log = LoggerFactory.getLogger(WindowConfig.class);

    protected Map<String, WindowInfo> screens = new HashMap<>();
    // route -> screen id
    protected BiMap<String, String> routes = HashBiMap.create();

    protected Map<Class, WindowInfo> primaryEditors = new HashMap<>();
    protected Map<Class, WindowInfo> primaryLookups = new HashMap<>();

    @Autowired(required = false)
    protected List<UiControllersConfiguration> configurations = Collections.emptyList();

    @Inject
    protected Resources resources;
    @Inject
    protected Scripting scripting;
    @Inject
    protected Metadata metadata;
    @Inject
    protected ScreenXmlLoader screenXmlLoader;
    @Inject
    protected Dom4jTools dom4JTools;

    @Inject
    protected ApplicationContext applicationContext;
    @Inject
    protected AnnotationScanMetadataReaderFactory metadataReaderFactory;
    @Inject
    protected UiControllerMetaProvider uiControllerMetaProvider;

    protected volatile boolean initialized;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected WindowAttributesProvider windowAttributesProvider = new WindowAttributesProvider() {
        @Override
        public WindowInfo.Type getType(WindowInfo windowInfo) {
            return resolveWindowInfo(windowInfo).getType();
        }

        @Nullable
        @Override
        public String getTemplate(WindowInfo windowInfo) {
            return resolveWindowInfo(windowInfo).getTemplate();
        }

        @Nonnull
        @Override
        public Class<? extends FrameOwner> getControllerClass(WindowInfo windowInfo) {
            return resolveWindowInfo(windowInfo).getControllerClass();
        }

        @Override
        public WindowInfo resolve(WindowInfo windowInfo) {
            return resolveWindowInfo(windowInfo);
        }
    };

    protected WindowInfo resolveWindowInfo(WindowInfo windowInfo) {
        Class<? extends FrameOwner> controllerClass;
        String template;

        if (windowInfo.getDescriptor() != null) {
            String className = windowInfo.getDescriptor().attributeValue("class");

            if (Strings.isNullOrEmpty(className)) {
                template = windowInfo.getDescriptor().attributeValue("template");

                Element screenXml = screenXmlLoader.load(template,
                        windowInfo.getId(), Collections.emptyMap());
                className = screenXml.attributeValue("class");
            } else {
                template = null;
            }

            if (Strings.isNullOrEmpty(className)) {
                // fallback for legacy frames
                controllerClass = AbstractFrame.class;
            } else {
                controllerClass = loadDefinedScreenClass(className);
            }

        } else if (windowInfo.getControllerClassName() != null) {
            controllerClass = loadDefinedScreenClass(windowInfo.getControllerClassName());

            UiDescriptor annotation = controllerClass.getAnnotation(UiDescriptor.class);
            if (annotation == null) {
                template = null;
            } else {
                String templatePath = UiDescriptorUtils.getInferredTemplate(annotation, controllerClass);
                if (!templatePath.startsWith("/")) {
                    String packageName = UiControllerUtils.getPackage(controllerClass);
                    if (StringUtils.isNotEmpty(packageName)) {
                        String relativePath = packageName.replace('.', '/');
                        templatePath = "/" + relativePath + "/" + templatePath;
                    }
                }

                template = templatePath;
            }
        } else {
            throw new IllegalStateException("Neither screen class nor descriptor is set for WindowInfo " + windowInfo.getId());
        }

        WindowInfo.Type type = extractWindowInfoType(windowInfo, controllerClass);

        return new ResolvedWindowInfo(windowInfo, type, controllerClass, template);
    }

    protected MetadataReaderFactory getMetadataReaderFactory() {
        return metadataReaderFactory;
    }

    protected ResourceLoader getResourceLoader() {
        return applicationContext;
    }

    protected WindowInfo.Type extractWindowInfoType(WindowInfo windowInfo, Class<? extends FrameOwner> controllerClass) {
        if (Screen.class.isAssignableFrom(controllerClass)) {
            return WindowInfo.Type.SCREEN;
        }

        if (ScreenFragment.class.isAssignableFrom(controllerClass)) {
            return WindowInfo.Type.FRAGMENT;
        }

        throw new IllegalStateException("Unknown type of screen " + windowInfo.getId());
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends FrameOwner> loadDefinedScreenClass(String className) {
        return (Class<? extends FrameOwner>) scripting.loadClassNN(className);
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
        primaryEditors.clear();
        primaryLookups.clear();

        routes.clear();

        loadScreenConfigurations();
        loadScreensXml();

        log.info("WindowConfig initialized in {} ms", System.currentTimeMillis() - startTime);
    }

    protected void loadScreenConfigurations() {
        for (UiControllersConfiguration provider : configurations) {
            List<UiControllerDefinition> uiControllers = provider.getUiControllers();

            Map<String, String> projectScreens = new HashMap<>(uiControllers.size());

            for (UiControllerDefinition definition : uiControllers) {
                String existingScreenController = projectScreens.get(definition.getId());
                if (existingScreenController != null
                        && !Objects.equals(existingScreenController, definition.getControllerClass())) {
                    throw new RuntimeException(
                            String.format("Project contains screens with the same id: '%s'. See '%s' and '%s'",
                                    definition.getId(),
                                    definition.getControllerClass(),
                                    existingScreenController));
                } else {
                    projectScreens.put(definition.getId(), definition.getControllerClass());
                }

                WindowInfo windowInfo = new WindowInfo(definition.getId(), windowAttributesProvider,
                        definition.getControllerClass(), definition.getRouteDefinition());
                registerScreen(definition, windowInfo);
            }

            projectScreens.clear();
        }
    }

    protected void loadScreensXml() {
        String configName = AppContext.getProperty(WINDOW_CONFIG_XML_PROP);
        StringTokenizer tokenizer = new StringTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                try (InputStream stream = resource.getInputStream()) {
                    loadConfig(dom4JTools.readDocument(stream).getRootElement());
                } catch (IOException e) {
                    throw new RuntimeException("Unable to read window config from " + location, e);
                }
            } else {
                log.warn("Resource {} not found, ignore it", location);
            }
        }
    }

    protected void loadConfig(Element rootElem) {
        for (Element element : rootElem.elements("include")) {
            String fileName = element.attributeValue("file");
            if (!StringUtils.isBlank(fileName)) {
                String incXml = resources.getResourceAsString(fileName);
                if (incXml == null) {
                    log.warn("File {} not found, ignore it", fileName);
                    continue;
                }
                loadConfig(dom4JTools.readDocument(incXml).getRootElement());
            }
        }
        for (Element element : rootElem.elements("screen")) {
            String id = element.attributeValue("id");
            if (StringUtils.isBlank(id)) {
                log.warn("Invalid window config: 'id' attribute not defined");
                continue;
            }

            RouteDefinition routeDef = loadRouteDefinition(element);

            WindowInfo windowInfo = new WindowInfo(id, windowAttributesProvider, element, routeDef);
            registerScreen(id, windowInfo);
        }
    }

    protected RouteDefinition loadRouteDefinition(Element screenElement) {
        String screenId = screenElement.attributeValue("id");
        String route = screenElement.attributeValue("route");
        String parentPrefix = screenElement.attributeValue("routeParentPrefix");
        boolean rootRoute = Boolean.parseBoolean(screenElement.attributeValue("rootRoute"));

        RouteDefinition routeDefinition;

        WindowInfo superScreen = screens.get(screenId);
        RouteDefinition superScreenRouteDefinition = superScreen != null
                ? superScreen.getRouteDefinition()
                : null;

        if (route != null && !route.isEmpty()) {
            if (superScreenRouteDefinition != null) {
                String superScreenRoute = superScreenRouteDefinition.getPath();
                String superScreenParentPrefix = superScreenRouteDefinition.getParentPrefix();

                if (!route.equals(superScreenRoute)) {
                    log.debug("Route for screen '{}' is redefined from '{}' to '{}'",
                            screenId, superScreenRoute, rootRoute);

                    routes.remove(superScreenRoute);
                }

                if (parentPrefix == null || parentPrefix.isEmpty()) {
                    parentPrefix = superScreenParentPrefix;
                }
            }
            routeDefinition = new RouteDefinition(route, parentPrefix, rootRoute);
        } else {
            routeDefinition = superScreenRouteDefinition;
        }

        return routeDefinition;
    }

    protected void registerScreen(UiControllerDefinition controllerDefinition, WindowInfo windowInfo) {
        String controllerClassName = windowInfo.getControllerClassName();
        if (controllerClassName != null) {
            registerPrimaryEditor(windowInfo, controllerDefinition);
            registerPrimaryLookup(windowInfo, controllerDefinition);
        }

        screens.put(controllerDefinition.getId(), windowInfo);

        registerScreenRoute(controllerDefinition.getId(), windowInfo);
    }

    protected void registerScreen(String id, WindowInfo windowInfo) {
        String controllerClassName = windowInfo.getControllerClassName();
        if (controllerClassName != null) {
            MetadataReader classMetadata = loadClassMetadata(controllerClassName);
            AnnotationMetadata annotationMetadata = classMetadata.getAnnotationMetadata();

            registerPrimaryEditor(windowInfo, annotationMetadata);
            registerPrimaryLookup(windowInfo, annotationMetadata);
        }

        screens.put(id, windowInfo);

        registerScreenRoute(id, windowInfo);
    }

    protected void registerScreenRoute(String screenId, WindowInfo windowInfo) {
        RouteDefinition routeDef = windowInfo.getRouteDefinition();
        if (routeDef != null) {
            String route = routeDef.getPath();
            String registeredScreenId = routes.get(route);
            if (registeredScreenId != null
                    && !Objects.equals(screenId, registeredScreenId)) {

                if (!routeOverrideAllowed(screenId)) {
                    return;
                }

                log.debug("Multiple use of the route '{}' for different screens is detected: '{}' and '{}'. " +
                                "The screen '{}' will be opened during navigation as the last registered screen",
                        route, screenId, registeredScreenId, screenId);
            }

            String registeredRoute = routes.inverse().get(screenId);
            if (StringUtils.isNotEmpty(registeredRoute)
                    && !Objects.equals(registeredRoute, route)) {
                log.debug("Route for screen '{}' is redefined from '{}' to '{}'",
                        screenId, registeredRoute, route);

                routes.remove(registeredRoute);
            }

            routes.put(route, screenId);
        }
    }

    /**
     * Have to do this check due to Login/Main Screen are registered
     * before legacy LoginWindow / AppMainWindow.
     */
    protected boolean routeOverrideAllowed(String newScreenId) {
        if (LOGIN_SCREEN_IDS.contains(newScreenId)) {
            String loginScreenId = AppContext.getProperty(LOGIN_SCREEN_PROP);
            return StringUtils.equals(loginScreenId, newScreenId);
        }

        if (MAIN_SCREEN_IDS.contains(newScreenId)) {
            String mainScreenId = AppContext.getProperty(MAIN_SCREEN_PROP);
            return StringUtils.equals(mainScreenId, newScreenId);
        }

        return true;
    }

    protected void registerPrimaryEditor(WindowInfo windowInfo, AnnotationMetadata annotationMetadata) {
        Map<String, Object> primaryEditorAnnotation =
                annotationMetadata.getAnnotationAttributes(PrimaryEditorScreen.class.getName());
        registerPrimaryEditor(windowInfo, primaryEditorAnnotation);
    }

    protected void registerPrimaryEditor(WindowInfo windowInfo, UiControllerDefinition controllerDefinition) {
        Map<String, Object> primaryEditorAnnotation = controllerDefinition.getControllerMeta()
                .getAnnotationAttributes(PrimaryEditorScreen.class.getName());
        registerPrimaryEditor(windowInfo, primaryEditorAnnotation);
    }

    protected void registerPrimaryEditor(WindowInfo windowInfo, Map<String, Object> primaryEditorAnnotation) {
        if (primaryEditorAnnotation != null) {
            Class entityClass = (Class) primaryEditorAnnotation.get("value");
            if (entityClass != null) {
                MetaClass metaClass = metadata.getClass(entityClass);
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);
                primaryEditors.put(originalMetaClass.getJavaClass(), windowInfo);
            }
        }
    }

    protected void registerPrimaryLookup(WindowInfo windowInfo, AnnotationMetadata annotationMetadata) {
        Map<String, Object> primaryEditorAnnotation =
                annotationMetadata.getAnnotationAttributes(PrimaryLookupScreen.class.getName());
        registerPrimaryLookup(windowInfo, primaryEditorAnnotation);
    }

    protected void registerPrimaryLookup(WindowInfo windowInfo, UiControllerDefinition controllerDefinition) {
        Map<String, Object> primaryEditorAnnotation = controllerDefinition.getControllerMeta()
                .getAnnotationAttributes(PrimaryLookupScreen.class.getName());
        registerPrimaryLookup(windowInfo, primaryEditorAnnotation);
    }

    protected void registerPrimaryLookup(WindowInfo windowInfo, Map<String, Object> primaryEditorAnnotation) {
        if (primaryEditorAnnotation != null) {
            Class entityClass = (Class) primaryEditorAnnotation.get("value");
            if (entityClass != null) {
                MetaClass metaClass = metadata.getClass(entityClass);
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);
                primaryLookups.put(originalMetaClass.getJavaClass(), windowInfo);
            }
        }
    }

    protected MetadataReader loadClassMetadata(String className) {
        Resource resource = getResourceLoader().getResource("/" + className.replace(".", "/") + ".class");
        if (!resource.isReadable()) {
            throw new RuntimeException(String.format("Resource %s is not readable for class %s", resource, className));
        }
        try {
            return getMetadataReaderFactory().getMetadataReader(resource);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read resource " + resource, e);
        }
    }

    /**
     * Loads hot-deployed {@link UiController} screens and registers
     * {@link UiControllersConfiguration} containing new {@link UiControllerDefinition}.
     *
     * @param className the fully qualified name of the screen class to load
     */
    public void loadScreenClass(final String className) {
        final Class screenClass = scripting.loadClass(className);
        if (screenClass == null
                || !FrameOwner.class.isAssignableFrom(screenClass)) {
            log.warn("Failed to hot deploy screen '{}'. Unable to load screen class", className);
            return;
        }

        //noinspection unchecked
        UiControllerDefinition uiControllerDefinition = new UiControllerDefinition(uiControllerMetaProvider.get(screenClass));

        UiControllersConfiguration controllersConfiguration = new UiControllersConfiguration();

        controllersConfiguration.setApplicationContext(applicationContext);
        controllersConfiguration.setMetadataReaderFactory(metadataReaderFactory);
        controllersConfiguration.setExplicitDefinitions(Collections.singletonList(uiControllerDefinition));

        configurations.add(controllersConfiguration);

        reset();
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
     * Get screen information by route.
     *
     * @param route route
     * @return screen's registration information or null if not found
     */
    @Nullable
    public WindowInfo findWindowInfoByRoute(String route) {
        String screenId = routes.get(route);
        return screenId != null
                ? findWindowInfo(screenId)
                : null;
    }

    /**
     * Find route by screen id.
     *
     * @param id screen id
     * @return registered route or null if no route for screen
     */
    @Nullable
    public String findRoute(String id) {
        return routes.inverse().get(id);
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
        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);
        WindowInfo windowInfo = primaryLookups.get(originalMetaClass.getJavaClass());
        if (windowInfo != null) {
            return windowInfo.getId();
        }

        return getMetaClassScreenId(metaClass, Window.LOOKUP_WINDOW_SUFFIX);
    }

    public String getEditorScreenId(MetaClass metaClass) {
        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);
        WindowInfo windowInfo = primaryEditors.get(originalMetaClass.getJavaClass());
        if (windowInfo != null) {
            return windowInfo.getId();
        }

        return getMetaClassScreenId(metaClass, Window.EDITOR_WINDOW_SUFFIX);
    }

    public WindowInfo getEditorScreen(Entity entity) {
        MetaClass metaClass = entity.getMetaClass();
        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);
        WindowInfo windowInfo = primaryEditors.get(originalMetaClass.getJavaClass());
        if (windowInfo != null) {
            return windowInfo;
        }

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

        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);
        WindowInfo windowInfo = primaryLookups.get(originalMetaClass.getJavaClass());
        if (windowInfo != null) {
            return windowInfo;
        }

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

    public static class ResolvedWindowInfo extends WindowInfo {

        protected final String template;
        protected final Class<? extends FrameOwner> controllerClass;
        protected final Type type;

        public ResolvedWindowInfo(WindowInfo windowInfo, Type type, Class<? extends FrameOwner> controllerClass,
                                  String template) {
            super(windowInfo.getId(), null, windowInfo.getDescriptor(),
                    windowInfo.getControllerClassName(), windowInfo.getRouteDefinition());

            this.template = template;

            this.controllerClass = controllerClass;
            this.type = type;
        }

        @Nullable
        @Override
        public String getTemplate() {
            return template;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Nonnull
        @Override
        public Class<? extends FrameOwner> getControllerClass() {
            return controllerClass;
        }

        @Override
        public WindowInfo resolve() {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ResolvedWindowInfo that = (ResolvedWindowInfo) o;
            return Objects.equals(template, that.template) &&
                    Objects.equals(controllerClass, that.controllerClass) &&
                    type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(template, controllerClass, type);
        }
    }
}