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
import org.apache.commons.collections.CollectionUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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

    @MetaProperty(mandatory = true)
    private List<AttributeTarget> permissions = new LinkedList<AttributeTarget>();

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
        while (iterator.hasNext() && i < 5) {
            AttributeTarget attributeTarget = iterator.next();
            if (attributeTarget.getPermissionVariant() != AttributePermissionVariant.NOTSET) {
                if (i < 4) {
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
        clone.permissions = new LinkedList<AttributeTarget>();
        for (AttributeTarget target : permissions) {
            AttributeTarget cloneTarget = new AttributeTarget(target.getId());
            cloneTarget.setPermissionVariant(target.getPermissionVariant());
            clone.getPermissions().add(cloneTarget);
        }
        return clone;
    }
}