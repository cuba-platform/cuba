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
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UuidProvider;

import java.util.UUID;

@com.haulmont.chile.core.annotations.MetaClass(name = "sec$AbstractTarget")
@SystemLevel
public class AbstractPermissionTarget extends AbstractInstance
        implements Entity<String> {

    @MetaProperty(mandatory = true)
    protected String id;

    @MetaProperty(mandatory = true)
    protected String caption;

    @MetaProperty(mandatory = true)
    protected String permissionValue;

    private UUID uuid = UuidProvider.createUuid();

    public AbstractPermissionTarget(String id, String caption) {
        this.id = id;
        this.caption = caption;
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
        return AppBeans.get(Metadata.class).getSession().getClass(getClass());
    }

    public String getCaption() {
        return caption;
    }

    @Override
    public String toString() {
        return caption;
    }

    public String getPermissionValue() {
        return permissionValue;
    }
}