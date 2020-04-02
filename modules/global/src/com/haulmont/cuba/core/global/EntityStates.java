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

package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.StackTrace;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.internal.helper.IdentityHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Provides information about entities states.
 */
@Component(EntityStates.NAME)
public class EntityStates {
    public static final String NAME = "cuba_EntityStates";

    @Inject
    protected PersistentAttributesLoadChecker checker;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Metadata metadata;

    private static final Logger log = LoggerFactory.getLogger(EntityStates.class);

    /**
     * Determines whether the instance is <em>New</em>, i.e. just created and not stored in database yet.
     *
     * @param entity entity instance
     * @return  - true if the instance is a new persistent entity, or if it is actually in Managed state
     *            but newly-persisted in this transaction <br>
     *          - true if the instance is a new non-persistent entity never returned from DataManager <br>
     *          - false otherwise
     * @throws IllegalArgumentException if entity instance is null
     */
    public boolean isNew(Object entity) {
        checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity) {
            return BaseEntityInternalAccess.isNew((BaseGenericIdEntity) entity);
        } else if (entity instanceof AbstractNotPersistentEntity) {
            return BaseEntityInternalAccess.isNew((AbstractNotPersistentEntity) entity);
        } else {
            if (log.isTraceEnabled()) {
                log.trace("EntityStates.isNew is called for unsupported type '{}'. Stacktrace:\n{}",
                        entity.getClass().getSimpleName(), StackTrace.asString());
            }
        }
        return false;
    }

    /**
     * Determines whether the instance is <em>Managed</em>, i.e. attached to a persistence context.
     *
     * @param entity entity instance
     * @return - true if the instance is managed,<br>
     *         - false if it is New (and not yet persisted) or Detached, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public boolean isManaged(Object entity) {
        checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity) {
            return BaseEntityInternalAccess.isManaged((BaseGenericIdEntity) entity);
        } else {
            if (log.isTraceEnabled()) {
                log.trace("EntityStates.isManaged is called for unsupported type '{}'. Stacktrace:\n{}",
                        entity.getClass().getSimpleName(), StackTrace.asString());
            }
        }
        return false;
    }

    /**
     * Determines whether the instance is <em>Detached</em>, i.e. stored in database but not attached to a persistence
     * context at the moment.
     *
     * @param entity entity instance
     * @return - true if the instance is detached,<br>
     *         - false if it is New or Managed, or if it is not a persistent entity
     * @throws IllegalArgumentException if entity instance is null
     */
    public boolean isDetached(Object entity) {
        checkNotNullArgument(entity, "entity is null");
        if (entity instanceof BaseGenericIdEntity && BaseEntityInternalAccess.isDetached((BaseGenericIdEntity) entity)) {
            return true;
        } else {
            if (log.isTraceEnabled()) {
                log.trace("EntityStates.isDetached is called for unsupported type '{}'. Stacktrace:\n{}",
                        entity.getClass().getSimpleName(), StackTrace.asString());
            }
        }
        return false;
    }

    /**
     * DEPRECATED. Use {@link MetadataTools#getEntityName(Class)} instead.
     */
    @Deprecated
    public String getEntityName(Class<?> entityClass) {
        Annotation annotation = entityClass.getAnnotation(javax.persistence.Entity.class);
        if (annotation == null)
            throw new IllegalArgumentException("Class " + entityClass + " is not a persistent entity");
        String name = ((javax.persistence.Entity) annotation).name();
        if (!StringUtils.isEmpty(name))
            return name;
        else
            return entityClass.getSimpleName();
    }

    /**
     * DEPRECATED. Use {@link MetadataTools#isSoftDeleted(Class)} instead.
     */
    @Deprecated
    public boolean isSoftDeleted(Class entityClass) {
        return SoftDelete.class.isAssignableFrom(entityClass);
    }

    /**
     * Checks if the property is loaded from DB.
     * <p>Non-persistent attributes are considered loaded if they do not have related properties, or all related
     * properties are loaded.
     *
     * @param entity   entity
     * @param property name of the property. Only immediate attributes of the entity are supported.
     * @return true if loaded
     */
    public boolean isLoaded(Object entity, String property) {
        return checker.isLoaded(entity, property);
    }

    /**
     * Check that entity has all specified properties loaded from DB.
     * Throw exception if property is not loaded.
     *
     * @param entity     entity
     * @param properties property names
     * @throws IllegalArgumentException if at least one of properties is not loaded
     */
    public void checkLoaded(Object entity, String... properties) {
        checkNotNullArgument(entity);

        for (String property : properties) {
            if (!isLoaded(entity, property)) {
                String errorMessage = String.format("%s.%s is not loaded", entity.getClass().getSimpleName(), property);
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    protected void checkLoadedWithView(Entity entity, View view, Set<Entity> visited) {
        if (visited.contains(entity)) {
            return;
        }

        visited.add(entity);

        for (ViewProperty property : view.getProperties()) {
            MetaClass metaClass = entity.getMetaClass();
            MetaProperty metaProperty = metaClass.getPropertyNN(property.getName());

            if (!isLoaded(entity, property.getName())) {
                String errorMessage = String.format("%s.%s is not loaded",
                        entity.getClass().getSimpleName(), property.getName());
                throw new IllegalArgumentException(errorMessage);
            }

            if (metaProperty.getRange().isClass()) {
                View propertyView = property.getView();

                if (propertyView != null && metadataTools.isPersistent(metaProperty)) {
                    Object value = entity.getValue(metaProperty.getName());

                    if (value != null) {
                        if (!metaProperty.getRange().getCardinality().isMany()) {
                            checkLoadedWithView((Entity) value, propertyView, visited);
                        } else {
                            @SuppressWarnings("unchecked")
                            Collection<Entity> collection = (Collection) value;

                            for (Entity item : collection) {
                                checkLoadedWithView(item, propertyView, visited);
                            }
                        }
                    }
                }
            }
        }

        // after check we remove item from visited because different subtrees may have different view for one instance
        visited.remove(entity);
    }

    /**
     * Check that all properties of the view are loaded from DB for the passed entity.
     * Throws exception if some property is not loaded.
     *
     * @param entity entity
     * @param view   view
     * @throws IllegalArgumentException if at least one of properties is not loaded
     */
    @SuppressWarnings("unchecked")
    public void checkLoadedWithView(Entity entity, View view) {
        checkNotNullArgument(entity);
        checkNotNullArgument(view);

        checkLoadedWithView(entity, view, new IdentityHashSet());
    }

    /**
     * Check that all properties of the view are loaded from DB for the passed entity.
     * Throws exception if some property is not loaded.
     *
     * @param entity   entity
     * @param viewName view name
     * @throws IllegalArgumentException if at least one of properties is not loaded
     */
    @SuppressWarnings("unchecked")
    public void checkLoadedWithView(Entity entity, String viewName) {
        checkNotNullArgument(viewName);

        checkLoadedWithView(entity, viewRepository.getView(entity.getMetaClass(), viewName));
    }

    protected boolean isLoadedWithView(Entity entity, View view, Set<Entity> visited) {
        if (visited.contains(entity)) {
            return true;
        }

        visited.add(entity);

        for (ViewProperty property : view.getProperties()) {
            MetaClass metaClass = entity.getMetaClass();
            MetaProperty metaProperty = metaClass.getPropertyNN(property.getName());

            if (!isLoaded(entity, property.getName())) {
                return false;
            }

            if (metaProperty.getRange().isClass()) {
                View propertyView = property.getView();

                if (propertyView != null && metadataTools.isPersistent(metaProperty)) {
                    Object value = entity.getValue(metaProperty.getName());

                    if (value != null) {
                        if (!metaProperty.getRange().getCardinality().isMany()) {
                            if (!isLoadedWithView((Entity) value, propertyView, visited)) {
                                return false;
                            }
                        } else {
                            @SuppressWarnings("unchecked")
                            Collection<Entity> collection = (Collection) value;

                            for (Entity item : collection) {
                                if (!isLoadedWithView(item, propertyView, visited)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        // after check we remove item from visited because different subtrees may have different view for one instance
        visited.remove(entity);

        return true;
    }

    /**
     * Check that all properties of the view are loaded from DB for the passed entity.
     *
     * @param entity entity
     * @param view   view name
     * @return false if at least one of properties is not loaded
     */
    @SuppressWarnings("unchecked")
    public boolean isLoadedWithView(Entity entity, View view) {
        checkNotNullArgument(entity);
        checkNotNullArgument(view);

        return isLoadedWithView(entity, view, new IdentityHashSet());
    }

    /**
     * Check that all properties of the view are loaded from DB for the passed entity.
     *
     * @param entity   entity
     * @param viewName view name
     * @return false if at least one of properties is not loaded
     */
    @SuppressWarnings("unchecked")
    public boolean isLoadedWithView(Entity entity, String viewName) {
        checkNotNullArgument(viewName);

        return isLoadedWithView(entity, viewRepository.getView(entity.getMetaClass(), viewName));
    }

    /**
     * Returns a view that corresponds to the loaded attributes of the given entity instance.
     * @param entity entity instance
     * @return view
     */
    public View getCurrentView(Entity entity) {
        checkNotNullArgument(entity);

        View view = new View(entity.getClass(), false);
        recursivelyGetCurrentView(entity, view, new HashSet<>());
        return view;
    }

    protected void recursivelyGetCurrentView(Entity entity, View view, HashSet<Object> visited) {
        if (visited.contains(entity))
            return;
        visited.add(entity);

        // Using MetaClass of the view helps in the case when the entity is an item of a collection, and the collection
        // can contain instances of different subclasses. So we don't want to add specific properties of subclasses
        // to the resulting view.
        MetaClass metaClass = metadata.getClassNN(view.getEntityClass());

        for (MetaProperty property : metaClass.getProperties()) {
            if (!isLoaded(entity, property.getName()))
                continue;

            if (property.getRange().isClass()) {
                View propertyView = new View(property.getRange().asClass().getJavaClass());
                view.addProperty(property.getName(), propertyView);
                if (isLoaded(entity, property.getName())) {
                    Object value = entity.getValue(property.getName());
                    if (value != null) {
                        if (value instanceof Collection) {
                            for (Object item : ((Collection) value)) {
                                recursivelyGetCurrentView((Entity) item, propertyView, visited);
                            }
                        } else {
                            recursivelyGetCurrentView((Entity) value, propertyView, visited);
                        }
                    }
                }
            } else {
                view.addProperty(property.getName());
            }
        }
    }

    /**
     * Determines whether the entity instance was <em>deleted</em>.
     *
     * @param entity entity instance
     * @return - true if the instance was deleted
     *         - false otherwise
     * @throws IllegalArgumentException if entity instance is null
     */
    public boolean isDeleted(Object entity) {
        checkNotNullArgument(entity, "entity is null");
        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted())
            return true;
        if (entity instanceof BaseGenericIdEntity && BaseEntityInternalAccess.isRemoved((BaseGenericIdEntity) entity)) {
            return true;
        }
        return false;
    }

    /**
     * Makes a newly constructed object detached. The detached object can be passed to {@code DataManager.commit()} or
     * to {@code EntityManager.merge()} to save its state to the database.
     * <p>If an object with such ID does not exist in the database, a new object will be inserted.
     * <p>If the entity is {@code Versioned}, the version attribute should be equal to the latest version existing in
     * the database, or null for a new object.
     *
     * @param entity    entity in the New state
     * @throws IllegalStateException if the entity is Managed
     * @see #isDetached(Object)
     * @see #makePatch(BaseGenericIdEntity)
     */
    public void makeDetached(BaseGenericIdEntity entity) {
        checkNotNullArgument(entity, "entity is null");
        if (BaseEntityInternalAccess.isManaged(entity))
            throw new IllegalStateException("entity is managed");

        BaseEntityInternalAccess.setNew(entity, false);
        BaseEntityInternalAccess.setDetached(entity, true);
    }

    /**
     * Makes a newly constructed object a patch object. The patch object is {@code !isNew() && !isDetached() && !isManaged()}.
     * The patch object can be passed to {@code DataManager.commit()} or
     * to {@code EntityManager.merge()} to save its state to the database. Only <b>non-null values</b> of attributes are
     * updated.
     * <p>If an object with such ID does not exist in the database, a new object will be inserted.
     * <p>If the entity is {@code Versioned}, the version attribute should be null or equal to the latest version existing in
     * the database.
     *
     * @param entity    entity in the New or Detached state
     * @throws IllegalStateException if the entity is Managed
     * @see #isDetached(Object)
     * @see #makeDetached(BaseGenericIdEntity)
     */
    public void makePatch(BaseGenericIdEntity entity) {
        checkNotNullArgument(entity, "entity is null");
        if (BaseEntityInternalAccess.isManaged(entity))
            throw new IllegalStateException("entity is managed");

        BaseEntityInternalAccess.setNew(entity, false);
        BaseEntityInternalAccess.setDetached(entity, false);
    }
}