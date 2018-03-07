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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.bali.events.EventRouter;
import com.haulmont.bali.util.Numbers;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.model.DataContext;

import javax.annotation.Nullable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;

/**
 *
 */
public class StandardDataContext implements DataContext {

    protected final Metadata metadata;
    protected final DataManager dataManager;
    protected final EntityStates entityStates;

    protected EventRouter eventRouter = new EventRouter();

    protected Map<Class<?>, Map<Object, Entity>> content = new HashMap<>();

    protected Set<Entity> modifiedInstances = new HashSet<>();

    protected Set<Entity> removedInstances = new HashSet<>();

    protected ChangeListener changeListener = new ChangeListener();

    protected boolean disableListeners;

    protected StandardDataContext parentContext;

    public StandardDataContext(Metadata metadata, DataManager dataManager, EntityStates entityStates) {
        this.metadata = metadata;
        this.dataManager = dataManager;
        this.entityStates = entityStates;
    }

    @Override
    public DataContext getParent() {
        return parentContext;
    }

    @Override
    public void setParent(DataContext parentContext) {
        Preconditions.checkNotNullArgument(parentContext, "parentContext is null");
        if (!(parentContext instanceof StandardDataContext))
            throw new IllegalArgumentException("Unsupported DataContext type: " + parentContext.getClass().getName());
        this.parentContext = (StandardDataContext) parentContext;

        for (Entity entity : this.parentContext.getAll()) {
            Entity entityCopy;
            try {
                entityCopy = entity.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Cannot create a copy of " + entity, e);
            }
            copyState(entity, entityCopy);
            merge(entityCopy);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T extends Entity<K>, K> T find(Class<T> entityClass, K entityId) {
        Map<Object, Entity> entityMap = content.get(entityClass);
        if (entityMap != null)
            return (T) entityMap.get(entityId);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(@Nullable Entity entity) {
        if (entity == null)
            return false;
        else
            return find(entity.getClass(), entity.getId()) != null;
    }

    @Override
    public <T extends Entity> T merge(T entity) {
        return merge(entity, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T merge(T entity, boolean deep) {
        Preconditions.checkNotNullArgument(entity, "entity is null");

        disableListeners = true;
        T result;
        try {
            result = (T) internalMerge(entity);
            if (deep) {
                metadata.getTools().traverseAttributes(entity, new MergingAttributeVisitor());
            }
        } finally {
            disableListeners = false;
        }
        return result;
    }

    protected Entity internalMerge(Entity entity) {
        Map<Object, Entity> entityMap = content.computeIfAbsent(entity.getClass(), aClass -> new HashMap<>());
        Entity managedInstance = entityMap.get(entity.getId());
        if (managedInstance != null) {
            if (managedInstance != entity) {
                copyState(entity, managedInstance);
            }
            return managedInstance;
        }
        entityMap.put(entity.getId(), entity);

        entity.addPropertyChangeListener(changeListener);

        if (entityStates.isNew(entity)) {
            modifiedInstances.add(entity);
        }
        return entity;
    }

    /**
     * (1) src.new -> dst.new : copy all non-null                                   - should not happen (happens in setParent?)
     * (2) src.new -> dst.det : do nothing                                          - should not happen
     * (3) src.det -> dst.new : copy all loaded, make detached                      - normal situation after commit
     * (4) src.det -> dst.det : if src.version >= dst.version, copy all loaded      - normal situation after commit (and in setParent?)
     *                          if src.version < dst.version, do nothing            - should not happen
     */
    protected void copyState(Entity srcEntity, Entity dstEntity) {
        boolean srcNew = entityStates.isNew(srcEntity);
        boolean dstNew = entityStates.isNew(dstEntity);
        if (srcNew && !dstNew) {
            return;
        }
        if (!srcNew && !dstNew) {
            if (srcEntity instanceof Versioned) {
                int srcVer = Numbers.nullToZero(((Versioned) srcEntity).getVersion());
                int dstVer = Numbers.nullToZero(((Versioned) dstEntity).getVersion());
                if (srcVer < dstVer) {
                    return;
                }
            }
        }
        for (MetaProperty property : metadata.getClassNN(srcEntity.getClass()).getProperties()) {
            String name = property.getName();
            if ((!property.getRange().isClass() || property.getRange().getCardinality().isMany()) // local and collections
                    && !property.isReadOnly()                                                     // read-write
                    && (srcNew || entityStates.isLoaded(srcEntity, name))) {                      // loaded
                AnnotatedElement annotatedElement = property.getAnnotatedElement();
                if (annotatedElement instanceof Field) {
                    Field field = (Field) annotatedElement;
                    field.setAccessible(true);
                    try {
                        Object value = field.get(srcEntity);
                        if (srcNew || property.getRange().getCardinality().isMany()) {
                            if (value != null)
                                copyValue(dstEntity, field, value);
                        } else {
                            copyValue(dstEntity, field, value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error copying state of attribute " + name, e);
                    }
                }
            }
        }
        if (!srcNew && dstNew) {
            if (dstEntity instanceof BaseGenericIdEntity) {
                BaseEntityInternalAccess.setNew((BaseGenericIdEntity) dstEntity, false);
                BaseEntityInternalAccess.setDetached((BaseGenericIdEntity) dstEntity, true);
            } else if (dstEntity instanceof AbstractNotPersistentEntity) {
                BaseEntityInternalAccess.setNew((AbstractNotPersistentEntity) dstEntity, false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void copyValue(Object dstObject, Field field, Object srcValue) throws IllegalAccessException {
        if (srcValue instanceof Collection) {
            Collection srcCollection = (Collection) srcValue;
            Collection dstCollection = (Collection) field.get(dstObject);
            Collection newDstCollection = srcValue instanceof List ? new ArrayList() : new LinkedHashSet();
            if (dstCollection == null) {
                newDstCollection.addAll(srcCollection);
            } else {
                newDstCollection.addAll(dstCollection);
                for (Object o : srcCollection) {
                    if (!newDstCollection.contains(o))
                        newDstCollection.add(o);
                }
            }
            field.set(dstObject, newDstCollection);
        } else {
            field.set(dstObject, srcValue);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void remove(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        if (contains(entity)) {
            modifiedInstances.remove(entity);
            removedInstances.add(entity);
            entity.removePropertyChangeListener(changeListener);
        }
    }

    @Override
    public void evict(Entity entity) {

    }

    @Override
    public boolean hasChanges() {
        return !(modifiedInstances.isEmpty() && removedInstances.isEmpty());
    }

    @Override
    public void commit() {
        PreCommitEvent preCommitEvent = new PreCommitEvent(this, modifiedInstances, removedInstances);
        eventRouter.fireEvent(PreCommitListener.class, PreCommitListener::preCommit, preCommitEvent);
        if (preCommitEvent.isCommitPrevented())
            return;

        Set<Entity> committed = performCommit();

        PostCommitEvent postCommitEvent = new PostCommitEvent(this, committed);
        eventRouter.fireEvent(PostCommitListener.class, PostCommitListener::postCommit, postCommitEvent);

        mergeCommitted(committed);
    }

    @Override
    public void addPreCommitListener(PreCommitListener listener) {
        eventRouter.addListener(PreCommitListener.class, listener);
    }

    @Override
    public void removePreCommitListener(PreCommitListener listener) {
        eventRouter.removeListener(PreCommitListener.class, listener);
    }

    @Override
    public void addPostCommitListener(PostCommitListener listener) {
        eventRouter.addListener(PostCommitListener.class, listener);
    }

    @Override
    public void removePostCommitListener(PostCommitListener listener) {
        eventRouter.removeListener(PostCommitListener.class, listener);
    }

    protected Set<Entity> performCommit() {
        if (!hasChanges())
            return Collections.emptySet();

        if (parentContext == null) {
            return commitToDataManager();
        } else {
            return commitToParentContext();
        }
    }

    protected Set<Entity> commitToDataManager() {
        CommitContext commitContext = new CommitContext(modifiedInstances, removedInstances);
        return dataManager.commit(commitContext);
    }

    protected Set<Entity> commitToParentContext() {
        HashSet<Entity> committedEntities = new HashSet<>();
        for (Entity entity : modifiedInstances) {
            committedEntities.add(parentContext.merge(entity));
        }
        for (Entity entity : removedInstances) {
            parentContext.remove(entity);
        }
        return committedEntities;
    }

    protected void mergeCommitted(Set<Entity> committed) {
        for (Entity entity : committed) {
            if (contains(entity)) {
                merge(entity, false);
            }
        }
    }

    protected Collection<Entity> getAll() {
        List<Entity> resultList = new ArrayList<>();
        for (Map<Object, Entity> entityMap : content.values()) {
            resultList.addAll(entityMap.values());
        }
        return resultList;
    }

    protected class ChangeListener implements Instance.PropertyChangeListener {
        @Override
        public void propertyChanged(Instance.PropertyChangeEvent e) {
            if (!disableListeners) {
                modifiedInstances.add((Entity) e.getItem());
            }
        }
    }

    protected class MergingAttributeVisitor implements EntityAttributeVisitor {

        @Override
        public boolean skip(MetaProperty property) {
            return !property.getRange().isClass() || property.isReadOnly();
        }

        @Override
        public void visit(Entity e, MetaProperty property) {
            if (!entityStates.isLoaded(e, property.getName()))
                return;
            Object value = e.getValue(property.getName());
            if (value != null) {
                if (value instanceof Collection) {
                    if (value instanceof List) {
                        mergeList((List) value, e, property.getName());
                    } else if (value instanceof Set) {
                        mergeSet((Set) value, e, property.getName());
                    } else {
                        throw new UnsupportedOperationException("Unsupported collection type: " + value.getClass().getName());
                    }
                } else {
                    mergeInstance((Entity) value, e, property.getName());
                }
            }
        }

        @SuppressWarnings("unchecked")
        protected void mergeList(List list, Entity owningEntity, String propertyName) {
            for (ListIterator<Entity> it = list.listIterator(); it.hasNext();) {
                Entity entity = it.next();
                Entity managed = internalMerge(entity);
                if (managed != entity) {
                    it.set(managed);
                }
            }
            if (!(list instanceof ObservableList)) {
                ObservableList observableList = new ObservableList<>(list, () -> modified(owningEntity));
                ((AbstractInstance) owningEntity).setValue(propertyName, observableList, false);
            }
        }

        @SuppressWarnings("unchecked")
        protected void mergeSet(Set set, Entity owningEntity, String propertyName) {
            for (Entity entity : new ArrayList<Entity>(set)) {
                Entity managed = internalMerge(entity);
                if (managed != entity) {
                    set.remove(entity);
                    set.add(managed);
                }
            }
            if (!(set instanceof ObservableList)) {
                ObservableSet observableSet = new ObservableSet<>(set, () -> modified(owningEntity));
                ((AbstractInstance) owningEntity).setValue(propertyName, observableSet, false);
            }
        }

        protected void mergeInstance(Entity entity, Entity owningEntity, String propertyName) {
            Entity managed = internalMerge(entity);
            if (managed != entity) {
                ((AbstractInstance) owningEntity).setValue(propertyName, managed, false);
            }
        }

        protected void modified(Entity entity) {
            if (!disableListeners) {
                modifiedInstances.add(entity);
            }
        }
    }
}
