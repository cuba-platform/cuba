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
import com.google.common.collect.ObjectArrays;
import com.haulmont.bali.util.Preconditions;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * INTERNAL
 */
public final class BaseEntityInternalAccess {

    public static final int NEW = 1;
    public static final int DETACHED = 2;
    public static final int MANAGED = 4;
    public static final int REMOVED = 8;

    private BaseEntityInternalAccess() {
    }

    public static boolean isNew(AbstractNotPersistentEntity entity) {
        return entity.__new;
    }

    public static boolean isNew(BaseGenericIdEntity entity) {
        return (entity.__state & NEW) == NEW;
    }

    public static void setNew(AbstractNotPersistentEntity entity, boolean _new) {
        entity.__new = _new;
    }

    public static void setNew(BaseGenericIdEntity entity, boolean _new) {
        entity.__state = (byte) (_new ? entity.__state | NEW : entity.__state & ~NEW);
    }

    public static boolean isManaged(BaseGenericIdEntity entity) {
        return (entity.__state & MANAGED) == MANAGED;
    }

    public static void setManaged(BaseGenericIdEntity entity, boolean managed) {
        entity.__state = (byte) (managed ? entity.__state | MANAGED : entity.__state & ~MANAGED);
    }

    public static boolean isDetached(BaseGenericIdEntity entity) {
        return (entity.__state & DETACHED) == DETACHED;
    }

    public static void setDetached(BaseGenericIdEntity entity, boolean detached) {
        entity.__state = (byte) (detached ? entity.__state | DETACHED : entity.__state & ~DETACHED);
    }

    public static boolean isRemoved(BaseGenericIdEntity entity) {
        return (entity.__state & REMOVED) == REMOVED;
    }

    public static void setRemoved(BaseGenericIdEntity entity, boolean removed) {
        entity.__state = (byte) (removed ? entity.__state | REMOVED : entity.__state & ~REMOVED);
    }

    public static String[] getInaccessibleAttributes(Entity entity) {
        SecurityState state = getSecurityState(entity);
        return state != null ? getInaccessibleAttributes(state) : null;
    }

    public static String[] getInaccessibleAttributes(SecurityState state) {
        return state.inaccessibleAttributes;
    }

    public static void setInaccessibleAttributes(SecurityState state, String[] inaccessibleAttributes) {
        state.inaccessibleAttributes = inaccessibleAttributes;
    }

    public static Multimap<String, Object> getFilteredData(Entity entity) {
        SecurityState state = getSecurityState(entity);
        return state != null ? getFilteredData(state) : null;
    }

    public static Multimap<String, Object> getFilteredData(SecurityState state) {
        return state.filteredData;
    }

    public static void setFilteredData(SecurityState state, Multimap<String, Object> filteredData) {
        state.filteredData = filteredData;
    }

    public static byte[] getSecurityToken(Entity entity) {
        SecurityState state = getSecurityState(entity);
        return state != null ? getSecurityToken(state) : null;
    }

    public static byte[] getSecurityToken(SecurityState state) {
        return state.securityToken;
    }

    public static void setSecurityToken(SecurityState state, byte[] securityToken) {
        state.securityToken = securityToken;
    }

    public static String[] getFilteredAttributes(BaseGenericIdEntity entity) {
        SecurityState state = getSecurityState(entity);
        return state != null ? getFilteredAttributes(state) : null;
    }

    public static String[] getFilteredAttributes(SecurityState state) {
        return state.filteredAttributes;
    }

    public static void setFilteredAttributes(SecurityState state, String[] filteredAttributes) {
        state.filteredAttributes = filteredAttributes;
    }

    public static String[] getReadonlyAttributes(SecurityState state) {
        return state.readonlyAttributes;
    }

    public static void setReadonlyAttributes(SecurityState state, String[] readonlyAttributes) {
        state.readonlyAttributes = readonlyAttributes;
    }

    public static void addReadonlyAttributes(SecurityState state, String[] readonlyAttributes) {
        if (state.readonlyAttributes == null) {
            state.readonlyAttributes = readonlyAttributes;
        } else {
            state.readonlyAttributes =
                    ObjectArrays.concat(state.readonlyAttributes, readonlyAttributes, String.class);
        }
    }

    public static String[] getRequiredAttributes(SecurityState state) {
        return state.requiredAttributes;
    }

    public static void setRequiredAttributes(SecurityState state, String[] requiredAttributes) {
        state.requiredAttributes = requiredAttributes;
    }

