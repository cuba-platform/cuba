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

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.model.utils.MethodsCache;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.haulmont.cuba.core.entity.BaseEntityInternalAccess.*;

public interface PersistentEntity<T> extends Entity<T> {

    @Override
    default MetaClass getMetaClass() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getSession().getClassNN(getClass());
    }

    @Deprecated
    @Override
    default String getInstanceName() {
        return InstanceUtils.getInstanceName(this);
    }

    default ListenersHolder getListenersHolder() {
        throw new UnsupportedOperationException("Entity isn't enhanced");
    }

    @Override
    default EntityEntry<T> getEntityEntry() {
        throw new UnsupportedOperationException("Entity isn't enhanced");
    }

    @Override
    default void addPropertyChangeListener(Instance.PropertyChangeListener listener) {
        getListenersHolder().addPropertyChangeListener(listener);
    }

    @Override
    default void removePropertyChangeListener(Instance.PropertyChangeListener listener) {
        getListenersHolder().removePropertyChangeListener(listener);
    }

    @Override
    default void removeAllListeners() {
        getListenersHolder().removeAllListeners();
    }

    @SuppressWarnings("unchecked")
    @Override
    default <V> V getValue(String name) {
        return (V) MethodsCache.getOrCreate(getClass()).invokeGetter(this, name);
    }

    @Override
    default void setValue(String name, Object value) {
        setValue(name, value, true);
    }

    /**
     * Set value to property in instance
     * <p>
     * For internal use only. Use {@link #setValue(String, Object)}
     *
     * @param name        property name
     * @param value       value
     * @param checkEquals check equals for previous and new value.
     *                    If flag is true and objects equals, then setter will not be invoked
     */
    default void setValue(String name, Object value, boolean checkEquals) {
        Object oldValue = getValue(name);
        if ((!checkEquals) || (!InstanceUtils.propertyValueEquals(oldValue, value))) {
            MethodsCache.getOrCreate(getClass()).invokeSetter(this, name, value);
        }
    }

    @Override
    default <V> V getValueEx(String name) {
        return InstanceUtils.getValueEx(this, name);
    }

    @Nullable
    @Override
    default <V> V getValueEx(Instance.BeanPropertyPath propertyPath) {
        return InstanceUtils.getValueEx(this, propertyPath);
    }

    @Override
    default void setValueEx(String name, Object value) {
        InstanceUtils.setValueEx(this, name, value);
    }

    @Override
    default void setValueEx(Instance.BeanPropertyPath propertyPath, Object value) {
        InstanceUtils.setValueEx(this, propertyPath, value);
    }

    static boolean $$_CUBA_entityEquals(Object a, Object b) {
        if (a == b)
            return true;

        if (b == null || a.getClass() != b.getClass())
            return false;

        return Objects.equals(((Entity) a).getEntityEntry().getId(), ((Entity) b).getEntityEntry().getId());
    }

    static int $$_CUBA_entityHashCode(Object a) {
        Object id = ((Entity) a).getEntityEntry().getId();
        return id != null ? id.hashCode() : 0;
    }

    static String $$_CUBA_entityToString(Object a) {
        String state = "";
        Entity entity = ((Entity)a);
        if (isNew(entity))
            state += "new,";
        if (isManaged(entity))
            state += "managed,";
        if (isDetached(entity))
            state += "detached,";
        if (isRemoved(entity))
            state += "removed,";
        if (state.length() > 0)
            state = state.substring(0, state.length() - 1);
        return entity.getClass().getName() + "-" + entity.getEntityEntry().getId() + " [" + state + "]";
    }
}
