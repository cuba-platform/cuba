/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author degtyarjov
 * @version $Id$
 */
public interface DynamicAttributesManagerAPI {
    String NAME = "cuba_DynamicAttributesManager";

    /**
     * Reload dynamic attributes cache from database
     */
    void loadCache();

    /**
     * Get all categories linked with metaClass from cache
     */
    Collection<Category> getCategoriesForMetaClass(MetaClass metaClass);

    /**
     * Get all categories attributes for metaClass from cache
     */
    Collection<CategoryAttribute> getAttributesForMetaClass(MetaClass metaClass);

    /**
     * Get certain category attribute for metaClass by attribute code
     */
    @Nullable
    CategoryAttribute getAttributeForMetaClass(MetaClass metaClass, String code);
}
