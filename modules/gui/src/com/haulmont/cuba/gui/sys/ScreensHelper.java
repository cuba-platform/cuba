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

package com.haulmont.cuba.gui.sys;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.Fragment;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.RowsCount;
import com.haulmont.cuba.gui.components.ScreenComponentDescriptor;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.screen.EditedEntityContainer;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.gui.xml.XmlInheritanceProcessor;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyMap;

@Component(ScreensHelper.NAME)
public class ScreensHelper {
    public static final String NAME = "cuba_ScreensHelper";

    private static final Logger log = LoggerFactory.getLogger(ScreensHelper.class);

    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Resources resources;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Metadata metadata;

    @Inject
    protected LayoutLoaderConfig layoutLoaderConfig;

    @Inject
    protected BeanLocator beanLocator;

    protected Map<String, String> captionCache = new ConcurrentHashMap<>();
    protected Map<String, Map<String, String>> availableScreensCache = new ConcurrentHashMap<>();
    protected Map<String, List<ScreenComponentDescriptor>> screenComponentsCache = new ConcurrentHashMap<>();

    /**
     * Sorts window infos alphabetically, takes into account $ mark.
     *
     * @param windowInfoCollection mutable list of window infos
     */
    public void sortWindowInfos(List<WindowInfo> windowInfoCollection) {
        windowInfoCollection.sort((w1, w2) -> {
            int w1DollarIndex = w1.getId().indexOf("$");
            int w2DollarIndex = w2.getId().indexOf("$");

            if ((w1DollarIndex > 0 && w2DollarIndex > 0) || (w1DollarIndex < 0 && w2DollarIndex < 0)) {
                return w1.getId().compareTo(w2.getId());
            } else if (w1DollarIndex > 0) {
                return -1;
            } else {
                return 1;
            }
        });
    }

    @Nullable
    public WindowInfo getDefaultBrowseScreen(MetaClass metaClass) {
        WindowInfo browseWindow = windowConfig.findWindowInfo(windowConfig.getBrowseScreenId(metaClass));
        if (browseWindow != null
                && userSessionSource.getUserSession().isScreenPermitted(browseWindow.getId())) {
            return browseWindow;
        }

        WindowInfo lookupWindow = windowConfig.findWindowInfo(windowConfig.getLookupScreenId(metaClass));
        if (lookupWindow != null
                && userSessionSource.getUserSession().isScreenPermitted(lookupWindow.getId())) {
            return lookupWindow;
        }

        return null;
    }

    public List<ScreenComponentDescriptor> getScreenComponents(String screenId) {
        String key = getScreenComponentsCacheKey(screenId, userSessionSource.getLocale());
        List<ScreenComponentDescriptor> screenComponents = screenComponentsCache.get(key);
        if (screenComponents != null) {
            return screenComponents;
        }

        List<ScreenComponentDescriptor> components = new ArrayList<>();

        WindowInfo windowInfo = windowConfig.findWindowInfo(screenId);
        if (windowInfo != null) {
            String template = windowInfo.getTemplate();
            try {
                Element layoutElement = getRootLayoutElement(template);
                if (layoutElement != null) {
                    findScreenComponents(components, null, layoutElement);
                }
            } catch (FileNotFoundException e) {
                log.error("Can't obtain screen's root layout: ", e);
            }
        }

        components = ImmutableList.copyOf(components);

        cacheScreenComponents(key, components);
        return components;
    }

    public void findScreenComponents(List<ScreenComponentDescriptor> components,
                                     @Nullable ScreenComponentDescriptor parent, Element root) {
        List<Element> elements = isFrame(root) ? getFrameElements(root) : root.elements();
        for (Element element : elements) {
            if (isComponentElement(element)) {
                //noinspection IncorrectCreateEntity
                ScreenComponentDescriptor descriptor = new ScreenComponentDescriptor(element, parent);
                components.add(descriptor);
                findScreenComponents(components, descriptor, element);
            }
        }
    }

    protected List<Element> getFrameElements(Element frameElement) {
        String src = frameElement.attributeValue("src");
        if (Strings.isNullOrEmpty(src)) {
            String screenId = frameElement.attributeValue("screen");
            if (!Strings.isNullOrEmpty(screenId)) {
                src = windowConfig.getWindowInfo(screenId).getTemplate();
            }
        }

        if (!Strings.isNullOrEmpty(src)) {
            try {
                Element layoutElement = getRootLayoutElement(src);
                if (layoutElement != null) {
                    return layoutElement.elements();
                }
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
            }
        }
        return Collections.emptyList();
    }

