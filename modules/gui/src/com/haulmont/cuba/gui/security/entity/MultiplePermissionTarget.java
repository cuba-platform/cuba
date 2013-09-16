/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.security.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.collections.CollectionUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$MultipleTarget")
@SystemLevel
public class MultiplePermissionTarget extends AbstractPermissionTarget
        implements EntityPermissionTarget, Cloneable {

    public static final int SHOW_PERMISSIONS_COUNT = 8;

    @MetaProperty(mandatory = true)
    private List<AttributeTarget> permissions = new LinkedList<>();
    private Class entityClass;

    public MultiplePermissionTarget(Class entityClass, String id, String caption, String permissionValue) {
        super(id, caption);
        this.entityClass = entityClass;
        this.caption = caption;
        this.permissionValue = permissionValue;
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