/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.config;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Main menu item descriptor
 */
public class MenuItem {

    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<MenuItem>();

    private String id;
    private Element descriptor;
    private boolean separator = false;

    private KeyCombination shortcut;

    public MenuItem(MenuItem parent, String id) {
        this.parent = parent;
        this.id = id;
    }

    /** Parent item. Null if this is root item. */
    public MenuItem getParent() {
        return parent;
    }

    /** Children items */
    public List<MenuItem> getChildren() {
        return children;
    }

    /**
     * Menu item ID as defined in <code>menu-config.xml</code>
     */
    public String getId() {
        return id;
    }

    public Element getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Element descriptor) {
        this.descriptor = descriptor;
    }

    public boolean isPermitted(UserSession session) {
        final String id = descriptor.attributeValue("id");
        if (StringUtils.isEmpty(id)) {
             return true;
        } else {
            boolean screenPermitted = session.isScreenPermitted(id);
            if (screenPermitted) {
                Element permissionsElem = descriptor.element("permissions");
                if (permissionsElem != null) {
                    for (Element element : Dom4j.elements(permissionsElem, "permission")) {
                        PermissionType type = PermissionType.valueOf(element.attributeValue("type"));
                        String target = element.attributeValue("target");
                        screenPermitted = session.isPermitted(type, target);
                        if (!screenPermitted)
                            break;
                    }
                }
            }
            return screenPermitted;
        }
    }

    public KeyCombination getShortcut() {
        return shortcut;
    }

    public void setShortcut(KeyCombination shortcut) {
        this.shortcut = shortcut;
    }

    public boolean isSeparator() {
        return separator || "-".equals(id);
    }

    public void setSeparator(boolean separator) {
        this.separator = separator;
    }
}
