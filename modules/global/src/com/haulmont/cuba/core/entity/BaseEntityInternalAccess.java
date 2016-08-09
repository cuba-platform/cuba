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
 */

package com.haulmont.cuba.core.entity;

import com.google.common.collect.Multimap;
import com.haulmont.bali.util.Preconditions;
import org.apache.commons.lang.reflect.FieldUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * For internal use only.
 */
public final class BaseEntityInternalAccess {

    private BaseEntityInternalAccess() {
    }

    public static boolean isNew(BaseGenericIdEntity entity) {
        return entity.__new;
    }

    public static void setNew(BaseGenericIdEntity entity, boolean cubaNew) {
        entity.__new = cubaNew;
    }

    public static boolean isManaged(BaseGenericIdEntity entity) {
        return entity.__managed;
    }

    public static void setManaged(BaseGenericIdEntity entity, boolean cubaManaged) {
        entity.__managed = cubaManaged;
    }

    public static boolean isDetached(BaseGenericIdEntity entity) {
        return entity.__detached;
    }

    public static void setDetached(BaseGenericIdEntity entity, boolean detached) {
        entity.__detached = detached;
    }

    public static boolean isRemoved(BaseGenericIdEntity entity) {
        return entity.__removed;
    }

    public static void setRemoved(BaseGenericIdEntity entity, boolean removed) {
        entity.__removed = removed;
    }

    public static String[] getInaccessibleAttributes(BaseGenericIdEntity entity) {
        return entity.__inaccessibleAttributes;
    }

    public static void setInaccessibleAttributes(BaseGenericIdEntity entity, String[] __inaccessibleAttributes) {
        entity.__inaccessibleAttributes = __inaccessibleAttributes;
    }

    public static Multimap<String, UUID> getFilteredData(BaseGenericIdEntity entity) {
        return entity.__filteredData;
    }

    public static void setFilteredData(BaseGenericIdEntity entity, Multimap<String, UUID> filteredData) {
        entity.__filteredData = filteredData;
    }

    public static byte[] getSecurityToken(BaseGenericIdEntity entity) {
        return entity.__securityToken;
    }

    public static void setSecurityToken(BaseGenericIdEntity entity, byte[] securityToken) {
        entity.__securityToken = securityToken;
    }

    public static String[] getFilteredAttributes(BaseGenericIdEntity entity) {
        return entity.__filteredAttributes;
    }

    public static void setFilteredAttributes(BaseGenericIdEntity entity, String[] filteredAttributes) {
        entity.__filteredAttributes = filteredAttributes;
    }

    public static void setValue(BaseGenericIdEntity entity, String attribute, @Nullable Object value) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        Field field = FieldUtils.getField(entity.getClass(), attribute, true);
        if (field == null)
            throw new RuntimeException("Cannot find field '" + attribute + "' in class " + entity.getClass().getName());
        try {
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to set value to " + entity.getClass().getSimpleName() + "." + attribute, e);
        }
    }
}