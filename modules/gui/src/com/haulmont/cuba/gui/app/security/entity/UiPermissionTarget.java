/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$UiTarget")
@SystemLevel
public class UiPermissionTarget extends AbstractPermissionTarget
        implements AssignableTarget {

    @MetaProperty(mandatory = true)
    private UiPermissionVariant permissionVariant = UiPermissionVariant.NOTSET;

    @MetaProperty(mandatory = true)
    private String screen;

    @MetaProperty(mandatory = true)
    private String component;

    public UiPermissionTarget(String id, String caption, String permissionValue) {
        super(id, caption);
        this.permissionValue = permissionValue;
    }

    public UiPermissionTarget(String id, String caption, String permissionValue, UiPermissionVariant permissionVariant) {
        super(id, caption);
        this.permissionValue = permissionValue;
        this.permissionVariant = permissionVariant;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean isAssigned() {
        return permissionVariant != UiPermissionVariant.NOTSET;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public UiPermissionVariant getPermissionVariant() {
        return permissionVariant;
    }

    public void setPermissionVariant(UiPermissionVariant permissionVariant) {
        this.permissionVariant = permissionVariant;
    }
}