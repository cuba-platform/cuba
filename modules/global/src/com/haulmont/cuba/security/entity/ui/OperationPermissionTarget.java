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

import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$OperationTarget")
@SystemLevel
public class OperationPermissionTarget extends AbstractInstance
        implements Entity<String>, Cloneable, AssignableTarget {

    private UUID uuid = UuidProvider.createUuid();

    @MetaProperty(mandatory = true)
    private String id;

    @MetaProperty(mandatory = true)
    private String caption;

    @MetaProperty(mandatory = true)
    private String permissionValue;

    @MetaProperty(mandatory = true)
    private PermissionVariant createPermissionVariant = PermissionVariant.NOTSET;

    @MetaProperty(mandatory = true)
    private PermissionVariant readPermissionVariant = PermissionVariant.NOTSET;

    @MetaProperty(mandatory = true)
    private PermissionVariant updatePermissionVariant = PermissionVariant.NOTSET;

    @MetaProperty(mandatory = true)
    private PermissionVariant deletePermissionVariant = PermissionVariant.NOTSET;

    private Class entityClass;

    public OperationPermissionTarget(Class entityClass, String id, String caption, String permissionValue) {
        this.id = id;
        this.caption = caption;
        this.permissionValue = permissionValue;
        this.entityClass = entityClass;
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
    @MetaProperty
    public boolean isAssigned() {
        return (createPermissionVariant != PermissionVariant.NOTSET) ||
                (readPermissionVariant != PermissionVariant.NOTSET) ||
                (updatePermissionVariant != PermissionVariant.NOTSET) ||
                (deletePermissionVariant != PermissionVariant.NOTSET);
    }

    @MetaProperty
    public boolean isAllowedAll() {
        return (createPermissionVariant == PermissionVariant.ALLOWED) &&
                (readPermissionVariant == PermissionVariant.ALLOWED) &&
                (updatePermissionVariant == PermissionVariant.ALLOWED) &&
                (deletePermissionVariant == PermissionVariant.ALLOWED);
    }

    @MetaProperty
    public boolean isDeniedAll() {
        return (createPermissionVariant == PermissionVariant.DISALLOWED) &&
                (readPermissionVariant == PermissionVariant.DISALLOWED) &&
                (updatePermissionVariant == PermissionVariant.DISALLOWED) &&
                (deletePermissionVariant == PermissionVariant.DISALLOWED);
    }

    public String getCaption() {
        return caption;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    @Override
    public String getPermissionValue() {
        return permissionValue;
    }

    public PermissionVariant getCreatePermissionVariant() {
        return createPermissionVariant;
    }

    public void setCreatePermissionVariant(PermissionVariant createPermissionVariant) {
        this.createPermissionVariant = createPermissionVariant;
    }

    public PermissionVariant getReadPermissionVariant() {
        return readPermissionVariant;
    }

    public void setReadPermissionVariant(PermissionVariant readPermissionVariant) {
        this.readPermissionVariant = readPermissionVariant;
    }

    public PermissionVariant getUpdatePermissionVariant() {
        return updatePermissionVariant;
    }

    public void setUpdatePermissionVariant(PermissionVariant updatePermissionVariant) {
        this.updatePermissionVariant = updatePermissionVariant;
    }

    public PermissionVariant getDeletePermissionVariant() {
        return deletePermissionVariant;
    }

    public void setDeletePermissionVariant(PermissionVariant deletePermissionVariant) {
        this.deletePermissionVariant = deletePermissionVariant;
    }

    @Override
    public String toString() {
        return caption;
    }

    @Override
    public OperationPermissionTarget clone() throws CloneNotSupportedException {
        OperationPermissionTarget target = (OperationPermissionTarget) super.clone();

        target.id = id;
        target.caption = caption;
        target.permissionValue = permissionValue;
        target.createPermissionVariant = createPermissionVariant;
        target.readPermissionVariant = readPermissionVariant;
        target.updatePermissionVariant = updatePermissionVariant;
        target.deletePermissionVariant = deletePermissionVariant;

        return target;
    }
}
