/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.security.entity;

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
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$AttributeTarget")
@SystemLevel
public class AttributeTarget extends AbstractInstance
        implements Entity<String> {

    private UUID uuid = UuidProvider.createUuid();

    @MetaProperty(mandatory = true)
    private String id;

    @MetaProperty(mandatory = true)
    private AttributePermissionVariant permissionVariant = AttributePermissionVariant.NOTSET;

    public AttributeTarget(String id) {
        this.id = id;
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

    public AttributePermissionVariant getPermissionVariant() {
        return permissionVariant;
    }

    public void setPermissionVariant(AttributePermissionVariant permissionVariant) {
        this.permissionVariant = permissionVariant;
    }
}
