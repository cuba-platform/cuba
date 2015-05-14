/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Service(DynamicAttributesCacheService.NAME)
public class DynamicAttributesCacheServiceBean implements DynamicAttributesCacheService {
    @Inject
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    @Override
    public void loadCache(){
        dynamicAttributesManagerAPI.loadCache();
    }

    @Override
    public DynamicAttributesCache getCacheIfNewer(Date clientCacheDate) {
        return dynamicAttributesManagerAPI.getCacheIfNewer(clientCacheDate);
    }
}
