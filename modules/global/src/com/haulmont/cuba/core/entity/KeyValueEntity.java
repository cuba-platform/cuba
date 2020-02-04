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

import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.sys.CubaEnhancingDisabled;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
public class KeyValueEntity
        extends AbstractInstance
        implements Entity<Object>, CubaEnhancingDisabled {

    protected UUID uuid;

    protected Map<String, Object> properties = new LinkedHashMap<>();

    protected String idName;

    protected MetaClass metaClass;

    public KeyValueEntity() {
        uuid = UuidProvider.createUuid();
    }

    @Override
    public MetaClass getMetaClass() {
        if (metaClass == null)
            throw new IllegalStateException("metaClass is null");
        return metaClass;
    }

    /**
     * Sets a meta-class for this entity instance.
     */
    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    /**
     * @return  name of a property that represents this entity id, if set by {@link #setIdName(String)}
     */
    public String getIdName() {
        return idName;
    }

    /**
     * Sets the name of a property that represents this entity id.
     */
    public void setIdName(String idName) {
        this.idName = idName;
    }

    @Override
    public <T> T getValue(String name) {
        if (metaClass != null) {
            MetaProperty metaProperty = metaClass.getProperty(name);
            if (metaProperty != null && metaProperty.getRange().isEnum()) {
                Enumeration enumeration = metaProperty.getRange().asEnumeration();
                Object enumValue = properties.get(name);
                if (enumValue != null) {
                    if (enumValue instanceof EnumClass) {
                        //noinspection unchecked
                        return (T) enumValue;
                    } else {
                        try {
                            //noinspection unchecked
                            return (T) enumeration.parse(enumValue.toString());
                        } catch (ParseException e) {
                            throw new RuntimeException("Can't parse enum value stored in the database", e);
                        }
                    }
                }
            }
        }
        //noinspection unchecked
        return (T) properties.get(name);
    }

    @Override
    public void setValue(String name, Object value, boolean checkEquals) {
        Object oldValue = getValue(name);
        if ((!checkEquals) || (!InstanceUtils.propertyValueEquals(oldValue, value))) {
            properties.put(name, value);
            propertyChanged(name, oldValue, value);
        }
    }

    @Override
    public Object getId() {
        if (idName == null)
            return uuid;
        else
            return properties.get(idName);
    }

    public void setId(Object id) {
        if (idName == null)
            throw new IllegalStateException("Id name is not set");
        properties.put(idName, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyValueEntity that = (KeyValueEntity) o;
        Object id = getId();
        Object thatId = that.getId();

        if (id != null && thatId != null)
            return id.equals(thatId);

        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        Object id = getId();
        if (id != null)
            return id.hashCode();
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        Object id = null;
        if (idName != null)
            id = properties.get(idName);
        if (id == null)
            id = "?(" + uuid + ")";
        return "sys$KeyValueEntity-" + id;
    }
}