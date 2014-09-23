/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

/**
 * Screen's registration information.
 *
 * @see WindowConfig
 *
 * @author krivopustov
 * @version $Id$
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
     * Screen ID as set in <code>screens.xml</code>
     */
    public String getId() {
        return id;
    }

    /**
     * Screen template path as set in <code>screens.xml</code>
     */
    public String getTemplate() {
        return descriptor.attributeValue("template");
    }

    /**
     * Screen class as set in <code>screens.xml</code>
     */
    public Class getScreenClass() {
        if (screenClass == null) {
            String className = descriptor.attributeValue("class");
            if (className != null) {
                Scripting scripting = AppBeans.get(Scripting.NAME);
                screenClass = scripting.loadClass(className);
            }
        }

        return screenClass;
    }

    public boolean getMultipleOpen() {
        return BooleanUtils.toBoolean(descriptor.attributeValue("multipleOpen"));
    }

    /**
     * The whole XML element of the screen as set in <code>screens.xml</code>
     */
    public Element getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Element descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public String toString() {
        String template = getTemplate();
        return "id='" + id + '\'' +
                (template != null ? ", template=" + template : "") +
                (screenClass != null ? ", screenClass=" + screenClass : "");
    }
}