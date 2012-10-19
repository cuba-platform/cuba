/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.ViewHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.kernel.StateManagerImpl;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.util.ObjectId;

import javax.annotation.ManagedBean;
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

        Set<String> set = new HashSet<String>();
        BitSet dirtySet = stateManager.getDirty();
        for (int i = 0; i < dirtySet.size() - 1; i++) {
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
     * @return UUID of the referenced entity
     * @throws IllegalStateException if the entity is not in Managed state
     */
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

    /**
     * Reload an entity from DB according to a combined view defined by the given array of views.
     * <p/> The method must be called inside of a transaction.
     * <p/> If the given entity is in managed state, the method returns the same object instance. If the entity is
     * detached, the method returns a new object instance.
     * @param entity    entity instance to reload
     * @param viewNames array of view names
     * @return          reloaded entity instance
     * @throws IllegalStateException if there is no active transaction
     */
    public <T extends Entity> T reloadEntity(T entity, String... viewNames) {
        Objects.requireNonNull(entity, "entity is null");

        Entity resultEntity = reloadEntity(entity.getClass(), entity.getId(), viewNames);
        return (T) resultEntity;
    }

    /**
     * Reload an entity from DB according to a combined view defined by the given array of views.
     * <p/> The method must be called inside of a transaction.
     * <p/> If there is a managed entity with the given id in the current persistence context, the method returns it.
     * Otherwise the method returns a new object instance.
     * @param entityClass   entity class
     * @param id            entity id
     * @param viewNames     array of view names
     * @return              reloaded entity instance
     * @throws IllegalStateException if there is no active transaction
     */
    public <T extends Entity> T reloadEntity(Class<T> entityClass, Object id, String... viewNames) {
        Objects.requireNonNull(entityClass, "entityClass is null");
        Objects.requireNonNull(id, "id is null");

        if (!persistence.isInTransaction())
            throw new IllegalStateException("No active transaction");

        EntityManager em = persistence.getEntityManager();

        View mainView = null;
        for (int i = 0; i < viewNames.length; i++) {
            String viewName = viewNames[i];
            View view = metadata.getViewRepository().getView(entityClass, viewName);
            if (i == 0) {
                mainView = view;
                em.setView(view);
            } else {
                em.addView(view);
            }
        }

        T e = em.find(entityClass, id);

        if (e != null && mainView != null && mainView.hasLazyProperties()) {
            em.fetch(e, mainView);
        }
        return e;
    }
}
