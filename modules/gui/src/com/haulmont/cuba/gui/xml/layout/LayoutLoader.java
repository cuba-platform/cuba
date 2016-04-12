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
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.XmlInheritanceProcessor;
import com.haulmont.cuba.gui.xml.layout.loaders.FrameLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.WindowLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class LayoutLoader {

    protected ComponentLoader.Context context;
    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    protected Locale locale;
    protected String messagesPack;

    public static Document parseDescriptor(InputStream stream) {
        checkNotNullArgument(stream, "Input stream is null");

        String template;
        try {
            template = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return parseDescriptor(template);
    }

    public static Document parseDescriptor(String template) {
        checkNotNullArgument(template, "template is null");

        Document document = Dom4j.readDocument(template);

        replaceAssignParameters(document);

        return document;
    }

    protected static void replaceAssignParameters(Document document) {
        Map<String, String> assignedParams = new HashMap<>();

        List<Element> assignElements = Dom4j.elements(document.getRootElement(), "assign");
        ThemeConstantsManager themeManager = AppBeans.get(ThemeConstantsManager.NAME);
        for (Element assignElement : assignElements) {
            String name = assignElement.attributeValue("name");
            if (StringUtils.isEmpty(name)) {
                throw new RuntimeException("'name' attribute of assign tag is empty");
            }

            String value = assignElement.attributeValue("value");
            if (StringUtils.isEmpty(value)) {
                throw new RuntimeException("'value' attribute of assign tag is empty");
            }

            if (StringUtils.startsWith(value, ThemeConstants.PREFIX)) {
                ThemeConstants theme = themeManager.getConstants();
                value = theme.get(value.substring(ThemeConstants.PREFIX.length()));
            }

            assignedParams.put(name, value);
        }

        if (!assignedParams.isEmpty()) {
            Element layoutElement = document.getRootElement().element("layout");
            if (layoutElement != null) {
                Dom4j.walkAttributesRecursive(layoutElement, (element, attribute) -> {
                    String attributeValue = attribute.getValue();
                    if (StringUtils.isNotEmpty(attributeValue)
                            && attributeValue.startsWith("${")
                            && attributeValue.endsWith("}")) {
                        String paramKey = attributeValue.substring(2, attributeValue.length() - 1);

                        String assignedValue = assignedParams.get(paramKey);
                        if (assignedValue == null) {
                            throw new RuntimeException("Unable to find value of assign param: " + paramKey);
                        }

                        attribute.setValue(assignedValue);
                    }
                });
            }
        }
    }

    public LayoutLoader(ComponentLoader.Context context, ComponentsFactory factory, LayoutLoaderConfig config) {
        this.context = context;
        this.factory = factory;
        this.config = config;
    }

    public String getMessagesPack() {
        return messagesPack;
    }

    public void setMessagesPack(String messagesPack) {
        this.messagesPack = messagesPack;
    }

    protected ComponentLoader getLoader(Element element) {
        Class<? extends ComponentLoader> loaderClass = config.getLoader(element.getName());
        if (loaderClass == null) {
            throw new GuiDevelopmentException("Unknown component: " + element.getName(), context.getFullFrameId());
        }

        ComponentLoader loader;
        try {
            Constructor<? extends ComponentLoader> constructor = loaderClass.getConstructor();
            loader = constructor.newInstance();

            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
            loader.setContext(context);
            loader.setLayoutLoaderConfig(config);
            loader.setFactory(factory);
            loader.setElement(element);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new GuiDevelopmentException("Loader instantiation error: " + e, context.getFullFrameId());
        }

        return loader;
    }

    public Pair<ComponentLoader, Element> createFrameComponent(InputStream stream, String id, Map<String, Object> params) {
        StopWatch xmlLoadWatch = new Log4JStopWatch(context.getCurrentFrameId() + "#" +
                UIPerformanceLogger.LifeCycle.XML,
                Logger.getLogger(UIPerformanceLogger.class));

        String template;
        try {
            template = IOUtils.toString(stream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read screen template", e);
        }

        ScreenXmlDocumentCache screenXmlCache = AppBeans.get(ScreenXmlDocumentCache.class);

        Document document = screenXmlCache.get(template);
        if (document == null) {
            Document originalDocument = parseDescriptor(template);

            XmlInheritanceProcessor processor = new XmlInheritanceProcessor(originalDocument, context.getParams());
            Element resultRoot = processor.getResultRoot();

            document = resultRoot.getDocument();

            screenXmlCache.put(template, document);
        }

        Element element = document.getRootElement();

        xmlLoadWatch.stop();

        ComponentLoader loader = getLoader(element);
        FrameLoader frameLoader = (FrameLoader) loader;
        frameLoader.setFrameId(id);

        loader.createComponent();

        return new Pair<>(loader, element);
    }

    public ComponentLoader createComponent(Element element) {
        ComponentLoader loader = getLoader(element);

        loader.createComponent();
        return loader;
    }

    public ComponentLoader createWindow(Element element, String windowId) {
        ComponentLoader loader = getLoader(element);
        ((WindowLoader) loader).setWindowId(windowId);

        loader.createComponent();
        return loader;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}