/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.kernel.StateManagerImpl;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.util.ObjectId;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

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

    protected Log log = LogFactory.getLog(getClass());

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    /**
     * Returns the set of dirty fields (fields changed since a last load from DB).
     *
     * @param entity entity instance
     * @return dirty field names
     */
    public Set<String> getDirtyFields(Entity entity) {
        if (!(entity instanceof PersistenceCapable))
            return Collections.emptySet();

        OpenJPAStateManager stateManager = (OpenJPAStateManager) ((PersistenceCapable) entity).pcGetStateManager();
        if (stateManager == null)
            return Collections.emptySet();

        Set<String> set = new HashSet<>();
        BitSet dirtySet = stateManager.getDirty();
        for (int i = 0; i < dirtySet.size(); i++) {
            if (dirtySet.get(i)) {
                FieldMetaData field = stateManager.getMetaData().getField(i);
                set.add(field.getName());
            }
        }
        return set;
    }

    /**
     * Checks if the property is loaded from DB.
     *
     * @param entity   entity
     * @param property name of the property
     * @return true if loaded
     * @throws IllegalStateException if the entity is not in Managed state
     */
    public boolean isLoaded(Object entity, String property) {
        if (entity instanceof PersistenceCapable) {
            final PersistenceCapable persistenceCapable = (PersistenceCapable) entity;
            final OpenJPAStateManager stateManager = (OpenJPAStateManager) persistenceCapable.pcGetStateManager();

            if (!(stateManager instanceof StateManagerImpl))
                throw new IllegalStateException("Entity must be in managed state");

            final BitSet loaded = stateManager.getLoaded();
            final ClassMetaData metaData = stateManager.getMetaData();

            final FieldMetaData fieldMetaData = metaData.getField(property);
            if (fieldMetaData == null) throw new IllegalStateException();

            final int index = fieldMetaData.getIndex();

            return loaded.get(index);
        } else {
            return true;
        }
    }

    /**
     * Returns an ID of directly referenced entity without loading it from DB.
     *
     * @param entity   master entity
     * @param property name of reference property
     * @return UUID of the referenced entity or null if the reference is null
     * @throws IllegalStateException if the entity is not in Managed state
     * @throws IllegalArgumentException if the specified property is not a reference
     */
    @Nullable
    public UUID getReferenceId(Object entity, String property) {
        OpenJPAStateManager stateManager = (OpenJPAStateManager) ((PersistenceCapable) entity).pcGetStateManager();
        if (!(stateManager instanceof StateManagerImpl))
            throw new IllegalStateException("Entity must be in managed state");

        ClassMetaData metaData = stateManager.getMetaData();
        int index = metaData.getField(property).getIndex();

        UUID id;
        BitSet loaded = stateManager.getLoaded();
        if (loaded.get(index)) {
            Object reference = ((Instance) entity).getValue(property);
            if (reference == null)
                return null;
            if (!(reference instanceof Instance))
                throw new IllegalArgumentException("Property " + property + " is not a reference");
            id = ((Instance) reference).getUuid();
        } else {
            Object implData = stateManager.getIntermediate(index);
            if (implData == null)
                return null;
            if (!(implData instanceof ObjectId))
                throw new IllegalArgumentException("Property " + property + " is not a reference");
            ObjectId objectId = (ObjectId) implData;
            id = (UUID) objectId.getId();
        }
        return id;
    }
}