    public static void addRequiredAttributes(SecurityState state, String[] requiredAttributes) {
        if (state.requiredAttributes == null) {
            state.requiredAttributes = requiredAttributes;
        } else {
            state.requiredAttributes =
                    ObjectArrays.concat(state.requiredAttributes, requiredAttributes, String.class);
        }
    }

    public static String[] getHiddenAttributes(SecurityState state) {
        return state.hiddenAttributes;
    }

    public static void setHiddenAttributes(SecurityState state, String[] hiddenAttributes) {
        state.hiddenAttributes = hiddenAttributes;
    }

    public static void addHiddenAttributes(SecurityState state, String[] hiddenAttributes) {
        if (state.hiddenAttributes == null) {
            state.hiddenAttributes = hiddenAttributes;
        } else {
            state.hiddenAttributes =
                    ObjectArrays.concat(state.hiddenAttributes, hiddenAttributes, String.class);
        }
    }

    public static boolean supportsSecurityState(Entity entity) {
        return entity instanceof BaseGenericIdEntity || entity instanceof EmbeddableEntity;
    }

    public static SecurityState getSecurityState(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "Entity is null");
        SecurityState securityState;
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;
            securityState = baseGenericIdEntity.__securityState;
        } else if (entity instanceof EmbeddableEntity) {
            EmbeddableEntity embeddableEntity = (EmbeddableEntity) entity;
            securityState = embeddableEntity.__securityState;
        } else {
            throw new IllegalArgumentException(String.format("Entity with type [%s] does not support security state", entity.getMetaClass().getName()));
        }
        return securityState;
    }

    public static void setSecurityState(Entity entity, SecurityState securityState) {
        Preconditions.checkNotNullArgument(entity, "Entity is null");
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;
            baseGenericIdEntity.__securityState = securityState;
        } else if (entity instanceof EmbeddableEntity) {
            EmbeddableEntity embeddableEntity = (EmbeddableEntity) entity;
            embeddableEntity.__securityState = securityState;
        } else {
            throw new IllegalArgumentException(String.format("Entity with type [%s] does not support security state", entity.getMetaClass().getName()));
        }
    }

    @SuppressWarnings("rawtypes")
    public static SecurityState getOrCreateSecurityState(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "Entity is null");
        SecurityState securityState;
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;
            if (baseGenericIdEntity.__securityState == null) {
                baseGenericIdEntity.__securityState = new SecurityState();
            }
            securityState = baseGenericIdEntity.__securityState;
        } else if (entity instanceof EmbeddableEntity) {
            EmbeddableEntity embeddableEntity = (EmbeddableEntity) entity;
            if (embeddableEntity.__securityState == null) {
                embeddableEntity.__securityState = new SecurityState();
            }
            securityState = embeddableEntity.__securityState;
        } else {
            throw new IllegalArgumentException(String.format("Entity with type [%s] does not support security state", entity.getMetaClass().getName()));
        }
        return securityState;
    }

    public static boolean isHiddenOrReadOnly(SecurityState securityState, String attributeName) {
        if (securityState == null) {
            return false;
        }
        return securityState.getHiddenAttributes().contains(attributeName)
                || securityState.getReadonlyAttributes().contains(attributeName);
    }

    public static boolean isRequired(SecurityState securityState, String attributeName) {
        if (securityState == null) {
            return false;
        }
        return securityState.getRequiredAttributes().contains(attributeName);
    }

    public static void setValue(Entity entity, String attribute, @Nullable Object value) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        Field field = FieldUtils.getField(entity.getClass(), attribute, true);
        if (field == null)
            throw new RuntimeException(String.format("Cannot find field '%s' in class %s", attribute, entity.getClass().getName()));
        try {
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Unable to set value to %s.%s", entity.getClass().getSimpleName(), attribute), e);
        }
    }

    public static void setValueForHolder(Entity entity, String attribute, @Nullable Object value) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        Field field = FieldUtils.getField(entity.getClass(), String.format("_persistence_%s_vh",attribute), true);
        if (field == null)
            return;
        try {
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Unable to set value to %s.%s", entity.getClass().getSimpleName(), attribute), e);
        }
    }

    public static Object getValue(Entity entity, String attribute) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        Field field = FieldUtils.getField(entity.getClass(), attribute, true);
        if (field == null)
            throw new RuntimeException(String.format("Cannot find field '%s' in class %s", attribute, entity.getClass().getName()));
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Unable to set value to %s.%s", entity.getClass().getSimpleName(), attribute), e);
        }
    }

    public static void copySystemState(BaseGenericIdEntity src, BaseGenericIdEntity dst) {
        dst.copySystemState(src);
    }
}