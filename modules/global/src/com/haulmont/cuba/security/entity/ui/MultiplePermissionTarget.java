/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.entity.ui;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$MultipleTarget")
@SystemLevel
public class MultiplePermissionTarget extends AbstractInstance
        implements Entity<String> {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public UUID getUuid() {
        return null;
    }

    @Override
    public MetaClass getMetaClass() {
        return null;
    }
}
