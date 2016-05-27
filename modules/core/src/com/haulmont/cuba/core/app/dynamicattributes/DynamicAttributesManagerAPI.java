/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    @Nullable
    DynamicAttributesCache getCacheIfNewer(Date clientCacheDate);

    /**
     *  Fetch dynamic attributes from database for each entity
     */
    <E extends BaseGenericIdEntity> void fetchDynamicAttributes(List<E> entities);

    /**
     * Store dynamic attributes from the entity to database
     */
    void storeDynamicAttributes(BaseGenericIdEntity entity);
}