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

package com.haulmont.cuba.core.sys.persistence;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.haulmont.bali.util.StackTrace;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.FtsSender;
import com.haulmont.cuba.core.app.MiddlewareStatisticsAccumulator;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.listener.AfterCompleteTransactionListener;
import com.haulmont.cuba.core.listener.BeforeCommitTransactionListener;
import com.haulmont.cuba.core.sys.EntityFetcher;
import com.haulmont.cuba.core.sys.entitycache.QueryCacheManager;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.core.sys.listener.EntityListenerType;
import com.haulmont.cuba.security.app.EntityLogAPI;
import org.eclipse.persistence.descriptors.changetracking.ChangeTracker;
import org.eclipse.persistence.internal.descriptors.changetracking.AttributeChangeListener;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.internal.sessions.ObjectChangeSet;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.ResourceHolderSynchronization;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Component(PersistenceImplSupport.NAME)
public class PersistenceImplSupport implements ApplicationContextAware {

    public static final String NAME = "cuba_PersistenceImplSupport";

    public static final String RESOURCE_HOLDER_KEY = ContainerResourceHolder.class.getName();

    public static final String PROP_NAME = "cuba.storeName";

    @Inject
    protected Persistence persistence;

    @Inject
    protected EntityListenerManager entityListenerManager;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Events events;

    @Inject
    protected EntityFetcher entityFetcher;

    @Inject
    protected QueryCacheManager queryCacheManager;

    @Inject
    protected EntityLogAPI entityLog;

    protected volatile FtsSender ftsSender;

    @Inject
    protected OrmCacheSupport ormCacheSupport;

    @Inject
    protected MiddlewareStatisticsAccumulator statisticsAccumulator;

    @Inject
    protected EntityChangedEventManager entityChangedEventManager;

    protected List<BeforeCommitTransactionListener> beforeCommitTxListeners;

    protected List<AfterCompleteTransactionListener> afterCompleteTxListeners;

    private static final Logger log = LoggerFactory.getLogger(PersistenceImplSupport.class.getName());

    private Logger implicitFlushLog = LoggerFactory.getLogger("com.haulmont.cuba.IMPLICIT_FLUSH");

    protected static Set<Entity> createEntitySet() {
        return Sets.newIdentityHashSet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, BeforeCommitTransactionListener> beforeCommitMap = applicationContext.getBeansOfType(BeforeCommitTransactionListener.class);
        beforeCommitTxListeners = new ArrayList<>(beforeCommitMap.values());
        beforeCommitTxListeners.sort(new OrderComparator());

        Map<String, AfterCompleteTransactionListener> afterCompleteMap = applicationContext.getBeansOfType(AfterCompleteTransactionListener.class);
        afterCompleteTxListeners = new ArrayList<>(afterCompleteMap.values());
        afterCompleteTxListeners.sort(new OrderComparator());
    }

    public void registerInstance(Entity entity, EntityManager entityManager) {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new RuntimeException("No transaction");

        UnitOfWork unitOfWork = entityManager.getDelegate().unwrap(UnitOfWork.class);
        getInstanceContainerResourceHolder(getStorageName(unitOfWork)).registerInstanceForUnitOfWork(entity, unitOfWork);

        if (entity instanceof BaseGenericIdEntity) {
            BaseEntityInternalAccess.setDetached((BaseGenericIdEntity) entity, false);
        }
    }

    public void registerInstance(Entity entity, AbstractSession session) {
        // Can be called outside of a transaction when fetching lazy attributes
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            return;

        if (!(session instanceof UnitOfWork))
            throw new RuntimeException("Session is not a UnitOfWork: " + session);

        getInstanceContainerResourceHolder(getStorageName(session)).registerInstanceForUnitOfWork(entity, (UnitOfWork) session);
    }

    public Collection<Entity> getInstances(EntityManager entityManager) {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new RuntimeException("No transaction");

        UnitOfWork unitOfWork = entityManager.getDelegate().unwrap(UnitOfWork.class);
        return getInstanceContainerResourceHolder(getStorageName(unitOfWork)).getInstances(unitOfWork);
    }

    public Collection<Entity> getSavedInstances(String storeName) {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new RuntimeException("No transaction");

        return getInstanceContainerResourceHolder(storeName).getSavedInstances();
    }

    public String getStorageName(Session session) {
        String storeName = (String) session.getProperty(PROP_NAME);
        return Strings.isNullOrEmpty(storeName) ? Stores.MAIN : storeName;
    }

