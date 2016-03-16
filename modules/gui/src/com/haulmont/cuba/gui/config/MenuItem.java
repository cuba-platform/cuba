/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
 *
 */
public class MenuItem {

    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<>();

    private String id;
    private Element descriptor;
    private boolean separator = false;

    private KeyCombination shortcut;
    private boolean isMenu = false;

    public MenuItem(MenuItem parent, String id) {
        this.parent = parent;
        this.id = id;
    }

    public boolean isMenu() {
        return isMenu;
    }

    public void setMenu(boolean isMenu) {
        this.isMenu = isMenu;
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
