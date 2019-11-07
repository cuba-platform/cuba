/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.app.events;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.SetupAttributeAccessHandler;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ExtendedEntities;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * An event that is used by {@link SetupAttributeAccessHandler}s to receive the entity instance and to return
 * resulting access information.
 * <p>
 * Use {@link #addHidden(String)}, {@link #addReadOnly(String)} and {@link #addRequired(String)} methods to write
 * appropriate attribute names.

 * @param <T> type of entity
 */
public class SetupAttributeAccessEvent<T extends Entity>
        extends ApplicationEvent implements ResolvableTypeProvider {

    private static final long serialVersionUID = -8775623806210166422L;

    protected Set<String> readonlyAttributes;
    protected Set<String> hiddenAttributes;
    protected Set<String> requiredAttributes;

    public SetupAttributeAccessEvent(T entity) {
        super(entity);
    }

    /**
     * Entity instance in managed or new state.
     */
    @SuppressWarnings("unchecked")
    public T getEntity() {
        return (T) getSource();
    }

    @Nullable
    public Set<String> getReadonlyAttributes() {
        return readonlyAttributes;
    }

    @Nullable
    public Set<String> getHiddenAttributes() {
        return hiddenAttributes;
    }

    @Nullable
    public Set<String> getRequiredAttributes() {
        return requiredAttributes;
    }

    public SetupAttributeAccessEvent addReadOnly(String attributeName) {
        if (readonlyAttributes == null) {
            readonlyAttributes = new HashSet<>();
        }
        readonlyAttributes.add(attributeName);
        return this;
    }

    public SetupAttributeAccessEvent addRequired(String attributeName) {
        if (requiredAttributes == null) {
            requiredAttributes = new HashSet<>();
        }
        requiredAttributes.add(attributeName);
        return this;
    }

    public SetupAttributeAccessEvent addHidden(String attributeName) {
        if (hiddenAttributes == null) {
            hiddenAttributes = new HashSet<>();
        }
        hiddenAttributes.add(attributeName);
        return this;
    }

    /**
     * Kept for compatibility with the previous implementation based on Spring application events.
     */
    @Override
    public ResolvableType getResolvableType() {
        ExtendedEntities extendedEntities = AppBeans.get(ExtendedEntities.NAME);
        MetaClass metaClass = extendedEntities.getOriginalOrThisMetaClass(getEntity().getMetaClass());
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forClass(metaClass.getJavaClass()));
    }
}
