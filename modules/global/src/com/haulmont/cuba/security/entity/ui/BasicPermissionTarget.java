/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.entity.ui;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.UuidProvider;

import java.util.UUID;

/**
 * Non-persistent entity to show permission targets in UI
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$Target")
@SystemLevel
public class BasicPermissionTarget
        extends AbstractInstance
        implements Entity<String>, Cloneable {

    @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
    private String id;

    @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
    private String caption;

    @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
    private String permissionValue;

    @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
    private PermissionVariant permissionVariant = PermissionVariant.NOTSET;

    private UUID uuid = UuidProvider.createUuid();

    public BasicPermissionTarget(String id, String caption, String permissionValue) {
        this(id, caption, permissionValue, PermissionVariant.NOTSET);
    }

    public BasicPermissionTarget(String id, String caption, String permissionValue, PermissionVariant permissionVariant) {
        this.caption = caption;
        this.id = id;
        this.permissionValue = permissionValue;
        this.permissionVariant = permissionVariant;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getPermissionValue() {
        return permissionValue;
    }

    @Override
    public String toString() {
        return caption;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(getClass());
    }

    public PermissionVariant getPermissionVariant() {
        return permissionVariant;
    }

    public void setPermissionVariant(PermissionVariant permissionVariant) {
        this.permissionVariant = permissionVariant;
    }

    @Override
    public BasicPermissionTarget clone() throws CloneNotSupportedException {
        BasicPermissionTarget targetClone = (BasicPermissionTarget) super.clone();
        targetClone.caption = caption;
        targetClone.id = id;
        targetClone.permissionValue = permissionValue;
        targetClone.permissionVariant = permissionVariant;

        return targetClone;
    }
}