    public ContainerResourceHolder getInstanceContainerResourceHolder(String storeName) {
        ContainerResourceHolder holder =
                (ContainerResourceHolder) TransactionSynchronizationManager.getResource(RESOURCE_HOLDER_KEY);
        if (holder == null) {
            holder = new ContainerResourceHolder(storeName);
            TransactionSynchronizationManager.bindResource(RESOURCE_HOLDER_KEY, holder);
        } else if (!storeName.equals(holder.getStoreName())) {
            throw new IllegalStateException("Cannot handle entity from " + storeName
                    + " datastore because active transaction is for " + holder.getStoreName());
        }

        if (TransactionSynchronizationManager.isSynchronizationActive() && !holder.isSynchronizedWithTransaction()) {
            holder.setSynchronizedWithTransaction(true);
            TransactionSynchronizationManager.registerSynchronization(
                    new ContainerResourceSynchronization(holder, RESOURCE_HOLDER_KEY));
        }
        return holder;
    }

    public void fireEntityListeners(EntityManager entityManager, boolean warnAboutImplicitFlush) {
        UnitOfWork unitOfWork = entityManager.getDelegate().unwrap(UnitOfWork.class);
        String storeName = getStorageName(unitOfWork);
        traverseEntities(getInstanceContainerResourceHolder(storeName), new OnFlushEntityVisitor(storeName), warnAboutImplicitFlush);
    }

