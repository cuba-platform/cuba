/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TemplateHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author abramov
 * @version $Id$
 */
public class LayoutLoader {
    protected ComponentLoader.Context context;
    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    protected Locale locale;
    protected String messagesPack;

    public static final Pattern COMMENT_PATTERN = Pattern.compile("<!--.*?-->", Pattern.DOTALL);

    public static final Pattern ASSIGN_PATTERN = Pattern.compile("<assign\\s+name\\s*=\\s*\"(.+)\"\\s+value\\s*=\\s*\"(.+)\"\\s*");

    public static final Pattern DS_CONTEXT_PATTERN = Pattern.compile("<dsContext>.+?</dsContext>", Pattern.DOTALL);

    public static Document parseDescriptor(InputStream stream, Map<String, Object> params) {
        if (stream == null)
            throw new IllegalArgumentException("Input stream is null");

        Document document;
        try {
            String template = IOUtils.toString(stream, StandardCharsets.UTF_8);

            Matcher matcher = COMMENT_PATTERN.matcher(template);
            template = matcher.replaceAll("");

            Map<String, Object> templateParams = new HashMap<>(params);

            matcher = ASSIGN_PATTERN.matcher(template);
            while (matcher.find()) {
                String variable = matcher.group(1);
                String value = matcher.group(2);

                if (StringUtils.startsWith(value, ThemeConstants.PREFIX)) {
                    ThemeConstantsManager themeManager = AppBeans.get(ThemeConstantsManager.NAME);
                    ThemeConstants theme = themeManager.getConstants();
                    value = theme.get(value.substring(ThemeConstants.PREFIX.length()));
                }

                templateParams.put(variable, value);
            }

            matcher = DS_CONTEXT_PATTERN.matcher(template);
            if (matcher.find()) {
                Document dsContextDocument = Dom4j.readDocument(matcher.group());
                // dsContext queries may have their own templates which are processed later
                template = matcher.replaceFirst("");
                template = TemplateHelper.processTemplate(template, templateParams);
                document = Dom4j.readDocument(template);
                document.getRootElement().add(dsContextDocument.getRootElement());
            } else {
                template = TemplateHelper.processTemplate(template, templateParams);
                document = Dom4j.readDocument(template);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return document;
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
            throw new GuiDevelopmentException("Loader instatiation error: " + e, context.getFullFrameId());
        }

        return loader;
    }

    public Pair<ComponentLoader, Element> createFrameComponent(InputStream stream, Map<String, Object> params) {
        StopWatch xmlLoadWatch = new Log4JStopWatch(context.getCurrentFrameId() + "#" +
                UIPerformanceLogger.LifeCycle.XML,
                Logger.getLogger(UIPerformanceLogger.class));

        Document doc = parseDescriptor(stream, params == null ? Collections.<String, Object>emptyMap() : params);
        Element element = doc.getRootElement();
        xmlLoadWatch.stop();

        return new Pair<>(createComponent(element), element);
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