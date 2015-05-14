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
import java.util.Date;

/**
 * @author degtyarjov
 * @version $Id$
 */
public interface DynamicAttributesCacheService {
    String NAME = "cuba_DynamicAttributesCacheService";

    /**
     * Reload dynamic attributes cache from database
     */
    void loadCache();

    @Nullable
    DynamicAttributesCache getCacheIfNewer(Date clientCacheDate);
}
