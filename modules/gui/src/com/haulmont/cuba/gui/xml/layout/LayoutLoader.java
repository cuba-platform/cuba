/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.TemplateHelper;
import com.haulmont.cuba.gui.components.Component;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
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
    private ComponentsFactory factory;
    private LayoutLoaderConfig config;

    private Locale locale;
    private String messagesPack;

    public static final Pattern COMMENT_PATTERN = Pattern.compile("<!--.*?-->", Pattern.DOTALL);

    public static final Pattern ASSIGN_PATTERN = Pattern.compile("<assign\\s+name\\s*=\\s*\"(.+)\"\\s+value\\s*=\\s*\"(.+)\"\\s*");

    public static final Pattern DS_CONTEXT_PATTERN = Pattern.compile("<dsContext>.+?</dsContext>", Pattern.DOTALL);

    public static Document parseDescriptor(InputStream stream, Map<String, Object> params) {
        if (stream == null)
            throw new IllegalArgumentException("Input stream is null");

        Document document;
        try {
            String template = IOUtils.toString(stream, "UTF-8");

            Matcher matcher = COMMENT_PATTERN.matcher(template);
            template = matcher.replaceAll("");

            Map<String, Object> templateParams = new HashMap<>(params);

            matcher = ASSIGN_PATTERN.matcher(template);
            while (matcher.find()) {
                templateParams.put(matcher.group(1), matcher.group(2));
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

    public Component loadComponent(InputStream stream, Component parent) {
        Document doc = Dom4j.readDocument(stream);
        Element element = doc.getRootElement();
        return loadComponent(element, parent);
    }

    public Component loadComponent(InputStream stream, Component parent, Map<String, Object> params) {
        try {
            Document doc = parseDescriptor(stream, params == null ? Collections.<String, Object>emptyMap() : params);
            Element element = doc.getRootElement();

            return loadComponent(element, parent);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected ComponentLoader getLoader(Element element) {
        Class<? extends ComponentLoader> loaderClass = config.getLoader(element.getName());
        if (loaderClass == null) {
            throw new IllegalStateException(String.format("Unknown component '%s'", element.getName()));
        }

        ComponentLoader loader;
        try {
            final Constructor<? extends ComponentLoader> constructor =
                    loaderClass.getConstructor(ComponentLoader.Context.class, LayoutLoaderConfig.class, ComponentsFactory.class);
            loader = constructor.newInstance(context, config, factory);

            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        } catch (Throwable e) {
            try {
                final Constructor<? extends ComponentLoader> constructor = loaderClass.getConstructor(ComponentLoader.Context.class);
                loader = constructor.newInstance(context);
                loader.setLocale(locale);
                loader.setMessagesPack(messagesPack);
            } catch (Throwable e1) {
                throw new RuntimeException(e1);
            }
        }

        return loader;
    }

    public <T extends Component> T loadComponent(Element element, Component parent) {
        try {
            ComponentLoader loader = getLoader(element);
            return (T) loader.loadComponent(factory, element, parent);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}

