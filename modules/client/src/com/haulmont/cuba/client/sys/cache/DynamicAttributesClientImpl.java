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

package com.haulmont.cuba.client.sys.cache;

import com.google.common.base.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCache;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

@Component(DynamicAttributes.NAME)
public class DynamicAttributesClientImpl implements DynamicAttributes {
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