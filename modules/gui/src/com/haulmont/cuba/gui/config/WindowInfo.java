/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.core.global.ScriptingProvider;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

/**
 * Screen information object
 */
public class WindowInfo {

    private String id;
    private Element descriptor;
    private Class screenClass;

    public WindowInfo(String id, Element descriptor) {
        this.id = id;
        this.descriptor = descriptor;
    }

    /**
     * Screen ID as set up in <code>screen-config.xml</code>
     */
    public String getId() {
        return id;
    }

    /**
     * Screen template path as set up in <code>screen-config.xml</code>
     */
    public String getTemplate() {
        return descriptor.attributeValue("template");
    }

    /**
     * Screen class as set up in <code>screen-config.xml</code>
     */
    public Class getScreenClass() {
        if (screenClass == null) {
            String className = descriptor.attributeValue("class");
            if (className != null)
                screenClass = ScriptingProvider.loadClass(className);
        }

        return screenClass;
    }

    public boolean getMultipleOpen() {
        return BooleanUtils.toBoolean(descriptor.attributeValue("multipleOpen"));
    }

    /**
     * The whole XML element of the screen as set up in <code>screen-config.xml</code>
     */
    public Element getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Element descriptor) {
        this.descriptor = descriptor;
    }

    public String toString() {
        String template = getTemplate();
        return "id='" + id + '\'' +
                (template != null ? ", template=" + template : "") +
                (screenClass != null ? ", screenClass=" + screenClass : "");
    }
}
