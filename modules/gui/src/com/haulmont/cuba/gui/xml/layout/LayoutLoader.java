/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:15:51
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.core.app.TemplateHelper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LayoutLoader {
    protected ComponentLoader.Context context;
    private ComponentsFactory factory;
    private LayoutLoaderConfig config;

    private Locale locale;
    private String messagesPack;

    public LayoutLoader(ComponentLoader.Context context, ComponentsFactory factory, LayoutLoaderConfig config) {
        this.context = context;
        this.factory = factory;
        this.config = config;
    }

    private static final Pattern DS_CONTEXT_PATTERN = Pattern.compile("<dsContext>(\\p{ASCII}+)</dsContext>");

    public static Document parseDescriptor(InputStream stream, Map<String, Object> params) {
        if (stream == null)
            throw new IllegalArgumentException("Input stream is null");
        
        Document document;
        try {
            String template = IOUtils.toString(stream);
            Matcher matcher = DS_CONTEXT_PATTERN.matcher(template);
            if (matcher.find()) {
                final String dsContext = matcher.group(1);

                template = DS_CONTEXT_PATTERN.matcher(template).replaceFirst("");
                template = TemplateHelper.processTemplate(template, params);

                document = loadDocument(template);
                final Document dsContextDocument = loadDocument("<dsContext>" + dsContext + "</dsContext>");
                document.getRootElement().add(dsContextDocument.getRootElement());
            } else {
                template = com.haulmont.cuba.core.app.TemplateHelper.processTemplate(template, params);
                document = loadDocument(template);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return document;
    }

    private static Document loadDocument(String template) {
        Document document;
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(new StringReader(template));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return document;
    }

    public String getMessagesPack() {
        return messagesPack;
    }

    public void setMessagesPack(String messagesPack) {
        this.messagesPack = messagesPack;
    }

    public Component loadComponent(InputStream stream, Component parent) {
        try {
            SAXReader reader = new SAXReader();
            Document doc;
            try {
                doc = reader.read(stream);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }

            Element element = doc.getRootElement();

            return loadComponent(element, parent);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Component loadComponent(InputStream stream, Component parent, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return loadComponent(stream, parent);
        }
        try {
            Document doc = parseDescriptor(stream, params);
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

