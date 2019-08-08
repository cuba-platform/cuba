/*
 * Copyright (c) 2008-2018 Haulmont.
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
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.persistence.FetchGroupUtils;
import com.haulmont.cuba.gui.model.DataContext;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Standard implementation of {@link DataContext} which commits data to {@link DataManager}.
 */
public class DataContextImpl implements DataContext {

    private static final Logger log = LoggerFactory.getLogger(DataContextImpl.class);

    protected ApplicationContext applicationContext;

    protected EventHub events = new EventHub();

    protected Map<Class<?>, Map<Object, Entity>> content = new HashMap<>();

    protected Set<Entity> modifiedInstances = new HashSet<>();

    protected Set<Entity> removedInstances = new HashSet<>();

    protected PropertyChangeListener propertyChangeListener = new PropertyChangeListener();

    protected boolean disableListeners;

    protected DataContextImpl parentContext;

    protected Function<CommitContext, Set<Entity>> commitDelegate;

    protected Map<Entity, Map<String, EmbeddedPropertyChangeListener>> embeddedPropertyListeners = new WeakHashMap<>();

    public DataContextImpl(ApplicationContext applicationContext) {
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

    @Nullable
    @Override
    public DataContext getParent() {
        return parentContext;
    }

    @Override
    public void setParent(DataContext parentContext) {
        checkNotNullArgument(parentContext, "parentContext is null");
        if (!(parentContext instanceof DataContextImpl)) {
            throw new IllegalArgumentException("Unsupported DataContext type: " + parentContext.getClass().getName());
        }
        this.parentContext = (DataContextImpl) parentContext;
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
        if (entityMap != null) {
            return (T) entityMap.get(entityId);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends Entity> T find(T entity) {
        checkNotNullArgument(entity, "entity is null");
        return (T) find(entity.getClass(), entity.getId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Entity entity) {
        checkNotNullArgument(entity, "entity is null");
        return find(entity.getClass(), entity.getId()) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T merge(T entity) {
        checkNotNullArgument(entity, "entity is null");

        disableListeners = true;
        T result;
        try {
            Set<Entity> merged = Sets.newIdentityHashSet();
            result = (T) internalMerge(entity, merged, true);
        } finally {
            disableListeners = false;
        }
        return result;
    }

    @Override
    public EntitySet merge(Collection<? extends Entity> entities) {
        checkNotNullArgument(entities, "entity collection is null");

        List<Entity> managedList = new ArrayList<>(entities.size());
        disableListeners = true;
        try {
            Set<Entity> merged = Sets.newIdentityHashSet();

            for (Entity entity : entities) {
                Entity managed = internalMerge(entity, merged, true);
                managedList.add(managed);
            }
        } finally {
            disableListeners = false;
        }
        return EntitySet.of(managedList);
    }

    protected Entity internalMerge(Entity entity, Set<Entity> mergedSet, boolean isRoot) {
        Map<Object, Entity> entityMap = content.computeIfAbsent(entity.getClass(), aClass -> new HashMap<>());
        Entity managed = entityMap.get(entity.getId());

        if (!isRoot && mergedSet.contains(entity)) {
            if (managed != null) {
                return managed;
            } else {
                // should never happen
                log.debug("Instance was merged but managed instance is null: {}", entity);
            }
        }
        mergedSet.add(entity);

        if (managed == null) {
            managed = copyEntity(entity);
            entityMap.put(managed.getId(), managed);

            mergeState(entity, managed, mergedSet, isRoot);

            managed.addPropertyChangeListener(propertyChangeListener);

            if (getEntityStates().isNew(managed)) {
                modifiedInstances.add(managed);
                fireChangeListener(managed);
            }
            return managed;
        } else {
            if (managed.getId() == null) {
                throw new IllegalStateException("DataContext already contains an instance with null id: " + managed);
            }

            if (managed != entity) {
                mergeState(entity, managed, mergedSet, isRoot);
            }
            return managed;
        }
    }

    protected Entity copyEntity(Entity srcEntity) {
        Entity dstEntity;
        try {
            dstEntity = srcEntity.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + srcEntity.getClass(), e);
        }
        copySystemState(srcEntity, dstEntity);
        return dstEntity;
    }

    protected void mergeState(Entity srcEntity, Entity dstEntity, Set<Entity> mergedSet, boolean isRoot) {
        EntityStates entityStates = getEntityStates();

        boolean srcNew = entityStates.isNew(srcEntity);
        boolean dstNew = entityStates.isNew(dstEntity);

        mergeSystemState(srcEntity, dstEntity, isRoot);

        MetaClass metaClass = getMetadata().getClassNN(srcEntity.getClass());

        for (MetaProperty property : metaClass.getProperties()) {
            String propertyName = property.getName();
            if (!property.getRange().isClass()                                             // local
                    && !property.isReadOnly()                                              // read-write
                    && (srcNew || entityStates.isLoaded(srcEntity, propertyName))          // loaded src
                    && (dstNew || entityStates.isLoaded(dstEntity, propertyName))) {       // loaded dst

                Object value = srcEntity.getValue(propertyName);

                // ignore null values in non-root source entities
                if (!isRoot && value == null) {
                    continue;
                }

                dstEntity.setValue(propertyName, value);
            }
        }

        for (MetaProperty property : metaClass.getProperties()) {
            String propertyName = property.getName();
            if (property.getRange().isClass()                                              // refs and collections
                    && !property.isReadOnly()                                              // read-write
                    && (srcNew || entityStates.isLoaded(srcEntity, propertyName))          // loaded src
                    && (dstNew || entityStates.isLoaded(dstEntity, propertyName))) {       // loaded dst

                Object value = srcEntity.getValue(propertyName);

                // ignore null values in non-root source entities
                if (!isRoot && value == null) {
                    continue;
                }

                if (value == null) {
                    dstEntity.setValue(propertyName, null);
                    continue;
                }

                if (value instanceof Collection) {
                    if (value instanceof List) {
                        mergeList((List) value, dstEntity, property.getName(), isRoot, mergedSet);
                    } else if (value instanceof Set) {
                        mergeSet((Set) value, dstEntity, property.getName(), isRoot, mergedSet);
                    } else {
                        throw new UnsupportedOperationException("Unsupported collection type: " + value.getClass().getName());
                    }
                } else {
                    Entity srcRef = (Entity) value;
                    if (!mergedSet.contains(srcRef)) {
                        Entity managedRef = internalMerge(srcRef, mergedSet, false);
                        ((AbstractInstance) dstEntity).setValue(propertyName, managedRef, false);
                        if (getMetadataTools().isEmbedded(property)) {
                            EmbeddedPropertyChangeListener listener = new EmbeddedPropertyChangeListener(dstEntity);
                            managedRef.addPropertyChangeListener(listener);
                            embeddedPropertyListeners.computeIfAbsent(dstEntity, e -> new HashMap<>()).put(propertyName, listener);
                        }
                    } else {
                        Entity managedRef = find(srcRef.getClass(), srcRef.getId());
                        if (managedRef != null) {
                            ((AbstractInstance) dstEntity).setValue(propertyName, managedRef, false);
                        } else {
                            // should never happen
                            log.debug("Instance was merged but managed instance is null: {}", srcRef);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void copySystemState(Entity srcEntity, Entity dstEntity) {
        if (dstEntity instanceof BaseGenericIdEntity) {
            ((BaseGenericIdEntity) dstEntity).setId(srcEntity.getId());

            BaseEntityInternalAccess.copySystemState((BaseGenericIdEntity) srcEntity, (BaseGenericIdEntity) dstEntity);

            if (srcEntity instanceof FetchGroupTracker && dstEntity instanceof FetchGroupTracker) {
                FetchGroup srcFetchGroup = ((FetchGroupTracker) srcEntity)._persistence_getFetchGroup();
                ((FetchGroupTracker) dstEntity)._persistence_setFetchGroup(srcFetchGroup);
            }

        } else if (dstEntity instanceof AbstractNotPersistentEntity) {
            ((AbstractNotPersistentEntity) dstEntity).setId((UUID) srcEntity.getId());
            BaseEntityInternalAccess.setNew((AbstractNotPersistentEntity) dstEntity, BaseEntityInternalAccess.isNew((AbstractNotPersistentEntity) srcEntity));
        }

        if (dstEntity instanceof Versioned) {
            ((Versioned) dstEntity).setVersion(((Versioned) srcEntity).getVersion());
        }
    }

    protected void mergeSystemState(Entity srcEntity, Entity dstEntity, boolean isRoot) {
        if (dstEntity instanceof BaseGenericIdEntity) {
            if (isRoot) {
                BaseEntityInternalAccess.copySystemState((BaseGenericIdEntity) srcEntity, (BaseGenericIdEntity) dstEntity);
            }

            if (srcEntity instanceof FetchGroupTracker && dstEntity instanceof FetchGroupTracker) {
                FetchGroup srcFetchGroup = ((FetchGroupTracker) srcEntity)._persistence_getFetchGroup();
                FetchGroup dstFetchGroup = ((FetchGroupTracker) dstEntity)._persistence_getFetchGroup();
                if (srcFetchGroup == null || dstFetchGroup == null) {
                    ((FetchGroupTracker) dstEntity)._persistence_setFetchGroup(null);
                } else {
                    ((FetchGroupTracker) dstEntity)._persistence_setFetchGroup(FetchGroupUtils.mergeFetchGroups(srcFetchGroup, dstFetchGroup));
                }
            }
        } else if (dstEntity instanceof AbstractNotPersistentEntity) {
            BaseEntityInternalAccess.setNew((AbstractNotPersistentEntity) dstEntity, BaseEntityInternalAccess.isNew((AbstractNotPersistentEntity) srcEntity));
        }
    }

    protected void mergeList(List<Entity> list, Entity managedEntity, String propertyName, boolean replace,
                             Set<Entity> mergedSet) {
        if (replace) {
            List<Entity> managedRefs = new ArrayList<>(list.size());
            for (Entity entity : list) {
                Entity managedRef = internalMerge(entity, mergedSet, false);
                managedRefs.add(managedRef);
            }
            List<Entity> dstList = createObservableList(managedRefs, managedEntity);
            managedEntity.setValue(propertyName, dstList);

        } else {
            List<Entity> dstList = managedEntity.getValue(propertyName);
            if (dstList == null) {
                dstList = createObservableList(managedEntity);
                managedEntity.setValue(propertyName, dstList);
            }
            if (dstList.size() == 0) {
                for (Entity srcRef : list) {
                    dstList.add(internalMerge(srcRef, mergedSet, false));
                }
            } else {
                for (Entity srcRef : list) {
                    Entity managedRef = internalMerge(srcRef, mergedSet, false);
                    if (!dstList.contains(managedRef)) {
                        dstList.add(managedRef);
                    }
                }
            }
        }
    }

    protected void mergeSet(Set<Entity> set, Entity managedEntity, String propertyName, boolean replace,
                            Set<Entity> mergedSet) {
        if (replace) {
            Set<Entity> managedRefs = new LinkedHashSet<>(set.size());
            for (Entity entity : set) {
                Entity managedRef = internalMerge(entity, mergedSet, false);
                managedRefs.add(managedRef);
            }
            Set<Entity> dstList = createObservableSet(managedRefs, managedEntity);
            managedEntity.setValue(propertyName, dstList);

        } else {
            Set<Entity> dstSet = managedEntity.getValue(propertyName);
            if (dstSet == null) {
                dstSet = createObservableSet(managedEntity);
                managedEntity.setValue(propertyName, dstSet);
            }
            if (dstSet.size() == 0) {
                for (Entity srcRef : set) {
                    dstSet.add(internalMerge(srcRef, mergedSet, false));
                }
            } else {
                for (Entity srcRef : set) {
                    Entity managedRef = internalMerge(srcRef, mergedSet, false);
                    dstSet.add(managedRef);
                }
            }
        }
    }

    protected List<Entity> createObservableList(Entity notifiedEntity) {
        return createObservableList(new ArrayList<>(), notifiedEntity);
    }

    protected List<Entity> createObservableList(List<Entity> list, Entity notifiedEntity) {
        return new ObservableList<>(list, (changeType, changes) -> modified(notifiedEntity));
    }

    protected Set<Entity> createObservableSet(Entity notifiedEntity) {
        return createObservableSet(new LinkedHashSet<>(), notifiedEntity);
    }

    protected ObservableSet<Entity> createObservableSet(Set<Entity> set, Entity notifiedEntity) {
        return new ObservableSet<>(set, (changeType, changes) -> modified(notifiedEntity));
    }

    @Override
    public void remove(Entity entity) {
        checkNotNullArgument(entity, "entity is null");

        modifiedInstances.remove(entity);
        if (!getEntityStates().isNew(entity) || parentContext != null) {
            removedInstances.add(entity);
        }
        removeListeners(entity);
        fireChangeListener(entity);

        Map<Object, Entity> entityMap = content.get(entity.getClass());
        if (entityMap != null) {
            Entity mergedEntity = entityMap.get(entity.getId());
            if (mergedEntity != null) {
                entityMap.remove(entity.getId());
                removeFromCollections(mergedEntity);
            }
        }

        cleanupContextAfterRemoveEntity(this, entity);
    }

    protected void removeFromCollections(Entity entityToRemove) {
        for (Map.Entry<Class<?>, Map<Object, Entity>> entry : content.entrySet()) {
            Class<?> entityClass = entry.getKey();

            MetaClass metaClass = getMetadata().getClassNN(entityClass);
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                if (metaProperty.getRange().isClass()
                        && metaProperty.getRange().getCardinality().isMany()
                        && metaProperty.getRange().asClass().getJavaClass().isAssignableFrom(entityToRemove.getClass())) {

                    Map<Object, Entity> entityMap = entry.getValue();
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
        checkNotNullArgument(entity, "entity is null");

        Map<Object, Entity> entityMap = content.get(entity.getClass());
        if (entityMap != null) {
            Entity mergedEntity = entityMap.get(entity.getId());
            if (mergedEntity != null) {
                entityMap.remove(entity.getId());
                removeListeners(entity);
            }
            modifiedInstances.remove(entity);
            removedInstances.remove(entity);
        }
    }

    @Override
    public void evictAll() {
        Set<Entity> tmpModifiedInstances = new HashSet<>(modifiedInstances);
        Set<Entity> tmpRemovedInstances = new HashSet<>(removedInstances);

        for (Entity entity : tmpModifiedInstances) {
            evict(entity);
        }
        for (Entity entity : tmpRemovedInstances) {
            evict(entity);
        }
    }

    @Override
    public <T extends Entity> T create(Class<T> entityClass) {
        T entity = getMetadata().create(entityClass);
        return merge(entity);
    }

    protected void removeListeners(Entity entity) {
        entity.removePropertyChangeListener(propertyChangeListener);
        Map<String, EmbeddedPropertyChangeListener> listenerMap = embeddedPropertyListeners.get(entity);
        if (listenerMap != null) {
            for (Map.Entry<String, EmbeddedPropertyChangeListener> entry : listenerMap.entrySet()) {
                Entity embedded = entity.getValue(entry.getKey());
                if (embedded != null) {
                    embedded.removePropertyChangeListener(entry.getValue());
                    embedded.removePropertyChangeListener(propertyChangeListener);
                }
            }
            embeddedPropertyListeners.remove(entity);
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
    public void setModified(Entity entity, boolean modified) {
        Entity merged = find(entity);
        if (merged == null) {
            return;
        }
        if (modified) {
            modifiedInstances.add(merged);
        } else {
            modifiedInstances.remove(merged);
        }
    }

    @Override
    public Set<Entity> getModified() {
        return Collections.unmodifiableSet(modifiedInstances);
    }

    @Override
    public boolean isRemoved(Entity entity) {
        return removedInstances.contains(entity);
    }

    @Override
    public Set<Entity> getRemoved() {
        return Collections.unmodifiableSet(removedInstances);
    }

    @Override
    public EntitySet commit() {
        PreCommitEvent preCommitEvent = new PreCommitEvent(this, modifiedInstances, removedInstances);
        events.publish(PreCommitEvent.class, preCommitEvent);
        if (preCommitEvent.isCommitPrevented())
            return EntitySet.of(Collections.emptySet());

        Set<Entity> committed = performCommit(preCommitEvent.getValidationMode(), preCommitEvent.getValidationGroups());

        EntitySet committedAndMerged = mergeCommitted(committed);

        events.publish(PostCommitEvent.class, new PostCommitEvent(this, committedAndMerged));

        modifiedInstances.clear();
        removedInstances.clear();

        return committedAndMerged;
    }

    @Override
    public Subscription addPreCommitListener(Consumer<PreCommitEvent> listener) {
        return events.subscribe(PreCommitEvent.class, listener);
    }

    @Override
    public Subscription addPostCommitListener(Consumer<PostCommitEvent> listener) {
        return events.subscribe(PostCommitEvent.class, listener);
    }

    @Override
    public Function<CommitContext, Set<Entity>> getCommitDelegate() {
        return commitDelegate;
    }

    @Override
    public void setCommitDelegate(Function<CommitContext, Set<Entity>> delegate) {
        this.commitDelegate = delegate;
    }

    protected Set<Entity> performCommit(CommitContext.ValidationMode validationMode, List<Class> validationGroups) {
        if (!hasChanges())
            return Collections.emptySet();

        if (parentContext == null) {
            return commitToDataManager(validationMode, validationGroups);
        } else {
            return commitToParentContext();
        }
    }

    protected Set<Entity> commitToDataManager(CommitContext.ValidationMode validationMode, List<Class> validationGroups) {
        CommitContext commitContext = new CommitContext(
                filterCommittedInstances(modifiedInstances),
                filterCommittedInstances(removedInstances));
        if (validationMode != null)
            commitContext.setValidationMode(validationMode);
        if (validationGroups != null)
            commitContext.setValidationGroups(validationGroups);
        if (commitDelegate == null) {
            return getDataManager().commit(commitContext);
        } else {
            return commitDelegate.apply(commitContext);
        }
    }

    protected Collection filterCommittedInstances(Set<Entity> instances) {
        return instances.stream()
                .filter(entity -> !getMetadataTools().isEmbeddable(entity.getClass()))
                .collect(Collectors.toList());
    }

    protected Set<Entity> commitToParentContext() {
        HashSet<Entity> committedEntities = new HashSet<>();
        for (Entity entity : modifiedInstances) {
            Entity merged = parentContext.merge(entity);
            parentContext.modifiedInstances.add(merged);
            committedEntities.add(merged);
        }
        for (Entity entity : removedInstances) {
            parentContext.remove(entity);
            cleanupContextAfterRemoveEntity(parentContext, entity);
        }
        return committedEntities;
    }

    protected void cleanupContextAfterRemoveEntity(DataContextImpl context, Entity removedEntity) {
        EntityStates entityStates = getEntityStates();
        if (entityStates.isNew(removedEntity)) {
            for (Entity modifiedInstance : new ArrayList<>(context.modifiedInstances)) {
                if (entityStates.isNew(modifiedInstance) && entityHasReference(modifiedInstance, removedEntity)) {
                    context.modifiedInstances.remove(modifiedInstance);
                }
            }
        }
    }

    protected boolean entityHasReference(Entity entity, Entity refEntity) {
        MetaClass metaClass = getMetadata().getClassNN(entity.getClass());
        MetaClass refMetaClass = getMetadata().getClassNN(refEntity.getClass());

        return metaClass.getProperties().stream()
                .anyMatch(metaProperty ->
                            metaProperty.getRange().isClass()
                            && metaProperty.getRange().asClass().equals(refMetaClass)
                            && Objects.equals(entity.getValue(metaProperty.getName()), refEntity));
    }

    protected EntitySet mergeCommitted(Set<Entity> committed) {
        // transform into sorted collection to have reproducible behavior
        List<Entity> entitiesToMerge = new ArrayList<>();
        for (Entity entity : committed) {
            if (contains(entity)) {
                entitiesToMerge.add(entity);
            }
        }
        entitiesToMerge.sort(Comparator.comparing(Object::hashCode));

        return merge(entitiesToMerge);
    }

    public Collection<Entity> getAll() {
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
                // if id has been changed, update put the entity to the content with the new id
                MetaProperty primaryKeyProperty = getMetadataTools().getPrimaryKeyProperty(e.getItem().getClass());
                if (primaryKeyProperty != null && e.getProperty().equals(primaryKeyProperty.getName())) {
                    Map<Object, Entity> entityMap = content.get(e.getItem().getClass());
                    if (entityMap != null) {
                        entityMap.remove(e.getPrevValue());
                        entityMap.put(e.getValue(), (Entity) e.getItem());
                    }
                }

                modifiedInstances.add((Entity) e.getItem());
                fireChangeListener((Entity) e.getItem());
            }
        }
    }

    protected class EmbeddedPropertyChangeListener implements Instance.PropertyChangeListener {

        private final Entity entity;

        public EmbeddedPropertyChangeListener(Entity entity) {
            this.entity = entity;
        }

        @Override
        public void propertyChanged(Instance.PropertyChangeEvent e) {
            if (!disableListeners) {
                modifiedInstances.add(entity);
                fireChangeListener(entity);
            }
        }
    }
}
