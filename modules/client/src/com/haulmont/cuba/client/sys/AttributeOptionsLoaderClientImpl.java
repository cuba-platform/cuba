/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.app.dynamicattributes.AttributeOptionsLoader;
import com.haulmont.cuba.core.app.dynamicattributes.AttributeOptionsLoaderService;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component(AttributeOptionsLoader.NAME)
public class AttributeOptionsLoaderClientImpl implements AttributeOptionsLoader {
    @Inject
    protected AttributeOptionsLoaderService optionsLoaderService;

    @Override
    public List loadOptions(BaseGenericIdEntity entity, CategoryAttribute attribute) {
        return optionsLoaderService.loadOptions(entity, attribute);
    }
}
