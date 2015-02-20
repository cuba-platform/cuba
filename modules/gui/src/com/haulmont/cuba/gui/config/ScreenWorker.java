/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Window;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.Entity;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gorelov
 * @version $Id$
 */
@ManagedBean(ScreenWorker.NAME)
public class ScreenWorker {
    public static final String NAME = "cuba_ScreenWorker";
    private static final String EMPTY_SCREEN_CAPTION = "";

    private Log logger = LogFactory.getLog(ScreenWorker.class);

    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Resources resources;

    @Inject
    private UserSessionSource userSessionSource;

    private Map<String, String> captionCache = new ConcurrentHashMap<>();
    private Map<String, Map<String, Object>> availableScreensCache = new ConcurrentHashMap<>();

    public Map<String, Object> getAvailableScreensMap(Class entityClass) {
        Map<String, Object> screensMap = availableScreensCache.get(entityClass.getName());
        if (screensMap != null)
            return screensMap;

        List<WindowInfo> windowInfoCollection =  new ArrayList<>(windowConfig.getWindows());
        screensMap = new TreeMap<>();
        for (WindowInfo windowInfo : windowInfoCollection) {
            String windowId = windowInfo.getId();
            if (!windowId.contains(".")
                    || windowId.contains(Window.BROWSE_WINDOW_SUFFIX)
                    || windowId.contains(Window.LOOKUP_WINDOW_SUFFIX)) {
                String src = windowInfo.getTemplate();
                if (StringUtils.isNotEmpty(src)) {
                    try {
                        Element windowElement = getWindowElement(src);
                        if (windowElement != null) {
                            if (isEntityAvailable(windowElement, entityClass)) {
                                String caption = getScreenCaption(windowElement, src);
                                if (StringUtils.isNotEmpty(caption)) {
                                    caption = caption + " (" + windowId + ")";
                                } else {
                                    caption = windowId;
                                }
                                screensMap.put(caption, windowId);
                            }
                        } else {
                            screensMap.put(windowId, windowId);
                        }
                    } catch (FileNotFoundException e) {
                        logger.error(e);
                    }
                }
            }
        }
        cacheScreens(entityClass.getName(), screensMap);
        return screensMap;
    }

    protected boolean isEntityAvailable(Element window, Class entityClass) {
        String lookupId = window.attributeValue("lookupComponent");
        if (StringUtils.isEmpty(lookupId))
            return false;

        Element dsContext = window.element("dsContext");
        if (dsContext == null)
            return false;

        Element lookupElement = elementByID(window, lookupId);
        if (lookupElement == null)
            return false;

        String datasourceId = null;
        for ( Iterator i = lookupElement.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String datasource = element.attributeValue("datasource");
            if (StringUtils.isNotEmpty(datasource)) {
                datasourceId = datasource;
            }
        }
        if (StringUtils.isEmpty(datasourceId))
            return false;

        Element datasource = elementByID(dsContext, datasourceId);
        if (datasource == null)
            return false;

        String dsClassValue = datasource.attributeValue("class");
        if (StringUtils.isEmpty(dsClassValue))
            return false;

        Class entity = entityClass;
        boolean isAvailable;
        boolean process = true;
        do {
            isAvailable = dsClassValue.equals(entity.getName());
            entity = entity.getSuperclass();
            if (entity.getAnnotation(Entity.class) == null) {
                process = false;
            }
        } while (process && !isAvailable);
        return isAvailable;
    }

    @Nullable
    protected Element elementByID(Element root, String elementId) {
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
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
                Document document = DocumentHelper.parseText(text);
                Element root = document.getRootElement();
                if (root.getName().equals(Window.NAME))
                    return root;
            } catch (DocumentException e) {
                logger.error("Can't parse screen file: " + src, e);
                return null;
            }
        }
        throw new FileNotFoundException("File doesn't exist or empty");
    }

    @Nullable
    public String getScreenCaption(WindowInfo windowInfo) throws FileNotFoundException {
        return getScreenCaption(windowInfo, getUserLocale());
    }

    @Nullable
    public String getScreenCaption(WindowInfo windowInfo, Locale locale) throws FileNotFoundException {
        String src = windowInfo.getTemplate();
        if (StringUtils.isNotEmpty(src)) {
            String key = getCaptionCacheKey(src, locale);
            String caption = captionCache.get(key);
            if (caption != null)
                return caption;

            Element window = getWindowElement(src);
            if (window != null)
                return getScreenCaption(window, src);
        }

        Class screenClass = windowInfo.getScreenClass();
        if (screenClass != null)
            return screenClass.getSimpleName();

        return null;
    }

    protected String getScreenCaption(Element window, String src) {
        return getScreenCaption(window, src, getUserLocale());
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
        caption = EMPTY_SCREEN_CAPTION;
        cacheCaption(key, caption);
        return caption;
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
            screenPackage = path.replaceAll("[/]", ".");
        }
        return screenPackage;
    }

    protected Locale getUserLocale() {
        return userSessionSource.checkCurrentUserSession() ?
                userSessionSource.getUserSession().getLocale() :
                messageTools.getDefaultLocale();
    }

    protected void cacheCaption(String key, String value) {
        if (!captionCache.containsKey(key))
            captionCache.put(key, value);
    }

    protected String getCaptionCacheKey(String src, Locale locale) {
        return src + locale.toString();
    }

    protected void cacheScreens(String key, Map<String, Object> value) {
        if (!availableScreensCache.containsKey(key))
            availableScreensCache.put(key, value);
    }

    public void clearCache() {
        captionCache.clear();
        availableScreensCache.clear();
    }
}