    protected boolean isFrame(Element element) {
        return Frame.NAME.equals(element.getName())
                || Fragment.NAME.equals(element.getName());
    }

    protected boolean isComponentElement(Element element) {
        Class<? extends ComponentLoader> loader = layoutLoaderConfig.getLoader(element.getName());
        return !isExclusion(element) &&
                (loader != null
                        || isAction(element)
                        || isTab(element)
                        || isRow(element)
                        || isField(element)
                        || isFieldGroupColumn(element)
                );
    }

    protected boolean isAction(Element element) {
        return "action".equals(element.getName())
                || "actions".equals(element.getName());
    }

    protected boolean isTab(Element element) {
        return "tab".equals(element.getName());
    }

    protected boolean isRow(Element element) {
        return "row".equals(element.getName())
                || "rows".equals(element.getName());
    }

    protected boolean isField(Element element) {
        return "field".equals(element.getName());
    }

    protected boolean isFieldGroupColumn(Element element) {
        return "column".equals(element.getName())
                && element.getParent() != null
                && FieldGroup.NAME.equals(element.getParent().getName());
    }

    protected boolean isExclusion(Element element) {
        return RowsCount.NAME.equals(element.getName())
                || isTableRows(element);
    }

    protected boolean isTableRows(Element element) {
        return "rows".equals(element.getName())
                && element.getParent() != null
                && StringUtils.containsIgnoreCase(element.getParent().getName(), Table.NAME);
    }

    protected enum ScreenType {
        BROWSER, EDITOR, ALL
    }

    public Map<String, String> getAvailableBrowserScreens(Class entityClass) {
        return getAvailableScreensMap(entityClass, ScreenType.BROWSER);
    }

    public Map<String, String> getAvailableScreens(Class entityClass) {
        return getAvailableScreensMap(entityClass, ScreenType.ALL);
    }

    protected Map<String, String> getAvailableScreensMap(Class entityClass, ScreenType filterScreenType) {
        String key = getScreensCacheKey(entityClass.getName(), userSessionSource.getLocale(), filterScreenType);
        Map<String, String> screensMap = availableScreensCache.get(key);
        if (screensMap != null) {
            return screensMap;
        }

        Collection<WindowInfo> windowInfoCollection = windowConfig.getWindows();
        screensMap = new TreeMap<>();

        Set<String> visitedWindowIds = new HashSet<>();

        for (WindowInfo windowInfo : windowInfoCollection) {
            String windowId = windowInfo.getId();

            // just skip for now, we assume all versions of screen can operate with the same entity
            if (visitedWindowIds.contains(windowId)) {
                continue;
            }

            String src = windowInfo.getTemplate();
            if (StringUtils.isNotEmpty(src)) {
                try {
                    Element windowElement = getWindowElement(src);
                    if (windowElement != null) {
                        if (isEntityAvailable(windowElement,
                                windowInfo.getControllerClass(), entityClass, filterScreenType)) {
                            String caption = getScreenCaption(windowElement, src);
                            caption = getDetailedScreenCaption(caption, windowId);
                            screensMap.put(caption, windowId);
                        }
                    } else {
                        screensMap.put(windowId, windowId);
                    }
                } catch (FileNotFoundException e) {
                    log.error("Unable to find file of screen", e.getMessage());
                }
            }

            visitedWindowIds.add(windowId);
        }

        screensMap = ImmutableMap.copyOf(screensMap);

        cacheScreens(key, screensMap);
        return screensMap;
    }

    protected boolean isEntityAvailable(Element window, Class<? extends FrameOwner> controllerClass,
                                        Class entityClass, ScreenType filterScreenType) {
        return isEntityAvailableInDataContainer(window, controllerClass, entityClass, filterScreenType)
                || isEntityAvailableInDatasource(window, entityClass, filterScreenType);
    }

    protected boolean isEntityAvailableInDataContainer(Element window, Class<? extends FrameOwner> controllerClass,
                                                       Class entityClass, ScreenType filterScreenType) {
        Element data = window.element("data");
        if (data == null) {
            return false;
        }

        String containerId = getDataContainerId(window, controllerClass, filterScreenType);
        if (Strings.isNullOrEmpty(containerId)) {
            return false;
        }

        return isEntityAvailableInDataElement(entityClass, data, containerId);
    }

