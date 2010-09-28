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

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.haulmont.cuba.security.entity.PermissionType;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.gui.AppConfig;

/**
 * Main menu item descriptor
 */
public class MenuItem implements Serializable
{
    private static final long serialVersionUID = -8504408057097836101L;

    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<MenuItem>();

    private String id;
    private Element descriptor;

    private ShortcutAction.KeyCombination shortcut;

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
            boolean screenPermitted = session.isScreenPermitted(AppConfig.getInstance().getClientType(), id);
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

    public ShortcutAction.KeyCombination getShortcut() {
        return shortcut;
    }

    public void setShortcut(ShortcutAction.KeyCombination shortcut) {
        this.shortcut = shortcut;
    }
}