    protected void fireBeforeDetachEntityListener(BaseGenericIdEntity entity, String storeName) {
        if (!BaseEntityInternalAccess.isDetached(entity)) {
            CubaEntityFetchGroup.setAccessLocalUnfetched(false);
            try {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_DETACH, storeName);
            } finally {
                CubaEntityFetchGroup.setAccessLocalUnfetched(true);
            }
        }
    }

    protected static boolean isDeleted(BaseGenericIdEntity entity, AttributeChangeListener changeListener) {
        if ((entity instanceof SoftDelete)) {
            ObjectChangeSet changeSet = changeListener.getObjectChangeSet();
            return changeSet != null
                    && changeSet.getAttributesToChanges().containsKey("deleteTs")
                    && ((SoftDelete) entity).isDeleted();

        } else {
            return BaseEntityInternalAccess.isRemoved(entity);
        }
    }

    protected void traverseEntities(ContainerResourceHolder container, EntityVisitor visitor, boolean warnAboutImplicitFlush) {
        beforeStore(container, visitor, container.getAllInstances(), createEntitySet(), warnAboutImplicitFlush);
    }

    protected void beforeStore(ContainerResourceHolder container, EntityVisitor visitor,
                               Collection<Entity> instances, Set<Entity> processed, boolean warnAboutImplicitFlush) {
        boolean possiblyChanged = false;
        Set<Entity> withoutPossibleChanges = createEntitySet();
        for (Entity instance : instances) {
            processed.add(instance);

            if (!(instance instanceof ChangeTracker && instance instanceof BaseGenericIdEntity))
                continue;

            BaseGenericIdEntity entity = (BaseGenericIdEntity) instance;
            boolean result = visitor.visit(entity);
            if (!result) {
                withoutPossibleChanges.add(instance);
            }
            possiblyChanged = result || possiblyChanged;
        }
        if (!possiblyChanged)
            return;

        if (warnAboutImplicitFlush) {
            statisticsAccumulator.incImplicitFlushCount();
            if (implicitFlushLog.isTraceEnabled()) {
                implicitFlushLog.trace("Implicit flush due to query execution, see stack trace for the cause:\n"
                        + StackTrace.asString());
            } else {
                implicitFlushLog.debug("Implicit flush due to query execution");
            }
        }

        Collection<Entity> afterProcessing = container.getAllInstances();
        if (afterProcessing.size() > processed.size()) {
            afterProcessing.removeAll(processed);
            beforeStore(container, visitor, afterProcessing, processed, false);
        }

        if (!withoutPossibleChanges.isEmpty()) {
            afterProcessing = withoutPossibleChanges.stream()
                    .filter(instance -> {
                        ChangeTracker changeTracker = (ChangeTracker) instance;
                        AttributeChangeListener changeListener =
                                (AttributeChangeListener) changeTracker._persistence_getPropertyChangeListener();
                        return changeListener != null
                                && changeListener.hasChanges();
                    })
                    .collect(Collectors.toList());
            if (!afterProcessing.isEmpty()) {
                beforeStore(container, visitor, afterProcessing, processed, false);
            }
        }
    }

    public void detach(EntityManager entityManager, Entity entity) {
        UnitOfWork unitOfWork = entityManager.getDelegate().unwrap(UnitOfWork.class);
        String storeName = getStorageName(unitOfWork);

        if (entity instanceof BaseGenericIdEntity) {
            fireBeforeDetachEntityListener((BaseGenericIdEntity) entity, storeName);

            ContainerResourceHolder container = getInstanceContainerResourceHolder(storeName);
            container.unregisterInstance(entity, unitOfWork);
            if (BaseEntityInternalAccess.isNew((BaseGenericIdEntity) entity)) {
                container.getNewDetachedInstances().add(entity);
            }
        }

        makeDetached(entity);
    }

    protected void makeDetached(Object instance) {
        if (instance instanceof BaseGenericIdEntity) {
            BaseEntityInternalAccess.setNew((BaseGenericIdEntity) instance, false);
            BaseEntityInternalAccess.setManaged((BaseGenericIdEntity) instance, false);
            BaseEntityInternalAccess.setDetached((BaseGenericIdEntity) instance, true);
        }
        if (instance instanceof FetchGroupTracker) {
            ((FetchGroupTracker) instance)._persistence_setSession(null);
        }
        if (instance instanceof ChangeTracker) {
            ((ChangeTracker) instance)._persistence_setPropertyChangeListener(null);
        }
    }

    public interface EntityVisitor {
        boolean visit(BaseGenericIdEntity entity);
    }

    public static class ContainerResourceHolder extends ResourceHolderSupport {

        protected Map<UnitOfWork, Set<Entity>> unitOfWorkMap = new HashMap<>();

        protected Set<Entity> savedInstances = createEntitySet();

        protected Set<Entity> newDetachedInstances = createEntitySet();

        protected String storeName;

        public ContainerResourceHolder(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreName() {
            return storeName;
        }

        protected void registerInstanceForUnitOfWork(Entity instance, UnitOfWork unitOfWork) {
            if (log.isTraceEnabled())
                log.trace("ContainerResourceHolder.registerInstanceForUnitOfWork: instance = " +
                        instance + ", UnitOfWork = " + unitOfWork);

            if (instance instanceof BaseGenericIdEntity) {
                BaseEntityInternalAccess.setManaged((BaseGenericIdEntity) instance, true);
            }

            Set<Entity> instances = unitOfWorkMap.get(unitOfWork);
            if (instances == null) {
                instances = createEntitySet();
                unitOfWorkMap.put(unitOfWork, instances);
            }
            instances.add(instance);
        }

        protected void unregisterInstance(Entity instance, UnitOfWork unitOfWork) {
            Set<Entity> instances = unitOfWorkMap.get(unitOfWork);
            if (instances != null) {
                instances.remove(instance);
            }
        }

        protected Collection<Entity> getInstances(UnitOfWork unitOfWork) {
            HashSet<Entity> set = new HashSet<>();
            Set<Entity> entities = unitOfWorkMap.get(unitOfWork);
            if (entities != null)
                set.addAll(entities);
            return set;
        }

        protected Collection<Entity> getAllInstances() {
            Set<Entity> set = createEntitySet();
            for (Set<Entity> instances : unitOfWorkMap.values()) {
                set.addAll(instances);
            }
            return set;
        }

        protected Collection<Entity> getSavedInstances() {
            return savedInstances;
        }

        public Set<Entity> getNewDetachedInstances() {
            return newDetachedInstances;
        }

        @Override
        public String toString() {
            return "ContainerResourceHolder@" + Integer.toHexString(hashCode()) + "{" +
                    "storeName='" + storeName + '\'' +
                    '}';
        }
    }

    protected class ContainerResourceSynchronization
            extends ResourceHolderSynchronization<ContainerResourceHolder, String> implements Ordered {

        protected final ContainerResourceHolder container;

        public ContainerResourceSynchronization(ContainerResourceHolder resourceHolder, String resourceKey) {
            super(resourceHolder, resourceKey);
            this.container = resourceHolder;
        }

        @Override
        protected void cleanupResource(ContainerResourceHolder resourceHolder, String resourceKey, boolean committed) {
            resourceHolder.unitOfWorkMap.clear();
            resourceHolder.savedInstances.clear();
        }

        @Override
        public void beforeCommit(boolean readOnly) {
            if (log.isTraceEnabled())
                log.trace("ContainerResourceSynchronization.beforeCommit: instances=" + container.getAllInstances() + ", readOnly=" + readOnly);

            if (!readOnly) {
                traverseEntities(container, new OnCommitEntityVisitor(container.getStoreName()), false);
                entityLog.flush();
            }

            Collection<Entity> instances = container.getAllInstances();
            Set<String> typeNames = new HashSet<>();
            for (Object instance : instances) {
                if (instance instanceof Entity) {
                    Entity entity = (Entity) instance;

                    if (readOnly) {
                        AttributeChangeListener changeListener =
                                (AttributeChangeListener) ((ChangeTracker) entity)._persistence_getPropertyChangeListener();
                        if (changeListener != null && changeListener.hasChanges())
                            throw new IllegalStateException("Changed instance " + entity + " in read-only transaction");
                    }

                    // if cache is enabled, the entity can have EntityFetchGroup instead of CubaEntityFetchGroup
                    if (instance instanceof FetchGroupTracker) {
                        FetchGroupTracker fetchGroupTracker = (FetchGroupTracker) entity;
                        FetchGroup fetchGroup = fetchGroupTracker._persistence_getFetchGroup();
                        if (fetchGroup != null && !(fetchGroup instanceof CubaEntityFetchGroup))
                            fetchGroupTracker._persistence_setFetchGroup(new CubaEntityFetchGroup(fetchGroup));
                    }

                    if (entity instanceof BaseGenericIdEntity) {
                        if (BaseEntityInternalAccess.isNew((BaseGenericIdEntity) entity)) {
                            typeNames.add(entity.getMetaClass().getName());
                        }
                        fireBeforeDetachEntityListener((BaseGenericIdEntity) entity, container.getStoreName());
                    }
                }
            }

            if (!readOnly) {
                Collection<Entity> allInstances = container.getAllInstances();
                for (BeforeCommitTransactionListener transactionListener : beforeCommitTxListeners) {
                    transactionListener.beforeCommit(persistence.getEntityManager(container.getStoreName()), allInstances);
                }
                queryCacheManager.invalidate(typeNames, true);
                List<EntityChangedEvent> collectedEvents = entityChangedEventManager.collect(container.getAllInstances());
                detachAll();
                publishEntityChangedEvents(collectedEvents);
            } else {
                detachAll();
            }
        }

        @Override
        public void afterCompletion(int status) {
            try {
                Collection<Entity> instances = container.getAllInstances();
                if (log.isTraceEnabled())
                    log.trace("ContainerResourceSynchronization.afterCompletion: instances = " + instances);
                for (Object instance : instances) {
                    if (instance instanceof BaseGenericIdEntity) {
                        if (status == TransactionSynchronization.STATUS_COMMITTED) {
                            if (BaseEntityInternalAccess.isNew((BaseGenericIdEntity) instance)) {
                                // new instances become not new and detached only if the transaction was committed
                                BaseEntityInternalAccess.setNew((BaseGenericIdEntity) instance, false);
                            }
                        } else { // commit failed or the transaction was rolled back
                            makeDetached(instance);
                            for (Entity entity : container.getNewDetachedInstances()) {
                                BaseEntityInternalAccess.setNew((BaseGenericIdEntity) entity, true);
                                BaseEntityInternalAccess.setDetached((BaseGenericIdEntity) entity, false);
                            }
                        }
                    }
                }
                for (AfterCompleteTransactionListener listener : afterCompleteTxListeners) {
                    listener.afterComplete(status == TransactionSynchronization.STATUS_COMMITTED, instances);
                }
            } finally {
                super.afterCompletion(status);
            }
        }

        private void detachAll() {
            Collection<Entity> instances = container.getAllInstances();
            for (Object instance : instances) {
                if (instance instanceof BaseGenericIdEntity &&
                        BaseEntityInternalAccess.isNew((BaseGenericIdEntity) instance)) {
                    container.getNewDetachedInstances().add((Entity) instance);
                }
            }

            javax.persistence.EntityManager jpaEm = persistence.getEntityManager(container.getStoreName()).getDelegate();
            jpaEm.flush();
            jpaEm.clear();

            for (Object instance : instances) {
                makeDetached(instance);
            }
        }

        private void publishEntityChangedEvents(List<EntityChangedEvent> collectedEvents) {
            if (collectedEvents.isEmpty())
                return;

            List<TransactionSynchronization> synchronizationsBefore = new ArrayList<>(
                    TransactionSynchronizationManager.getSynchronizations());

            entityChangedEventManager.publish(collectedEvents);

            List<TransactionSynchronization> synchronizations = new ArrayList<>(
                    TransactionSynchronizationManager.getSynchronizations());

            if (synchronizations.size() > synchronizationsBefore.size()) {
                synchronizations.removeAll(synchronizationsBefore);
                for (TransactionSynchronization synchronization : synchronizations) {
                    synchronization.beforeCommit(false);
                }
            }
        }

        @Override
        public int getOrder() {
            return 100;
        }
    }

    protected class OnCommitEntityVisitor implements EntityVisitor {

        private String storeName;

        public OnCommitEntityVisitor(String storeName) {
            this.storeName = storeName;
        }

        @Override
        public boolean visit(BaseGenericIdEntity entity) {
            if (BaseEntityInternalAccess.isNew(entity)
                    && !getSavedInstances(storeName).contains(entity)) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_INSERT, storeName);
                entityLog.registerCreate(entity, true);
                enqueueForFts(entity, FtsChangeType.INSERT);
                ormCacheSupport.evictMasterEntity(entity, null);
                return true;
            }

            AttributeChangeListener changeListener =
                    (AttributeChangeListener) ((ChangeTracker) entity)._persistence_getPropertyChangeListener();
            if (changeListener == null)
                return false;

            if (isDeleted(entity, changeListener)) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_DELETE, storeName);
                entityLog.registerDelete(entity, true);
                if ((entity instanceof SoftDelete))
                    processDeletePolicy(entity);
                enqueueForFts(entity, FtsChangeType.DELETE);
                ormCacheSupport.evictMasterEntity(entity, null);
                return true;

            } else if (changeListener.hasChanges()) {
                EntityAttributeChanges changes = new EntityAttributeChanges();
                // add changes before listener
                changes.addChanges(changeListener.getObjectChangeSet());

                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_UPDATE, storeName);
                // add changes after listener
                changes.addChanges(changeListener.getObjectChangeSet());

                if (BaseEntityInternalAccess.isNew(entity)) {
                    // it can happen if flush was performed, so the entity is still New but was saved
                    entityLog.registerCreate(entity, true);
                    enqueueForFts(entity, FtsChangeType.INSERT);
                } else {
                    entityLog.registerModify(entity, true, changes);
                    enqueueForFts(entity, FtsChangeType.UPDATE);
                }
                ormCacheSupport.evictMasterEntity(entity, changes);
                return true;
            }

            return false;
        }

        protected void enqueueForFts(Entity entity, FtsChangeType changeType) {
            if (!FtsConfigHelper.getEnabled())
                return;
            try {
                if (ftsSender == null) {
                    if (AppBeans.containsBean(FtsSender.NAME)) {
                        ftsSender = AppBeans.get(FtsSender.NAME);
                    } else {
                        log.error("Error enqueueing changes for FTS: " + FtsSender.NAME + " bean not found");
                    }
                }
                if (ftsSender != null)
                    ftsSender.enqueue(entity, changeType);
            } catch (Exception e) {
                log.error("Error enqueueing changes for FTS", e);
            }
        }

        protected void processDeletePolicy(Entity entity) {
            DeletePolicyProcessor processor = AppBeans.get(DeletePolicyProcessor.NAME); // prototype
            processor.setEntity(entity);
            processor.process();
        }
    }

    protected class OnFlushEntityVisitor implements EntityVisitor {

        private String storeName;

        public OnFlushEntityVisitor(String storeName) {
            this.storeName = storeName;
        }

        @Override
        public boolean visit(BaseGenericIdEntity entity) {
            if (BaseEntityInternalAccess.isNew(entity)
                    && !getSavedInstances(storeName).contains(entity)) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_INSERT, storeName);
                entityLog.registerCreate(entity, true);
                return true;
            }

            AttributeChangeListener changeListener =
                    (AttributeChangeListener) ((ChangeTracker) entity)._persistence_getPropertyChangeListener();
            if (changeListener == null)
                return false;

            if (isDeleted(entity, changeListener)) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_DELETE, storeName);
                entityLog.registerDelete(entity, true);
                return true;

            } else if (changeListener.hasChanges()) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_UPDATE, storeName);
                if (BaseEntityInternalAccess.isNew(entity)) {
                    // it can happen if flush has already happened, so the entity is still New but was saved
                    entityLog.registerCreate(entity, true);
                } else {
                    EntityAttributeChanges changes = new EntityAttributeChanges();
                    changes.addChanges(changeListener.getObjectChangeSet());
                    entityLog.registerModify(entity, true, changes);
                }
                return true;
            }

            return false;
        }
    }
}