    protected boolean isEntityAvailableInDatasource(Element window, Class entityClass, ScreenType filterScreenType) {
        Element dsContext = window.element("dsContext");
        if (dsContext == null) {
            return false;
        }

        String datasourceId = getDatasourceId(window, filterScreenType);
        if (StringUtils.isEmpty(datasourceId)) {
            return false;
        }

        return isEntityAvailableInDataElement(entityClass, dsContext, datasourceId);
    }

    protected boolean isEntityAvailableInDataElement(Class entityClass, Element dataElement, String datasourceId) {
        Element datasource = elementByID(dataElement, datasourceId);
        if (datasource == null) {
            return false;
        }

        String dsClassValue = datasource.attributeValue("class");
        if (StringUtils.isEmpty(dsClassValue)) {
            return false;
        }

        Class entity = entityClass;
        boolean isAvailable;
        boolean process;
        do {
            isAvailable = dsClassValue.equals(entity.getName());
            entity = entity.getSuperclass();
            process = metadata.getClass(entity) != null && metadataTools.isPersistent(entity);
        } while (process && !isAvailable);
        return isAvailable;
    }


    @Nullable
    protected String getDataContainerId(Element window,
                                        Class<? extends FrameOwner> controllerClass, ScreenType filterScreenType) {
        String windowDc = resolveEditedEntityContainerId(controllerClass);
        String lookupDc = resolveLookupDataContainer(window, controllerClass);

        if (filterScreenType == ScreenType.ALL) {
            return windowDc != null ? windowDc : lookupDc;
        } else {
            return filterScreenType == ScreenType.BROWSER ? lookupDc : windowDc;
        }
    }

    @Nullable
    protected String resolveEditedEntityContainerId(Class<? extends FrameOwner> controllerClass) {
        EditedEntityContainer annotation = controllerClass.getAnnotation(EditedEntityContainer.class);
        return annotation != null ? annotation.value() : null;
    }

    @Nullable
    protected String resolveLookupComponentId(Class<? extends FrameOwner> controllerClass) {
        LookupComponent annotation = controllerClass.getAnnotation(LookupComponent.class);
        return annotation != null ? annotation.value() : null;
    }

    @Nullable
    protected String getDatasourceId(Element window, ScreenType filterScreenType) {
        String windowDatasource = window.attributeValue("datasource");
        String lookupDatasource = resolveLookupDatasource(window);
        if (filterScreenType == ScreenType.ALL) {
            return windowDatasource != null ? windowDatasource : lookupDatasource;
        } else if (filterScreenType == ScreenType.BROWSER) {
            return lookupDatasource;
        } else {
            return windowDatasource;
        }
    }

    @Nullable
    protected String resolveLookupDataContainer(Element window, Class<? extends FrameOwner> controllerClass) {
        String lookupId = resolveLookupComponentId(controllerClass);
        if (Strings.isNullOrEmpty(lookupId)) {
            return null;
        }

        Element lookupElement = elementByID(window, lookupId);
        return lookupElement != null
                ? findLookupElementDataAttributeId(lookupElement, "dataContainer")
                : null;
    }

    @Nullable
    protected String resolveLookupDatasource(Element window) {
        String lookupId = window.attributeValue("lookupComponent");
        Element lookupElement = null;
        if (StringUtils.isNotBlank(lookupId)) {
            lookupElement = elementByID(window, lookupId);
        }

        return lookupElement != null
                ? findLookupElementDataAttributeId(lookupElement, "datasource")
                : null;
    }

    @Nullable
    protected String findLookupElementDataAttributeId(Element lookupElement, String dataAttribute) {
        String datasource = lookupElement.attributeValue(dataAttribute);
        if (StringUtils.isNotBlank(datasource)) {
            return datasource;
        }
        for (Element element : lookupElement.elements()) {
            datasource = element.attributeValue(dataAttribute);
            if (StringUtils.isNotBlank(datasource)) {
                return datasource;
            }
        }

        return null;
    }

    @Nullable
    protected Element elementByID(Element root, String elementId) {
        for (Element element : root.elements()) {
            String id = element.attributeValue("id");
            if (StringUtils.isNotEmpty(id) && elementId.equals(id)) {
                return element;
            } else {
                element = elementByID(element, elementId);
                if (element != null) {
                    return element;
                }
            }
        }
        return null;
    }

