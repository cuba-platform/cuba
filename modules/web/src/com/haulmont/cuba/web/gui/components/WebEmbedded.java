/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.export.ClosedDataProviderException;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebEmbedded
        extends WebAbstractComponent<com.vaadin.ui.Embedded>
        implements Embedded, Component.Disposable
{
    private Map<String, String> parameters = null;
    private Type type = Type.OBJECT;
    private ConnectorResource resource;
    private boolean disposed;

    public WebEmbedded() {
        component = new com.vaadin.ui.Embedded();
        provideType();
    }

    @Override
    public void setSource(URL src) {
        component.setSource(new ExternalResource(src));
        setType(Type.BROWSER);
    }

    @Override
    public void setSource(String src) {
        if (src.startsWith("http") || src.startsWith("https")) {
            try {
                setSource(new URL(src));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            File file = new File(src);
            if (!file.isAbsolute()) {
                Configuration configuration = AppBeans.get(Configuration.NAME);
                String root = configuration.getConfig(WebConfig.class).getResourcesRoot();
                if (root != null) {
                    if (!root.endsWith(File.separator)) {
                        root += File.separator;
                    }
                    file = new File(root + file.getName());
                }
            }

            resource = new FileResource(file);
            component.setSource(resource);
        }
    }

    @Override
    public void setSource(String fileName, final InputStream src) {

        final StreamResource.StreamSource source = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                return src;
            }
        };

        resource = new StreamResource(source, fileName);
        component.setSource(resource);
    }

    @Override
    public void setSource(String fileName, final ExportDataProvider dataProvider) {
        StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                try {
                    return dataProvider.provide();
                } catch (ClosedDataProviderException e) {
                    // todo log
                    return null;
                }
            }
        };

        resource = new StreamResource(streamSource, fileName);
        component.setSource(resource);
    }

    @Override
    public void setMIMEType(String mt) {
        component.setMimeType(mt);
    }

    @Override
    public void addParameter(String name, String value) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        component.setParameter(name, value);
        parameters.put(name, value);
    }

    @Override
    public void removeParameter(String name) {
        component.removeParameter(name);
        if (parameters != null) {
            parameters.remove(name);
        }
    }

    @Override
    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public void setType(Type t) {
        type = t;
        provideType();
    }

    @Override
    public Type getType() {
        return type;
    }

    protected void provideType() {
        switch (type) {
            case OBJECT:
                component.setType(com.vaadin.ui.Embedded.TYPE_OBJECT);
                break;
            case IMAGE:
                component.setType(com.vaadin.ui.Embedded.TYPE_IMAGE);
                break;
            case BROWSER:
                component.setType(com.vaadin.ui.Embedded.TYPE_BROWSER);
                break;
        }
    }

    @Override
    public void dispose() {
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }
}