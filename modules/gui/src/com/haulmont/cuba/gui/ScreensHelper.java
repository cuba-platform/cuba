/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.xml.XmlInheritanceProcessor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(ScreensHelper.NAME)
public class ScreensHelper {
    public static final String NAME = "cuba_ScreensHelper";
    private static final String EMPTY_SCREEN_CAPTION = "";
    private static final Map<String, Object> EMPTY_MAP = new HashMap<>();

    private Log logger = LogFactory.getLog(ScreensHelper.class);

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

    private Map<String, String> captionCache = new ConcurrentHashMap<>();
    private Map<String, Map<String, Object>> availableScreensCache = new ConcurrentHashMap<>();

    /**
     * Sorts window infos alphabetically, takes into account $ mark
     */
    public void sortWindowInfos(List<WindowInfo> windowInfoCollection) {
        Collections.sort(windowInfoCollection, new Comparator<WindowInfo>() {
            @Override
            public int compare(WindowInfo w1, WindowInfo w2) {
                int w1DollarIndex = w1.getId().indexOf("$");
                int w2DollarIndex = w2.getId().indexOf("$");

                if ((w1DollarIndex > 0 && w2DollarIndex > 0) || (w1DollarIndex < 0 && w2DollarIndex < 0)) {
                    return w1.getId().compareTo(w2.getId());
                } else if (w1DollarIndex > 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    @Nullable
    public WindowInfo getAvailableBrowseScreen(MetaClass metaClass) {
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

    public Map<String, Object> getAvailableScreensMap(Class entityClass) {
        String key = getScreensKey(entityClass.getName(), getUserLocale());
        Map<String, Object> screensMap = availableScreensCache.get(key);
        if (screensMap != null)
            return screensMap;

        List<WindowInfo> windowInfoCollection =  new ArrayList<>(windowConfig.getWindows());
        screensMap = new TreeMap<>();
        for (WindowInfo windowInfo : windowInfoCollection) {
            String windowId = windowInfo.getId();
            String src = windowInfo.getTemplate();
            if (StringUtils.isNotEmpty(src)) {
                try {
                    Element windowElement = getWindowElement(src);
                    if (windowElement != null) {
                        if (isEntityAvailable(windowElement, entityClass)) {
                            String caption = getScreenCaption(windowElement, src);
                            caption = StringUtils.isNotEmpty(caption) ? caption + " (" + windowId + ")" : windowId;
                            screensMap.put(caption, windowId);
                        }
                    } else {
                        screensMap.put(windowId, windowId);
                    }
                } catch (FileNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        cacheScreens(key, screensMap);
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
        for (Element element : Dom4j.elements(lookupElement)) {
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
        boolean process;
        do {
            isAvailable = dsClassValue.equals(entity.getName());
            entity = entity.getSuperclass();
            process = metadataTools.isPersistent(entity);
        } while (process && !isAvailable);
        return isAvailable;
    }

    @Nullable
    protected Element elementByID(Element root, String elementId) {
        for (Element element : Dom4j.elements(root)) {
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
                XmlInheritanceProcessor processor = new XmlInheritanceProcessor(document, EMPTY_MAP);
                Element root = processor.getResultRoot();
                if (root.getName().equals(Window.NAME))
                    return root;
            } catch (RuntimeException e) {
                logger.error("Can't parse screen file: " + src);
            }
        } else {
            throw new FileNotFoundException("File doesn't exist or empty: " + src);
        }
        return null;
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
            screenPackage = path.replaceAll("/", ".");
        }
        return screenPackage;
    }

    protected Locale getUserLocale() {
        return userSessionSource.getUserSession().getLocale();
    }

    protected void cacheCaption(String key, String value) {
        if (!captionCache.containsKey(key))
            captionCache.put(key, value);
    }

    protected void cacheScreens(String key, Map<String, Object> value) {
        if (!availableScreensCache.containsKey(key))
            availableScreensCache.put(key, value);
    }

    protected String getCaptionCacheKey(String src, Locale locale) {
        return src + locale.toString();
    }

    protected String getScreensKey(String className, Locale locale) {
        return className + locale.toString();
    }

    public void clearCache() {
        captionCache.clear();
        availableScreensCache.clear();
    }
}