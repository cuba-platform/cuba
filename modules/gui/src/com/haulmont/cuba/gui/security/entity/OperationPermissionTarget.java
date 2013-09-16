/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.security.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$OperationTarget")
@SystemLevel
public class OperationPermissionTarget extends AbstractPermissionTarget
        implements Cloneable, EntityPermissionTarget {

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
        super(id, caption);
        this.permissionValue = permissionValue;
        this.entityClass = entityClass;
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

    @Override
    public Class getEntityClass() {
        return entityClass;
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
