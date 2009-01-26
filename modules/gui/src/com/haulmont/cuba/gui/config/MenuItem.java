/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 16:09:59
 *
 * $Id$
 */
package com.haulmont.cuba.gui.config;

import org.dom4j.Element;

import java.util.List;
import java.util.ArrayList;

public class MenuItem
{
    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<MenuItem>();

    private String caption;

    private Element descriptor;

    public MenuItem(MenuItem parent, String caption) {
        this.parent = parent;
        this.caption = caption;
    }

    public MenuItem getParent() {
        return parent;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public String getCaption() {
        return caption;
    }

    public String toString() {
        return caption;
    }

    public Element getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Element descriptor) {
        this.descriptor = descriptor;
    }
}
