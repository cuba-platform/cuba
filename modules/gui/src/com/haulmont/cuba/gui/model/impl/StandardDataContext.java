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

import com.google.common.collect.Sets;
import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Numbers;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.model.CollectionChangeType;
import com.haulmont.cuba.gui.model.DataContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 */
public class StandardDataContext implements DataContext {

    private ApplicationContext applicationContext;

    protected EventHub events = new EventHub();

    protected Map<Class<?>, Map<Object, Entity>> content = new HashMap<>();

    protected Set<Entity> modifiedInstances = new HashSet<>();

    protected Set<Entity> removedInstances = new HashSet<>();

    protected PropertyChangeListener propertyChangeListener = new PropertyChangeListener();

    protected boolean disableListeners;

    protected StandardDataContext parentContext;

    public StandardDataContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected Metadata getMetadata() {
        return applicationContext.getBean(Metadata.NAME, Metadata.class);
    }

    protected MetadataTools getMetadataTools() {
        return applicationContext.getBean(MetadataTools.NAME, MetadataTools.class);
    }

    protected EntityStates getEntityStates() {
        return applicationContext.getBean(EntityStates.NAME, EntityStates.class);
    }

    protected DataManager getDataManager() {
        return applicationContext.getBean(DataManager.NAME, DataManager.class);
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
            Entity copy = copyGraph(entity, new HashMap<>());
            merge(copy);
        }
    }

    @Override
    public Subscription addChangeListener(Consumer<ChangeEvent> listener) {
        return events.subscribe(ChangeEvent.class, listener);
    }

    protected void fireChangeListener(Entity entity) {
        events.publish(ChangeEvent.class, new ChangeEvent(this, entity));
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
    public boolean contains(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
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
                getMetadataTools().traverseAttributes(entity, new MergingAttributeVisitor());
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
                copyReferences(entity, managedInstance);
            }
            return managedInstance;

        } else {
            mergeReferences(entity);

            entityMap.put(entity.getId(), entity);

            entity.addPropertyChangeListener(propertyChangeListener);

            if (getEntityStates().isNew(entity)) {
                modifiedInstances.add(entity);
                fireChangeListener(entity);
            }
            return entity;
        }
    }

    protected void copyReferences(Entity srcEntity, Entity dstEntity) {
        EntityStates entityStates = getEntityStates();

        for (MetaProperty property : getMetadata().getClassNN(srcEntity.getClass()).getProperties()) {
            String propertyName = property.getName();
            if (!property.getRange().isClass()
                    || property.getRange().getCardinality().isMany()
                    || property.isReadOnly()
                    || !entityStates.isLoaded(srcEntity, propertyName)
                    || !entityStates.isLoaded(dstEntity, propertyName)) {
                continue;
            }
            Object value = srcEntity.getValue(propertyName);
            if (!entityStates.isNew(srcEntity) || value != null) {
                if (value == null) {
                    dstEntity.setValue(propertyName, null);
                } else {
                    Entity srcRef = (Entity) value;
                    Entity dstRef = internalMerge(srcRef);
                    ((AbstractInstance) dstEntity).setValue(propertyName, dstRef, false);
                }
            }
        }
    }

    protected void mergeReferences(Entity entity) {
        EntityStates entityStates = getEntityStates();

        for (MetaProperty property : getMetadata().getClassNN(entity.getClass()).getProperties()) {
            String propertyName = property.getName();
            if (!property.getRange().isClass()
                    || property.getRange().getCardinality().isMany()
                    || property.isReadOnly()
                    || !entityStates.isLoaded(entity, propertyName)) {
                continue;
            }
            Object value = entity.getValue(propertyName);
            if (value != null) {
                Entity srcRef = (Entity) value;
                Entity dstRef = internalMerge(srcRef);
                ((AbstractInstance) entity).setValue(propertyName, dstRef, false);
            }
        }
    }

    /*
     * (1) src.new -> dst.new : copy all non-null                                   - should not happen (happens in setParent?)
     * (2) src.new -> dst.det : do nothing                                          - should not happen
     * (3) src.det -> dst.new : copy all loaded, make detached                      - normal situation after commit
     * (4) src.det -> dst.det : if src.version >= dst.version, copy all loaded      - normal situation after commit (and in setParent?)
     *                          if src.version < dst.version, do nothing            - should not happen
     */
    protected void copyState(Entity srcEntity, Entity dstEntity) {
        EntityStates entityStates = getEntityStates();

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
        for (MetaProperty property : getMetadata().getClassNN(srcEntity.getClass()).getProperties()) {
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
            copySystemState(srcEntity, dstEntity);
        }
    }

    /**
     * Creates a deep copy of the given graph.
     *
     * @param srcEntity source entity
     * @param copied    map of already copied instances to their copies
     * @return          copy of the given graph
     */
    @SuppressWarnings("unchecked")
    protected Entity copyGraph(Entity srcEntity, Map<Entity, Entity> copied) {
        Entity existingCopy = copied.get(srcEntity);
        if (existingCopy != null)
            return existingCopy;

        EntityStates entityStates = getEntityStates();
        boolean srcNew = entityStates.isNew(srcEntity);

        Entity dstEntity;
        try {
            dstEntity = srcEntity.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot create an instance of " + srcEntity.getClass(), e);
        }
        copyIdAndVersion(srcEntity, dstEntity);

        copied.put(srcEntity, dstEntity);

        for (MetaProperty property : getMetadata().getClassNN(srcEntity.getClass()).getProperties()) {
            String name = property.getName();
            if (!property.isReadOnly()
                    && (srcNew || entityStates.isLoaded(srcEntity, name))) {
                AnnotatedElement annotatedElement = property.getAnnotatedElement();
                if (annotatedElement instanceof Field) {
                    Field field = (Field) annotatedElement;
                    field.setAccessible(true);
                    try {
                        Object value = field.get(srcEntity);
                        Object newValue;
                        if (value != null) {
                            if (!property.getRange().isClass()) {
                                newValue = value;
                            } else if (!property.getRange().getCardinality().isMany()) {
                                newValue = copyGraph((Entity) value, copied);
                            } else {
                                Collection dstCollection = value instanceof List ? new ArrayList() : new LinkedHashSet();
                                for (Object item : (Collection) value) {
                                    dstCollection.add(copyGraph((Entity) item, copied));
                                }
                                newValue = dstCollection;
                            }
                            if (newValue != null) {
                                field.set(dstEntity, newValue);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error copying state of attribute " + name, e);
                    }
                }
            }
        }
        copySystemState(srcEntity, dstEntity);
        return dstEntity;
    }

    @SuppressWarnings("unchecked")
    protected void copyIdAndVersion(Entity srcEntity, Entity dstEntity) {
        if (dstEntity instanceof BaseGenericIdEntity)
            ((BaseGenericIdEntity) dstEntity).setId(srcEntity.getId());
        else if (dstEntity instanceof AbstractNotPersistentEntity)
            ((AbstractNotPersistentEntity) dstEntity).setId((UUID) srcEntity.getId());

        if (dstEntity instanceof Versioned) {
            ((Versioned) dstEntity).setVersion(((Versioned) srcEntity).getVersion());
        }
    }

    protected void copySystemState(Entity srcEntity, Entity dstEntity) {
        if (dstEntity instanceof BaseGenericIdEntity) {
            BaseEntityInternalAccess.setNew((BaseGenericIdEntity) dstEntity, BaseEntityInternalAccess.isNew((BaseGenericIdEntity) srcEntity));
            BaseEntityInternalAccess.setDetached((BaseGenericIdEntity) dstEntity, BaseEntityInternalAccess.isDetached((BaseGenericIdEntity) srcEntity));
        } else if (dstEntity instanceof AbstractNotPersistentEntity) {
            BaseEntityInternalAccess.setNew((AbstractNotPersistentEntity) dstEntity, BaseEntityInternalAccess.isNew((BaseGenericIdEntity) srcEntity));
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
            BiConsumer<CollectionChangeType, Collection> onChanged = (changeType, changed) -> modified((Entity) dstObject);
            Collection observable = newDstCollection instanceof List ?
                    new ObservableList(((List) newDstCollection), onChanged) :
                    new ObservableSet(((Set) newDstCollection), onChanged);
            field.set(dstObject, observable);
        } else {
            field.set(dstObject, srcValue);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void remove(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        Map<Object, Entity> entityMap = content.get(entity.getClass());
        if (entityMap != null) {
            Entity mergedEntity = entityMap.get(entity.getId());
            if (mergedEntity != null) {
                modifiedInstances.remove(entity);
                removedInstances.add(entity);
                entityMap.remove(entity.getId());
                entity.removePropertyChangeListener(propertyChangeListener);
                fireChangeListener(entity);
                removeFromCollections(mergedEntity);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void removeFromCollections(Entity entityToRemove) {
        for (Class<?> entityClass : content.keySet()) {
            MetaClass metaClass = getMetadata().getClassNN(entityClass);
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                if (metaProperty.getRange().isClass()
                        && metaProperty.getRange().getCardinality().isMany()
                        && metaProperty.getRange().asClass().getJavaClass().isAssignableFrom(entityToRemove.getClass())) {
                    Map<Object, Entity> entityMap = content.get(entityClass);
                    for (Entity entity : entityMap.values()) {
                        if (getEntityStates().isLoaded(entity, metaProperty.getName())) {
                            Collection collection = entity.getValue(metaProperty.getName());
                            if (collection != null) {
                                collection.remove(entityToRemove);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void evict(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        Map<Object, Entity> entityMap = content.get(entity.getClass());
        if (entityMap != null) {
            Entity mergedEntity = entityMap.get(entity.getId());
            if (mergedEntity != null) {
                entityMap.remove(entity.getId());
                entity.removePropertyChangeListener(propertyChangeListener);
            }
        }
    }

    @Override
    public boolean hasChanges() {
        return !(modifiedInstances.isEmpty() && removedInstances.isEmpty());
    }

    @Override
    public boolean isModified(Entity entity) {
        return modifiedInstances.contains(entity);
    }

    @Override
    public boolean isRemoved(Entity entity) {
        return removedInstances.contains(entity);
    }

    @Override
    public void commit() {
        PreCommitEvent preCommitEvent = new PreCommitEvent(this, modifiedInstances, removedInstances);
        events.publish(PreCommitEvent.class, preCommitEvent);
        if (preCommitEvent.isCommitPrevented())
            return;

        Set<Entity> committed = performCommit();

        events.publish(PostCommitEvent.class, new PostCommitEvent(this, committed));

        mergeCommitted(committed);

        modifiedInstances.clear();
        removedInstances.clear();
    }

    @Override
    public Subscription addPreCommitListener(Consumer<PreCommitEvent> listener) {
        return events.subscribe(PreCommitEvent.class, listener);
    }

    @Override
    public Subscription addPostCommitListener(Consumer<PostCommitEvent> listener) {
        return events.subscribe(PostCommitEvent.class, listener);
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
        return getDataManager().commit(commitContext);
    }

    protected Set<Entity> commitToParentContext() {
        HashSet<Entity> committedEntities = new HashSet<>();
        for (Entity entity : modifiedInstances) {
            Entity merged = parentContext.merge(entity, false);
            parentContext.modifiedInstances.add(merged);
            committedEntities.add(merged);
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

    protected void modified(Entity entity) {
        if (!disableListeners) {
            modifiedInstances.add(entity);
            fireChangeListener(entity);
        }
    }

    public String printContent() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Class<?>, Map<Object, Entity>> entry : content.entrySet()) {
            sb.append("=== ").append(entry.getKey().getSimpleName()).append(" ===\n");
            for (Entity entity : entry.getValue().values()) {
                sb.append(printEntity(entity, 1, Sets.newIdentityHashSet())).append('\n');
            }
        }
        return sb.toString();
    }

    protected String printEntity(Entity entity, int level, Set<Entity> visited) {
        StringBuilder sb = new StringBuilder();
        sb.append(printObject(entity)).append(" ").append(entity.toString()).append("\n");

        if (visited.contains(entity)) {
            return sb.toString();
        }
        visited.add(entity);

        for (MetaProperty property : getMetadata().getClassNN(entity.getClass()).getProperties()) {
            if (!property.getRange().isClass() || !getEntityStates().isLoaded(entity, property.getName()))
                continue;
            Object value = entity.getValue(property.getName());
            String prefix = StringUtils.repeat("  ", level);
            if (value instanceof Entity) {
                String str = printEntity((Entity) value, level + 1, visited);
                if (!str.equals(""))
                    sb.append(prefix).append(str);
            } else if (value instanceof Collection) {
                sb.append(prefix).append(value.getClass().getSimpleName()).append("[\n");
                for (Object item : (Collection) value) {
                    String str = printEntity((Entity) item, level + 1, visited);
                    if (!str.equals(""))
                        sb.append(prefix).append(str);
                }
                sb.append(prefix).append("]\n");
            }
        }
        return sb.toString();
    }

    protected String printObject(Object object) {
        return "{" + object.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(object)) + "}";
    }

    protected class PropertyChangeListener implements Instance.PropertyChangeListener {
        @Override
        public void propertyChanged(Instance.PropertyChangeEvent e) {
            if (!disableListeners) {
                modifiedInstances.add((Entity) e.getItem());
                fireChangeListener((Entity) e.getItem());
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
            if (!getEntityStates().isLoaded(e, property.getName()))
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
                ObservableList observableList = new ObservableList<>(list, (changeType, changes) -> modified(owningEntity));
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
                ObservableSet observableSet = new ObservableSet<>(set, (changeType, changes) -> modified(owningEntity));
                ((AbstractInstance) owningEntity).setValue(propertyName, observableSet, false);
            }
        }

        protected void mergeInstance(Entity entity, Entity owningEntity, String propertyName) {
            Entity managed = internalMerge(entity);
            if (managed != entity) {
                ((AbstractInstance) owningEntity).setValue(propertyName, managed, false);
            }
        }
    }
}
