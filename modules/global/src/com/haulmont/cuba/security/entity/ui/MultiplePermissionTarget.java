/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.entity.ui;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.UuidProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$MultipleTarget")
@SystemLevel
public class MultiplePermissionTarget extends AbstractInstance
        implements Entity<String>, AssignableTarget, Cloneable {

    private UUID uuid = UuidProvider.createUuid();

    @MetaProperty(mandatory = true)
    private String id;

    @MetaProperty(mandatory = true)
    private String caption;

    @MetaProperty(mandatory = true)
    private String permissionValue;

    private Map<String, AttributePermissionVariant> permissions = new HashMap<String, AttributePermissionVariant>();

    public MultiplePermissionTarget(String id, String caption, String permissionValue) {
        this.id = id;
        this.caption = caption;
        this.permissionValue = permissionValue;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(getClass());
    }

    @Override
    public String toString() {
        return caption;
    }

    @Override
    public String getPermissionValue() {
        return permissionValue;
    }

    public String getCaption() {
        return caption;
    }

    public Map<String, AttributePermissionVariant> getPermissions() {
        return permissions;
    }

    @MetaProperty
    public String getPermissionsInfo() {
        return "";
    }

    @Override
    @MetaProperty
    public boolean isAssigned() {
        for (Map.Entry<String, AttributePermissionVariant> variantEntry : permissions.entrySet()) {
            if (variantEntry.getValue() != AttributePermissionVariant.NOTSET)
                return true;
        }
        return false;
    }

    @Override
    public MultiplePermissionTarget clone() throws CloneNotSupportedException {
        MultiplePermissionTarget clone = (MultiplePermissionTarget) super.clone();
        clone.id = id;
        clone.caption = caption;
        clone.permissionValue = permissionValue;
        clone.getPermissions().putAll(permissions);
        return clone;
    }
}