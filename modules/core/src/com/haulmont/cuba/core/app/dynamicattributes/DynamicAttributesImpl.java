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
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

/**
 */
@Component(DynamicAttributes.NAME)
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
