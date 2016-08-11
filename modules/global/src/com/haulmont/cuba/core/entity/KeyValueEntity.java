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

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.sys.CubaEnhancingDisabled;
import org.apache.commons.lang.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Entity that contains a variable set of attributes. For example:
 * <pre>
 * KeyValueEntity company = new KeyValueEntity();
 * company.setValue("email", "info@globex.com");
 * company.setValue("name", "Globex Corporation");
 *
 * KeyValueEntity person = new KeyValueEntity();
 * person.setValue("email", "homer.simpson@mail.com");
 * person.setValue("firstName", "Homer");
 * person.setValue("lastName", "Simpson");
 * </pre>
 *
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sys$KeyValueEntity")
@SystemLevel
public class KeyValueEntity extends AbstractNotPersistentEntity implements CubaEnhancingDisabled {

    private Map<String, Object> properties = new LinkedHashMap<>();

    private MetaClass metaClass;

    @Override
    public MetaClass getMetaClass() {
        if (metaClass == null)
            throw new IllegalStateException("metaClass is null");
        return metaClass;
    }

    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    @Override
    public <T> T getValue(String name) {
        //noinspection unchecked
        return (T) properties.get(name);
    }

    @Override
    public void setValue(String name, Object value, boolean checkEquals) {
        Object oldValue = getValue(name);
        if ((!checkEquals) || (!ObjectUtils.equals(oldValue, value))) {
            properties.put(name, value);
            propertyChanged(name, oldValue, value);
        }
    }
}
