/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.dynamicattributes;

import com.google.common.base.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCache;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesService;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.gui.cache.ClientCacheManager;
import com.haulmont.cuba.gui.cache.DynamicAttributesCacheStrategy;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

/**
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean(DynamicAttributesService.NAME)
public class ClientDynamicAttributesServiceBean implements DynamicAttributesService {
    @Inject
    protected ClientCacheManager clientCacheManager;

    @Override
    public Collection<Category> getCategoriesForMetaClass(MetaClass metaClass) {
        return cache().getCategoriesForMetaClass(metaClass);
    }

    @Override
    public Collection<CategoryAttribute> getAttributesForMetaClass(MetaClass metaClass) {
        return cache().getAttributesForMetaClass(metaClass);
    }

    @Nullable
    @Override
    public CategoryAttribute getAttributeForMetaClass(MetaClass metaClass, String code) {
        return cache().getAttributeForMetaClass(metaClass, code);
    }

    protected DynamicAttributesCache cache() {
        DynamicAttributesCache cache = clientCacheManager.getCached(DynamicAttributesCacheStrategy.NAME);
        Preconditions.checkState(cache != null, "Dynamic attributes cache is not available");
        return cache;
    }
}