    @Nullable
    protected Element getWindowElement(String src) throws FileNotFoundException {
        String text = resources.getResourceAsString(src);
        if (StringUtils.isNotEmpty(text)) {
            try {
                Document document = Dom4j.readDocument(text);
                XmlInheritanceProcessor processor =
                        beanLocator.getPrototype(XmlInheritanceProcessor.NAME, document, emptyMap());
                Element root = processor.getResultRoot();
                if (root.getName().equals(Window.NAME)) {
                    return root;
                }
            } catch (RuntimeException e) {
                log.error("Can't parse screen file: ", src);
            }
        } else {
            throw new FileNotFoundException("File doesn't exist or empty: " + src);
        }
        return null;
    }

    @Nullable
    protected Element getRootLayoutElement(String src) throws FileNotFoundException {
        Element windowElement = getWindowElement(src);
        if (windowElement != null) {
            return windowElement.element("layout");
        }

        return null;
    }

    @Nullable
    public String getScreenCaption(WindowInfo windowInfo) throws FileNotFoundException {
        return getScreenCaption(windowInfo, userSessionSource.getLocale());
    }

    @Nullable
    public String getScreenCaption(WindowInfo windowInfo, Locale locale) throws FileNotFoundException {
        String src = windowInfo.getTemplate();
        if (StringUtils.isNotEmpty(src)) {
            String key = getCaptionCacheKey(src, locale);
            String caption = captionCache.get(key);
            if (caption != null) {
                return caption;
            }

            Element window = getWindowElement(src);
            if (window != null) {
                return getScreenCaption(window, src);
            }
        }

        Class screenClass = windowInfo.getControllerClass();
        return screenClass.getSimpleName();
    }

    protected String getScreenCaption(Element window, String src) {
        return getScreenCaption(window, src, userSessionSource.getLocale());
    }

    protected String getScreenCaption(Element window, String src, Locale locale) {
        String key = getCaptionCacheKey(src, locale);
        String caption = captionCache.get(key);
        if (caption != null)
            return caption;

        caption = window.attributeValue("caption");
        if (StringUtils.isNotEmpty(caption)) {
            if (!caption.startsWith("msg://")) {
                cacheCaption(key, caption);
                return caption;
            }

            String messagePack = window.attributeValue("messagesPack");
            if (StringUtils.isEmpty(messagePack)) {
                messagePack = packageFromFilePath(src);
            }
            if (StringUtils.isNotEmpty(messagePack)) {
                caption = messageTools.loadString(messagePack, caption);
                cacheCaption(key, caption);
                return caption;
            }
        }
        caption = StringUtils.EMPTY;
        cacheCaption(key, caption);
        return caption;
    }

    public String getDetailedScreenCaption(WindowInfo windowInfo) throws FileNotFoundException {
        return getDetailedScreenCaption(windowInfo, userSessionSource.getLocale());
    }

    public String getDetailedScreenCaption(WindowInfo windowInfo, Locale locale) throws FileNotFoundException {
        String caption = getScreenCaption(windowInfo, locale);
        return getDetailedScreenCaption(caption, windowInfo.getId());
    }

    protected String getDetailedScreenCaption(String caption, String windowId) {
        return StringUtils.isNotEmpty(caption) ? caption + " (" + windowId + ")" : windowId;
    }

    @Nullable
    protected String packageFromFilePath(String path) {
        String screenPackage = null;
        int endIndex = path.lastIndexOf("/");
        if (endIndex > 0) {
            int beginIndex = 0;
            if (path.startsWith("/"))
                beginIndex = 1;
            path = path.substring(beginIndex, endIndex);
            screenPackage = path.replaceAll("/", ".");
        }
        return screenPackage;
    }

    protected void cacheCaption(String key, String value) {
        if (!captionCache.containsKey(key)) {
            captionCache.put(key, value);
        }
    }

    protected void cacheScreens(String key, Map<String, String> value) {
        if (!availableScreensCache.containsKey(key)) {
            availableScreensCache.put(key, value);
        }
    }

    protected void cacheScreenComponents(String key, List<ScreenComponentDescriptor> value) {
        if (!screenComponentsCache.containsKey(key)) {
            screenComponentsCache.put(key, value);
        }
    }

    protected String getCaptionCacheKey(String src, Locale locale) {
        return src + locale.toString();
    }

    protected String getScreensCacheKey(String className, Locale locale, ScreenType filterScreenType) {
        return String.format("%s_%s_%s", className, locale.toString(), filterScreenType);
    }

    protected String getScreenComponentsCacheKey(String screenId, Locale locale) {
        return screenId + locale.toString();
    }

    public void clearCache() {
        captionCache.clear();
        availableScreensCache.clear();
        screenComponentsCache.clear();
    }
}