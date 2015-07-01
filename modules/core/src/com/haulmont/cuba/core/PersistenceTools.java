/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.impl.UUIDDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.persistence.descriptors.changetracking.ChangeTracker;
import org.eclipse.persistence.indirection.ValueHolderInterface;
import org.eclipse.persistence.internal.descriptors.changetracking.AttributeChangeListener;
import org.eclipse.persistence.internal.indirection.DatabaseValueHolder;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.ObjectChangeSet;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.JoinColumn;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Utility class to provide common functionality related to persistence.
 * <p/> Implemented as Spring bean to allow extension in application projects.
 * <p/> A reference to this class can be obtained either via DI or by
 * {@link com.haulmont.cuba.core.Persistence#getTools()} method.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(PersistenceTools.NAME)
public class PersistenceTools {

    public static final String NAME = "cuba_PersistenceTools";

    protected final Log log = LogFactory.getLog(getClass());

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    /**
     * Returns the set of dirty fields (fields changed since a last load from DB).
     * <p> If the entity is new, returns all its fields.
     * <p> If the entity is not persistent or not in the Managed state, returns empty set.
     *
     * @param entity entity instance
     * @return dirty field names
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
     * @param entity   master entity
     * @param property name of reference property
     * @return UUID of the referenced entity or null if the reference is null
     * @throws IllegalArgumentException if the entity is not persistent or if the specified property is not a reference
     * @throws IllegalStateException if the entity is not in Managed state
     * @throws RuntimeException if anything goes wrong when retrieving the ID
     */
    @Nullable @SuppressWarnings("unchecked")
    public <T> T getReferenceId(Object entity, String property) {
        if (!(entity instanceof BaseGenericIdEntity))
            throw new IllegalArgumentException("Not a persistent entity");

        MetaProperty metaProperty = metadata.getClassNN(entity.getClass()).getPropertyNN(property);
        if (!metaProperty.getRange().isClass() || metaProperty.getRange().getCardinality().isMany())
            throw new IllegalArgumentException("Property is not a reference");

        if (!PersistenceHelper.isManaged(entity))
            throw new IllegalStateException("Entity must be in managed state");

        // todo EL
        Object value = ((BaseGenericIdEntity) entity).getValue(property);
        return value == null ? null : (T) ((BaseGenericIdEntity) value).getId();
    }

    protected RuntimeException getReferenceIdError(Object entity, String property, String message, @Nullable Throwable cause) {
        return new RuntimeException(
                "Error retrieving reference ID from " + entity.getClass().getSimpleName() + "." + property + ": " + message,
                cause);
    }
}
