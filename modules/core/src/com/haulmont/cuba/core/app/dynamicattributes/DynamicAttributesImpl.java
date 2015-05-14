/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

/**
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean(DynamicAttributes.NAME)
public class DynamicAttributesImpl implements DynamicAttributes {
    @Inject
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    @Override
    public Collection<Category> getCategoriesForMetaClass(MetaClass metaClass) {
        return dynamicAttributesManagerAPI.getCategoriesForMetaClass(metaClass);
    }

    @Override
    public Collection<CategoryAttribute> getAttributesForMetaClass(MetaClass metaClass){
        return dynamicAttributesManagerAPI.getAttributesForMetaClass(metaClass);
    }

    @Nullable
    @Override
    public CategoryAttribute getAttributeForMetaClass(MetaClass metaClass, String code) {
        return dynamicAttributesManagerAPI.getAttributeForMetaClass(metaClass, code);
    }
}
