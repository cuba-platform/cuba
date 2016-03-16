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

import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;

import org.springframework.stereotype.Component;

/**
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
