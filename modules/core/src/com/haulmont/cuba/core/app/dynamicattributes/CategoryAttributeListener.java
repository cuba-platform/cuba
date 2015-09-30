/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;

import org.springframework.stereotype.Component;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Component("report_CategoryAttributeListener")
public class CategoryAttributeListener
        implements BeforeInsertEntityListener<CategoryAttribute>, BeforeUpdateEntityListener<CategoryAttribute> {
    @Override
    public void onBeforeInsert(CategoryAttribute entity) {
        setCategoryEntityType(entity);
    }

    @Override
    public void onBeforeUpdate(CategoryAttribute entity) {
        setCategoryEntityType(entity);
    }

    protected void setCategoryEntityType(CategoryAttribute entity) {
        if (entity.getCategory() != null) {
            entity.setCategoryEntityType(entity.getCategory().getEntityType());
        }
    }
}
