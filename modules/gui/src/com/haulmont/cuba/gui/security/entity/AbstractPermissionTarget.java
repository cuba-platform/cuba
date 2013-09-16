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
        return MetadataProvider.getSession().getClass(getClass());
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
