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
 * Non-persistent entity to show permission targets in UI
 *
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$Target")
@SystemLevel
public class BasicPermissionTarget
        extends AbstractPermissionTarget implements Cloneable, AssignableTarget {

    @MetaProperty(mandatory = true)
    private PermissionVariant permissionVariant = PermissionVariant.NOTSET;

    public BasicPermissionTarget(String id, String caption, String permissionValue) {
        this(id, caption, permissionValue, PermissionVariant.NOTSET);
    }

    public BasicPermissionTarget(String id, String caption, String permissionValue, PermissionVariant permissionVariant) {
        super(id, caption);
        this.permissionValue = permissionValue;
        this.permissionVariant = permissionVariant;
    }

    @Override
    public boolean isAssigned() {
        return permissionVariant != PermissionVariant.NOTSET;
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