/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.export.ExportDataProvider;

import java.util.Map;
import java.io.InputStream;
import java.io.File;
import java.net.URL;

/**
 * A component for embedding external objects, such as images or PDF documents.
 * <p>
 * Web implementation may require a browser plugin. Only images support is mandatory for all implementations.
 *
 * @author gorodnov
 * @version $Id$
 */
public interface Embedded
        extends Component, Component.BelongToFrame
{
    String NAME = "embedded";

    void setMIMEType(String mt);

    void setSource(URL src);
    void setSource(String src);
    void setSource(String fileName, InputStream src);
    void setSource(String fileName, ExportDataProvider dataProvider);

    void addParameter(String name, String value);
    void removeParameter(String name);
    Map<String, String> getParameters();

    void setType(Type t);
    Type getType();

    enum Type {
        OBJECT,
        IMAGE,
        BROWSER
    }
}