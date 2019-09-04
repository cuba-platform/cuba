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

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.validation.EntityValidationException;
import com.haulmont.cuba.security.app.EntityLogAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Component(DataManager.NAME)
public class DataManagerBean implements DataManager {

    private static final Logger log = LoggerFactory.getLogger(DataManagerBean.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected EntityStates entityStates;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected StoreFactory storeFactory;

    @Inject
    protected EntityLogAPI entityLog;

    @Inject
    protected BeanValidation beanValidation;

    @Nullable
    @Override
    public <E extends Entity> E load(LoadContext<E> context) {
        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());
        DataStore storage = storeFactory.get(getStoreName(metaClass));
        E entity = storage.load(context);
        if (entity != null)
            readCrossDataStoreReferences(Collections.singletonList(entity), context.getView(), metaClass, context.isJoinTransaction());
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());
        DataStore storage = storeFactory.get(getStoreName(metaClass));
        List<E> entities = storage.loadList(context);
        readCrossDataStoreReferences(entities, context.getView(), metaClass, context.isJoinTransaction());
        return entities;
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());
        DataStore storage = storeFactory.get(getStoreName(metaClass));
        return storage.getCount(context);
    }

    @Override
    public <E extends Entity> E reload(E entity, String viewName) {
        Objects.requireNonNull(viewName, "viewName is null");
        return reload(entity, metadata.getViewRepository().getView(entity.getClass(), viewName));
    }

    @Override
    public <E extends Entity> E reload(E entity, View view) {
        return reload(entity, view, null);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass) {
        return reload(entity, view, metaClass, entityHasDynamicAttributes(entity));
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass, boolean loadDynamicAttributes) {
        if (metaClass == null) {
            metaClass = metadata.getSession().getClass(entity.getClass());
        }
        LoadContext<E> context = new LoadContext<>(metaClass);
        context.setId(entity.getId());
        context.setView(view);
        context.setLoadDynamicAttributes(loadDynamicAttributes);

        E reloaded = load(context);
        if (reloaded == null)
            throw new EntityAccessException(metaClass, entity.getId());

        return reloaded;
    }

    protected void validate(CommitContext context) {
        if (CommitContext.ValidationMode.DEFAULT == context.getValidationMode() && serverConfig.getDataManagerBeanValidation()
                || CommitContext.ValidationMode.ALWAYS_VALIDATE == context.getValidationMode()) {
            for (Entity entity : context.getCommitInstances()) {
                validateEntity(entity, context.getValidationGroups());
            }
        }
    }

    protected void validateEntity(Entity entity, List<Class> validationGroups) {
        Validator validator = beanValidation.getValidator();
        Set<ConstraintViolation<Entity>> violations;
        if (validationGroups == null || validationGroups.isEmpty()) {
            violations = validator.validate(entity);
        } else {
            violations = validator.validate(entity, validationGroups.toArray(new Class[0]));
        }
        if (!violations.isEmpty())
            throw new EntityValidationException(String.format("Entity %s validation failed.", entity.toString()), violations);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EntitySet commit(CommitContext context) {
        validate(context);

        Map<String, CommitContext> storeToContextMap = new TreeMap<>();
        Set<Entity> toRepeat = new HashSet<>();
        for (Entity entity : context.getCommitInstances()) {
            MetaClass metaClass = metadata.getClassNN(entity.getClass());
            String storeName = getStoreName(metaClass);

            boolean repeatRequired = writeCrossDataStoreReferences(entity, context.getCommitInstances());
            if (repeatRequired) {
                toRepeat.add(entity);
            }

            CommitContext cc = storeToContextMap.get(storeName);
            if (cc == null) {
                cc = createCommitContext(context);
                storeToContextMap.put(storeName, cc);
            }
            cc.getCommitInstances().add(entity);
            View view = context.getViews().get(entity);
            if (view != null)
                cc.getViews().put(entity, view);
        }
        for (Entity entity : context.getRemoveInstances()) {
            MetaClass metaClass = metadata.getClassNN(entity.getClass());
            String storeName = getStoreName(metaClass);

            CommitContext cc = storeToContextMap.get(storeName);
            if (cc == null) {
                cc = createCommitContext(context);
                storeToContextMap.put(storeName, cc);
            }
            cc.getRemoveInstances().add(entity);
            View view = context.getViews().get(entity);
            if (view != null)
                cc.getViews().put(entity, view);
        }

        Set<Entity> result = new LinkedHashSet<>();
        for (Map.Entry<String, CommitContext> entry : storeToContextMap.entrySet()) {
            DataStore dataStore = storeFactory.get(entry.getKey());
            Set<Entity> committed = dataStore.commit(entry.getValue());
            result.addAll(committed);
        }

        if (!toRepeat.isEmpty()) {
            boolean logging = entityLog.isLoggingForCurrentThread();
            entityLog.processLoggingForCurrentThread(false);
            try {
                CommitContext cc = new CommitContext();
                cc.setJoinTransaction(context.isJoinTransaction());
                for (Entity entity : result) {
                    if (toRepeat.contains(entity)) {
                        cc.addInstanceToCommit(entity, context.getViews().get(entity));
                    }
                }
                Set<Entity> committedEntities = commit(cc);
                for (Entity committedEntity : committedEntities) {
                    if (result.contains(committedEntity)) {
                        result.remove(committedEntity);
                        result.add(committedEntity);
                    }
                }
            } finally {
                entityLog.processLoggingForCurrentThread(logging);
            }
        }

        return EntitySet.of(result);
    }

    @Override
    public EntitySet commit(Entity... entities) {
        return commit(new CommitContext(entities));
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable View view) {
        return commit(new CommitContext().addInstanceToCommit(entity, view)).get(entity);
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable String viewName) {
        if (viewName != null) {
            View view = metadata.getViewRepository().getView(metadata.getClassNN(entity.getClass()), viewName);
            return commit(entity, view);
        } else {
            return commit(entity, (View) null);
        }
    }

    @Override
    public <E extends Entity> E commit(E entity) {
        return commit(entity, (View) null);
    }

    @Override
    public void remove(Entity entity) {
        CommitContext context = new CommitContext(
                Collections.<Entity>emptyList(),
                Collections.singleton(entity));
        commit(context);
    }

    @Override
    public List<KeyValueEntity> loadValues(ValueLoadContext context) {
        DataStore store = storeFactory.get(getStoreName(context.getStoreName()));
        return store.loadValues(context);
    }

    protected boolean entityHasDynamicAttributes(Entity entity) {
        return entity instanceof BaseGenericIdEntity
                && ((BaseGenericIdEntity) entity).getDynamicAttributes() != null;
    }

    protected CommitContext createCommitContext(CommitContext context) {
        CommitContext newCtx = new CommitContext();
        newCtx.setSoftDeletion(context.isSoftDeletion());
        newCtx.setDiscardCommitted(context.isDiscardCommitted());
        newCtx.setAuthorizationRequired(context.isAuthorizationRequired());
        newCtx.setJoinTransaction(context.isJoinTransaction());
        newCtx.setValidationMode(context.getValidationMode());
        newCtx.setValidationGroups(context.getValidationGroups());
        return newCtx;
    }

    @Override
    public DataManager secure() {
        return new Secure(this, metadata);
    }

    @Override
    public <E extends Entity<K>, K> FluentLoader<E, K> load(Class<E> entityClass) {
        return new FluentLoader<>(entityClass, this);
    }

    @Override
    public FluentValuesLoader loadValues(String queryString) {
        return new FluentValuesLoader(queryString, this);
    }

    @Override
    public <T> FluentValueLoader<T> loadValue(String queryString, Class<T> valueClass) {
        return new FluentValueLoader<>(queryString, valueClass, this);
    }

    @Override
    public <T extends Entity> T create(Class<T> entityClass) {
        return metadata.create(entityClass);
    }

    @Override
    public <T extends BaseGenericIdEntity<K>, K> T getReference(Class<T> entityClass, K id) {
        T entity = metadata.create(entityClass);
        entity.setId(id);
        entityStates.makePatch(entity);
        return entity;
    }

    protected boolean writeCrossDataStoreReferences(Entity entity, Collection<Entity> allEntities) {
        if (Stores.getAdditional().isEmpty())
            return false;

        boolean repeatRequired = false;
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        for (MetaProperty property : metaClass.getProperties()) {
            if (property.getRange().isClass() && !property.getRange().getCardinality().isMany()) {
                MetaClass propertyMetaClass = property.getRange().asClass();
                if (!Objects.equals(metadataTools.getStoreName(propertyMetaClass), metadataTools.getStoreName(metaClass))) {
                    List<String> relatedProperties = metadataTools.getRelatedProperties(property);
                    if (relatedProperties.size() == 0) {
                        continue;
                    }
                    if (relatedProperties.size() > 1) {
                        log.warn("More than 1 related property is defined for attribute {}, skip handling different data store", property);
                        continue;
                    }
                    String relatedPropertyName = relatedProperties.get(0);
                    if (PersistenceHelper.isLoaded(entity, relatedPropertyName)) {
                        Entity refEntity = entity.getValue(property.getName());
                        if (refEntity == null) {
                            entity.setValue(relatedPropertyName, null);
                        } else {
                            Object refEntityId = refEntity.getId();
                            if (refEntityId instanceof IdProxy) {
                                Object realId = ((IdProxy) refEntityId).get();
                                if (realId == null) {
                                    if (allEntities.stream().anyMatch(e -> e.getId().equals(refEntityId))) {
                                        repeatRequired = true;
                                    } else {
                                        log.warn("No entity with ID={} in the context, skip handling different data store", refEntityId);
                                    }
                                } else {
                                    entity.setValue(relatedPropertyName, realId);
                                }
                            } else if (refEntityId instanceof EmbeddableEntity) {
                                MetaProperty relatedProperty = metaClass.getPropertyNN(relatedPropertyName);
                                if (!relatedProperty.getRange().isClass()) {
                                    log.warn("PK of entity referenced by {} is a EmbeddableEntity, but related property {} is not", property, relatedProperty);
                                } else {
                                    entity.setValue(relatedPropertyName, metadataTools.copy((Entity) refEntityId));
                                }
                            } else {
                                entity.setValue(relatedPropertyName, refEntityId);
                            }
                        }
                    }
                }
            }
        }
        return repeatRequired;
    }

    protected void readCrossDataStoreReferences(Collection<? extends Entity> entities, View view, MetaClass metaClass,
                                                boolean joinTransaction) {
        if (Stores.getAdditional().isEmpty() || entities.isEmpty() || view == null)
            return;

        CrossDataStoreReferenceLoader crossDataStoreReferenceLoader = AppBeans.getPrototype(
                CrossDataStoreReferenceLoader.NAME, metaClass, view, joinTransaction);
        crossDataStoreReferenceLoader.processEntities(entities);
    }

    protected String getStoreName(MetaClass metaClass) {
        return getStoreName(metadata.getTools().getStoreName(metaClass));
    }

    protected String getStoreName(@Nullable String storeName) {
        return storeName == null ? StoreFactory.NULL_NAME : storeName;
    }

    private static class Secure extends DataManagerBean {

        private DataManager dataManager;

        public Secure(DataManager dataManager, Metadata metadata) {
            this.dataManager = dataManager;
            //noinspection ReassignmentInjectVariable
            this.metadata = metadata;
        }

        @Nullable
        @Override
        public <E extends Entity> E load(LoadContext<E> context) {
            context.setAuthorizationRequired(true);
            return dataManager.load(context);
        }

        @Override
        public <E extends Entity> List<E> loadList(LoadContext<E> context) {
            context.setAuthorizationRequired(true);
            return dataManager.loadList(context);
        }

        @Override
        public List<KeyValueEntity> loadValues(ValueLoadContext context) {
            context.setAuthorizationRequired(true);
            return dataManager.loadValues(context);
        }

        @Override
        public long getCount(LoadContext<? extends Entity> context) {
            context.setAuthorizationRequired(true);
            return dataManager.getCount(context);
        }

        @Override
        public EntitySet commit(CommitContext context) {
            context.setAuthorizationRequired(true);
            return dataManager.commit(context);
        }
    }
}