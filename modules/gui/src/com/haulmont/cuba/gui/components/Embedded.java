package com.haulmont.cuba.gui.components;

import java.util.Map;
import java.io.InputStream;
import java.io.File;
import java.net.URL;

/**
 * User: Nikolay Gorodnov
 * Date: 22.06.2009
 */
public interface Embedded
        extends Component, Component.Expandable
{
    void setMIMEType(String mt);

    void setSource(URL src);
    void setSource(String src);
    void setSource(String fileName, InputStream src);

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
