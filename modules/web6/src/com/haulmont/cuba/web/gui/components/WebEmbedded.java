/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.terminal.*;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebEmbedded
        extends WebAbstractComponent<com.vaadin.ui.Embedded>
        implements Embedded, Component.Disposable {

    private Map<String, String> parameters = null;
    private Type type = Type.OBJECT;
    private ApplicationResource resource;
    private boolean disposed;
    private boolean doNotSetSize = false;

    public WebEmbedded() {
        component = new com.vaadin.ui.Embedded() {
            @Override
            public void paintContent(PaintTarget target) throws PaintException {
                super.paintContent(target);
                if (doNotSetSize) {
                    target.addAttribute("doNotSetSize", doNotSetSize);
                }
            }
        };
        provideType();
    }

    @Override
    public void setSource(@Nullable URL src) {
        if (src != null) {
            component.setSource(new ExternalResource(src));
            setType(Type.BROWSER);
        } else {
            resetSource();
        }
    }

    @Override
    public void setSource(@Nullable String src) {
        if (src != null) {
            if (src.startsWith("http") || src.startsWith("https")) {
                try {
                    setSource(new URL(src));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                File file = new File(src);
                if (!file.isAbsolute()) {
                    String root = ConfigProvider.getConfig(WebConfig.class).getResourcesRoot();
                    if (root != null) {
                        if (!root.endsWith(File.separator)) {
                            root += File.separator;
                        }
                        file = new File(root + file.getName());
                    }
                }

                resource = new FileResource(file, App.getInstance());
                component.setSource(resource);
            }
        } else {
            resetSource();
        }
    }

    @Override
    public void setSource(String fileName,@Nullable final InputStream src) {
        if (src != null) {
            final StreamResource.StreamSource source = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    return src;
                }
            };

            resource = new StreamResource(source, fileName, App.getInstance());
            component.setSource(resource);
        } else {
            resetSource();
        }
    }

    @Override
    public void setSource(String fileName,@Nullable ExportDataProvider dataProvider) {
        if (dataProvider != null) {
            resource = new WebEmbeddedApplicationResource(dataProvider, fileName, App.getInstance());
            component.setSource(resource);
        } else {
            resetSource();
        }
    }


    @Override
    public void resetSource() {
        resource = null;
        component.setType(com.vaadin.ui.Embedded.TYPE_IMAGE);
        component.setMimeType("image/png");
        component.setSource(new StreamResource(new EmptyStreamSource(), UUID.randomUUID() + ".png", App.getInstance()));
        component.requestRepaint();
    }

    public boolean isDoNotSetSize() {
        return doNotSetSize;
    }

    /**
     * Ability to disable auto size setting
     */
    public void setDoNotSetSize(boolean doNotSetSize) {
        this.doNotSetSize = doNotSetSize;
        component.requestRepaint();
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
        if (resource != null) {
            App.getInstance().removeResource(resource);
        }
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    private static class EmptyStreamSource implements StreamResource.StreamSource {

        private byte[] emptyImage;

        @Override
        public InputStream getStream() {
            if (emptyImage == null) {
                InputStream stream =
                        getClass().getResourceAsStream("/com/haulmont/cuba/web/gui/components/resources/empty.png");
                try {
                    emptyImage = IOUtils.toByteArray(stream);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to read empty.png from classpath", e);
                }
            }

            return new ByteArrayInputStream(emptyImage);
        }
    }
}
