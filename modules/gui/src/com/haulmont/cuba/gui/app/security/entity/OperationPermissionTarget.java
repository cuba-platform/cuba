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

package com.haulmont.cuba.gui.app.security.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

/**
 *
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
