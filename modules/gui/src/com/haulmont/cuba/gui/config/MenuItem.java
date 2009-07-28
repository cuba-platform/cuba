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
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.ArrayList;

import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.core.global.ClientType;

public class MenuItem
{
    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<MenuItem>();

    private String id;
    private String caption;

    private Element descriptor;

    public MenuItem(MenuItem parent, String id, String caption) {
        this.parent = parent;
        this.id = id;
        this.caption = caption;
    }

    public MenuItem getParent() {
        return parent;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public String getId() {
        return id;
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

    public boolean isPermitted(UserSession session) {
        final String id = descriptor.attributeValue("id");
        if (StringUtils.isEmpty(id)) {
             return true;
        } else {
            PermissionConfig permissionConfig = AppConfig.getInstance().getPermissionConfig();
            return session.isPermitted(PermissionType.SCREEN, permissionConfig.getScreenPermissionTarget(id));
        }
    }
}
