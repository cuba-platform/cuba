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

package com.haulmont.cuba.core;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.changetracking.ChangeTracker;
import org.eclipse.persistence.indirection.ValueHolderInterface;
import org.eclipse.persistence.internal.descriptors.changetracking.AttributeChangeListener;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.indirection.DatabaseValueHolder;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.ObjectChangeSet;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.mappings.foundation.AbstractColumnMapping;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.changesets.ChangeRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Utility class to provide common functionality related to persistence.
 * <p/> Implemented as Spring bean to allow extension in application projects.
 * <p/> A reference to this class can be obtained either via DI or by
 * {@link com.haulmont.cuba.core.Persistence#getTools()} method.
 */
@Component(PersistenceTools.NAME)
public class PersistenceTools {

    public static final String NAME = "cuba_PersistenceTools";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    /**
     * Returns the set of dirty attributes (changed since the last load from the database).
     * <p> If the entity is new, returns all its attributes.
     * <p> If the entity is not persistent or not in the Managed state, returns empty set.
     *
     * @param entity    entity instance
     * @return          dirty attribute names
     * @see #isDirty(Entity, String...)
     */
    public Set<String> getDirtyFields(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");

        if (!(entity instanceof ChangeTracker) || !PersistenceHelper.isManaged(entity))
            return Collections.emptySet();

        HashSet<String> result = new HashSet<>();
        if (PersistenceHelper.isNew(entity)) {
            for (MetaProperty property : metadata.getClassNN(entity.getClass()).getProperties()) {
                if (metadata.getTools().isPersistent(property))
                    result.add(property.getName());
            }
        } else {
            ObjectChangeSet objectChanges =
                    ((AttributeChangeListener)((ChangeTracker) entity)._persistence_getPropertyChangeListener()).getObjectChangeSet();
            if (objectChanges != null) // can be null for example in AFTER_DELETE entity listener
                result.addAll(objectChanges.getChangedAttributeNames());
        }
        return result;
    }

    /**
     * Returns true if at least one of the given attributes is dirty (i.e. changed since the last load from the database).
     * <p> If the entity is new, always returns true.
     * <p> If the entity is not persistent or not in the Managed state, always returns false.
     * @param entity        entity instance
     * @param attributes    attributes to check
     * @see #getDirtyFields(Entity)
     */
    public boolean isDirty(Entity entity, String... attributes) {
        Set<String> dirtyFields = getDirtyFields(entity);
        for (String attribute : attributes) {
            if (dirtyFields.contains(attribute))
                return true;
        }
        return false;
    }

    /**
     * Returns an old value of an attribute changed in the current transaction. The entity must be in the Managed state.
     * @param entity    entity instance
     * @param attribute attribute name
     * @return  an old value stored in the database. For a new entity returns null.
     * @throws IllegalArgumentException if the entity is not persistent or not in the Managed state
     */
    @Nullable
    public Object getOldValue(Entity entity, String attribute) {
        Preconditions.checkNotNullArgument(entity, "entity is null");

        if (!(entity instanceof ChangeTracker) || !PersistenceHelper.isManaged(entity))
            throw new IllegalArgumentException("The entity " + entity + " is not a ChangeTracker");

        if (!PersistenceHelper.isManaged(entity))
            throw new IllegalArgumentException("The entity " + entity + " is not in the Managed state");

        if (PersistenceHelper.isNew(entity)) {
            return null;
        } else {
            ObjectChangeSet objectChanges =
                    ((AttributeChangeListener)((ChangeTracker) entity)._persistence_getPropertyChangeListener()).getObjectChangeSet();
            if (objectChanges != null) { // can be null for example in AFTER_DELETE entity listener
                ChangeRecord changeRecord = objectChanges.getChangesForAttributeNamed(attribute);
                if (changeRecord != null)
                    return changeRecord.getOldValue();
            }
        }
        return null;
    }

    /**
     * Checks if the property is loaded from DB.
     *
     * @param entity   entity
     * @param property name of the property
     * @return true if loaded
     */
    public boolean isLoaded(Object entity, String property) {
        return PersistenceHelper.isLoaded(entity, property);
    }

    /**
     * Returns an ID of directly referenced entity without loading it from DB.
     *
     * <p>Use this method only when the given entity has been loaded <b>without a view</b>. If the entity is loaded with a view
     * and the view contains the reference attribute, you can get the reference value from the corresponding getter.
     * If the view does not contain the reference, the returned {@link RefId} will have {@link RefId#isLoaded()} = false.
     *
     * <p>Usage example:
     * <pre>
     *   PersistenceTools.RefId refId = persistenceTools.getReferenceId(doc, "currency");
     *   if (refId.isLoaded()) {
     *       String currencyCode = (String) refId.getValue();
     *   }
     * </pre>
     *
     * @param entity   entity instance in managed state
     * @param property name of reference property
     * @return {@link RefId} instance which contains the referenced entity ID
     * @throws IllegalArgumentException if the specified property is not a reference
     * @throws IllegalStateException if the entity is not in Managed state
     * @throws RuntimeException if anything goes wrong when retrieving the ID
     */
    public RefId getReferenceId(BaseGenericIdEntity entity, String property) {
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        MetaProperty metaProperty = metaClass.getPropertyNN(property);

        if (!metaProperty.getRange().isClass() || metaProperty.getRange().getCardinality().isMany())
            throw new IllegalArgumentException("Property is not a reference");

        if (!PersistenceHelper.isManaged(entity))
            throw new IllegalStateException("Entity must be in managed state");

        String[] inaccessibleAttributes = BaseEntityInternalAccess.getInaccessibleAttributes(entity);
        if (inaccessibleAttributes != null) {
            for (String inaccessibleAttr : inaccessibleAttributes) {
                if (inaccessibleAttr.equals(property))
                    return RefId.createNotLoaded(property);
            }
        }

        if (entity instanceof FetchGroupTracker) {
            FetchGroup fetchGroup = ((FetchGroupTracker) entity)._persistence_getFetchGroup();
            if (fetchGroup != null) {
                if (!fetchGroup.getAttributeNames().contains(property))
                    return RefId.createNotLoaded(property);
                else {
                    Entity refEntity = (Entity) entity.getValue(property);
                    return RefId.create(property, refEntity == null ? null : refEntity.getId());
                }
            }
        }

        try {
            Method vhMethod = entity.getClass().getMethod("_persistence_get_" + property + "_vh");
            ValueHolderInterface vh = (ValueHolderInterface) vhMethod.invoke(entity);
            if (vh instanceof DatabaseValueHolder) {
                AbstractRecord row = ((DatabaseValueHolder) vh).getRow();
                if (row != null) {
                    Session session = persistence.getEntityManager().getDelegate().unwrap(Session.class);
                    ClassDescriptor descriptor = session.getDescriptor(entity);
                    DatabaseMapping mapping = descriptor.getMappingForAttributeName(property);
                    Vector<DatabaseField> fields = mapping.getFields();
                    if (fields.size() != 1) {
                        throw new IllegalStateException("Invalid number of columns in mapping: " + fields);
                    }
                    Object value = row.get(fields.get(0));
                    if (value != null) {
                        ClassDescriptor refDescriptor = mapping.getReferenceDescriptor();
                        DatabaseMapping refMapping = refDescriptor.getMappingForAttributeName(metadata.getTools().getPrimaryKeyName(metaClass));
                        if (refMapping instanceof AbstractColumnMapping) {
                            Converter converter = ((AbstractColumnMapping) refMapping).getConverter();
                            if (converter != null) {
                                return RefId.create(property, converter.convertDataValueToObjectValue(value, session));
                            }
                        }
                    }
                    return RefId.create(property, value);
                }
            }
            return RefId.createNotLoaded(property);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reference ID from "
                    + entity.getClass().getSimpleName() + "." + property, e);
        }
    }

    /**
     * A wrapper for the reference ID value returned by {@link #getReferenceId(BaseGenericIdEntity, String)} method.
     * @see #isLoaded()
     * @see #getValue()
     */
    public static class RefId {

        private String name;
        private final boolean loaded;
        private final Object value;

        private RefId(String name, boolean loaded, Object value) {
            this.name = name;
            this.loaded = loaded;
            this.value = value;
        }

        public static RefId create(String name, Object value) {
            return new RefId(name, true, value);
        }

        public static RefId createNotLoaded(String name) {
            return new RefId(name, false, true);
        }

        /**
         * Returns true if the reference ID has been loaded and can be retrieved by calling {@link #getValue()}
         */
        public boolean isLoaded() {
            return loaded;
        }

        /**
         * Returns the reference ID value (can be null) if {@link #isLoaded()} is true
         * @throws IllegalStateException if {@link #isLoaded()} is false
         */
        @Nullable
        public Object getValue() {
            if (!loaded)
                throw new IllegalStateException("Property '" + name + "' has not been loaded");
            return value;
        }
    }
}