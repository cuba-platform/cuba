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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.collections.CollectionUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$MultipleTarget")
@SystemLevel
public class MultiplePermissionTarget extends AbstractPermissionTarget
        implements EntityPermissionTarget, Cloneable {

    public static final int SHOW_PERMISSIONS_COUNT = 8;

    @MetaProperty(mandatory = true)
    private List<AttributeTarget> permissions = new LinkedList<>();
    private Class entityClass;

    @MetaProperty(mandatory = true)
    protected String localName;

    @MetaProperty(mandatory = true)
    protected String metaClassName;

    public MultiplePermissionTarget(Class entityClass, String id, String caption, String permissionValue) {
        super(id, caption);
        Metadata metadata = AppBeans.get(Metadata.class);
        Messages messages = AppBeans.get(Messages.class);

        this.localName = messages.getTools().getEntityCaption(metadata.getClassNN(entityClass));
        this.metaClassName = metadata.getClassNN(entityClass).getName();
        this.entityClass = entityClass;
        this.caption = caption;
        this.permissionValue = permissionValue;
    }

    public String getLocalName() {
        return localName;
    }

    public String getMetaClassName() {
        return metaClassName;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }

    public List<AttributeTarget> getPermissions() {
        return permissions;
    }

    public AttributePermissionVariant getPermissionVariant(final String attribute) {
        AttributeTarget attrTarget = (AttributeTarget) CollectionUtils.find(getPermissions(),
                new org.apache.commons.collections.Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        @SuppressWarnings("unchecked")
                        AttributeTarget variantPair = (AttributeTarget) object;
                        return variantPair != null && attribute.equals(variantPair.getId());
                    }
                });
        if (attrTarget != null)
            return attrTarget.getPermissionVariant();
        else
            return null;
    }

    public void assignPermissionVariant(final String attribute, AttributePermissionVariant permissionVariant) {
        AttributeTarget attrTarget = (AttributeTarget) CollectionUtils.find(getPermissions(),
                new org.apache.commons.collections.Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        @SuppressWarnings("unchecked")
                        AttributeTarget variantPair = (AttributeTarget) object;
                        return variantPair != null && attribute.equals(variantPair.getId());
                    }
                });

        if (attrTarget != null) {
            attrTarget.setPermissionVariant(permissionVariant);
        }
    }

    @MetaProperty
    public String getPermissionsInfo() {
        int i = 0;
        StringBuilder builder = new StringBuilder();
        Iterator<AttributeTarget> iterator = permissions.iterator();
        while (iterator.hasNext() && i < SHOW_PERMISSIONS_COUNT) {
            AttributeTarget attributeTarget = iterator.next();
            if (attributeTarget.getPermissionVariant() != AttributePermissionVariant.NOTSET) {
                if (i < SHOW_PERMISSIONS_COUNT - 1) {
                    if (i > 0)
                        builder.append(", ");
                    builder.append(attributeTarget.getId());
                } else {
                    builder.append(", ..");
                }
                i++;
            }
        }

        return builder.toString();
    }

    @Override
    @MetaProperty
    public boolean isAssigned() {
        for (AttributeTarget target : permissions) {
            if (target.getPermissionVariant() != AttributePermissionVariant.NOTSET)
                return true;
        }
        return false;
    }

    public boolean isAllModified() {
        for (AttributeTarget target : permissions) {
            if (target.getPermissionVariant() != AttributePermissionVariant.MODIFY)
                return false;
        }
        return true;
    }

    public boolean isAllReadOnly() {
        for (AttributeTarget target : permissions) {
            if (target.getPermissionVariant() != AttributePermissionVariant.READ_ONLY)
                return false;
        }
        return true;
    }

    public boolean isAllHide() {
        for (AttributeTarget target : permissions) {
            if (target.getPermissionVariant() != AttributePermissionVariant.HIDE)
                return false;
        }
        return true;
    }

    @Override
    public MultiplePermissionTarget clone() throws CloneNotSupportedException {
        MultiplePermissionTarget clone = (MultiplePermissionTarget) super.clone();
        clone.id = id;
        clone.caption = caption;
        clone.permissionValue = permissionValue;
        clone.permissions = new LinkedList<>();
        for (AttributeTarget target : permissions) {
            AttributeTarget cloneTarget = new AttributeTarget(target.getId());
            cloneTarget.setPermissionVariant(target.getPermissionVariant());
            clone.getPermissions().add(cloneTarget);
        }
        return clone;
    }
}