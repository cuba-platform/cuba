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
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.app.FtsSender;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.global.FtsConfigHelper;
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
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.ResourceHolderSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.inject.Inject;
import java.util.*;

@Component(PersistenceImplSupport.NAME)
public class PersistenceImplSupport {

    public static final String NAME = "cuba_PersistenceImplSupport";

    public static final String RESOURCE_HOLDER_KEY = ContainerResourceHolder.class.getName();

    @Inject
    protected EntityListenerManager entityListenerManager;

    @Inject
    protected EntityLogAPI entityLog;

    protected volatile FtsSender ftsSender;

    @Inject
    protected OrmCacheSupport ormCacheSupport;

    protected ThreadLocal<Boolean> firingEntityListeners = new ThreadLocal<>();

    private static Logger log = LoggerFactory.getLogger(PersistenceImplSupport.class.getName());

    public void registerInstance(Entity entity, EntityManager entityManager) {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new RuntimeException("No transaction");

        UnitOfWork unitOfWork = entityManager.getDelegate().unwrap(UnitOfWork.class);
        getInstanceContainerResourceHolder(getStorageName(unitOfWork)).registerInstanceForUnitOfWork(entity, unitOfWork);

        if (entity instanceof BaseGenericIdEntity) {
            BaseEntityInternalAccess.setDetached((BaseGenericIdEntity) entity, false);
        }
    }

    public void registerInstance(Object object, AbstractSession session) {
        // Can be called outside of a transaction when fetching lazy attributes
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            return;

        if (!(session instanceof UnitOfWork))
            throw new RuntimeException("Session is not a UnitOfWork: " + session);

        getInstanceContainerResourceHolder(getStorageName(session)).registerInstanceForUnitOfWork(object, (UnitOfWork) session);
    }

    public Collection<Object> getInstances(EntityManager entityManager) {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new RuntimeException("No transaction");

        UnitOfWork unitOfWork = entityManager.getDelegate().unwrap(UnitOfWork.class);
        return getInstanceContainerResourceHolder(getStorageName(unitOfWork)).getInstances(unitOfWork);
    }

    public String getStorageName(Session session) {
        String storeName = (String) session.getProperty(Stores.PROP_NAME);
        return Strings.isNullOrEmpty(storeName) ? Stores.MAIN : storeName;
    }

    protected ContainerResourceHolder getInstanceContainerResourceHolder(String storeName) {
        ContainerResourceHolder holder =
                (ContainerResourceHolder) TransactionSynchronizationManager.getResource(RESOURCE_HOLDER_KEY);
        if (holder != null)
            return holder;

        holder = new ContainerResourceHolder(storeName);
        TransactionSynchronizationManager.bindResource(RESOURCE_HOLDER_KEY, holder);
        holder.setSynchronizedWithTransaction(true);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new ContainerResourceSynchronization(holder, RESOURCE_HOLDER_KEY));
        }
        return holder;
    }

    public void fireEntityListeners(EntityManager entityManager) {
        if (Boolean.TRUE.equals(firingEntityListeners.get()))
            return;
        firingEntityListeners.set(true);
        try {
            UnitOfWork unitOfWork = entityManager.getDelegate().unwrap(UnitOfWork.class);
            String storeName = getStorageName(unitOfWork);
            traverseEntities(getInstanceContainerResourceHolder(storeName), new OnFlushEntityVisitor(storeName));
        } finally {
            firingEntityListeners.remove();
        }
    }

    protected boolean isDeleted(BaseGenericIdEntity entity, AttributeChangeListener changeListener) {
        if ((entity instanceof SoftDelete)) {
            ObjectChangeSet changeSet = changeListener.getObjectChangeSet();
            return changeSet != null
                    && changeSet.getAttributesToChanges().containsKey("deleteTs")
                    && ((SoftDelete) entity).isDeleted();

        } else {
            return BaseEntityInternalAccess.isRemoved(entity);
        }
    }

    protected void traverseEntities(ContainerResourceHolder container, EntityVisitor visitor) {
        beforeStore(container, visitor, container.getAllInstances(), new HashSet<>());
    }

    protected void beforeStore(ContainerResourceHolder container, EntityVisitor visitor,
                             Collection<Object> instances, Set<Object> processed) {
        boolean possiblyChanged = false;
        for (Object instance : instances) {
            processed.add(instance);

            if (!(instance instanceof ChangeTracker && instance instanceof BaseGenericIdEntity))
                continue;

            BaseGenericIdEntity entity = (BaseGenericIdEntity) instance;
            possiblyChanged = visitor.visit(entity);
        }
        if (!possiblyChanged)
            return;

        Collection<Object> afterProcessing = container.getAllInstances();
        if (afterProcessing.size() > processed.size()) {
            afterProcessing.removeAll(processed);
            beforeStore(container, visitor, afterProcessing, processed);
        }
    }

    public interface EntityVisitor {
        boolean visit(BaseGenericIdEntity entity);
    }

    public static class ContainerResourceHolder extends ResourceHolderSupport {

        protected Map<UnitOfWork, Set<Object>> unitOfWorkMap = new HashMap<>();

        protected String storeName;

        public ContainerResourceHolder(String storeName) {
            this.storeName = storeName;
        }

        public String getStorageName() {
            return storeName;
        }

        protected void registerInstanceForUnitOfWork(Object instance, UnitOfWork unitOfWork) {
            if (log.isTraceEnabled())
                log.trace("ContainerResourceHolder.registerInstanceForUnitOfWork: instance = " +
                        instance + ", UnitOfWork = " + unitOfWork);

            if (instance instanceof BaseGenericIdEntity) {
                BaseEntityInternalAccess.setManaged((BaseGenericIdEntity) instance, true);
            }

            Set<Object> instances = unitOfWorkMap.get(unitOfWork);
            if (instances == null) {
                instances = new HashSet<>();
                unitOfWorkMap.put(unitOfWork, instances);
            }
            instances.add(instance);
        }

        protected Collection<Object> getInstances(UnitOfWork unitOfWork) {
            return new HashSet<>(unitOfWorkMap.get(unitOfWork));
        }

        protected Collection<Object> getAllInstances() {
            Set<Object> set = new HashSet<>();
            for (Set<Object> instances : unitOfWorkMap.values()) {
                set.addAll(instances);
            }
            return set;
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
        }

        @Override
        public void beforeCommit(boolean readOnly) {
            if (log.isTraceEnabled())
                log.trace("ContainerResourceSynchronization.beforeCommit: instances = " + container.getAllInstances());

            traverseEntities(container, new OnCommitEntityVisitor(container.getStorageName()));

            Collection<Object> instances = container.getAllInstances();
            for (Object instance : instances) {
                if (instance instanceof Entity) {
                    // if cache is enabled, the entity can have EntityFetchGroup instead of CubaEntityFetchGroup
                    if (instance instanceof FetchGroupTracker) {
                        FetchGroupTracker entity = (FetchGroupTracker) instance;
                        FetchGroup fetchGroup = entity._persistence_getFetchGroup();
                        if (fetchGroup != null && !(fetchGroup instanceof CubaEntityFetchGroup))
                            entity._persistence_setFetchGroup(new CubaEntityFetchGroup(fetchGroup));
                    }

                    entityListenerManager.fireListener((Entity) instance, EntityListenerType.BEFORE_DETACH, container.getStorageName());
                }
            }
        }

        @Override
        public void afterCompletion(int status) {
            Collection<Object> instances = container.getAllInstances();
            if (log.isTraceEnabled())
                log.trace("ContainerResourceSynchronization.afterCompletion: instances = " + instances);
            for (Object instance : instances) {
                if (instance instanceof BaseGenericIdEntity) {
                    BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) instance;

                    BaseEntityInternalAccess.setNew(baseGenericIdEntity, false);
                    BaseEntityInternalAccess.setManaged(baseGenericIdEntity, false);
                    BaseEntityInternalAccess.setDetached(baseGenericIdEntity, true);
                }
            }
            super.afterCompletion(status);
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
            if (BaseEntityInternalAccess.isNew(entity)) {
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

                entityLog.registerModify(entity, true, changes);
                enqueueForFts(entity, FtsChangeType.UPDATE);
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
            if (BaseEntityInternalAccess.isNew(entity)) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_INSERT, storeName);
                return true;
            }

            AttributeChangeListener changeListener =
                    (AttributeChangeListener) ((ChangeTracker) entity)._persistence_getPropertyChangeListener();
            if (changeListener == null)
                return false;

            if (isDeleted(entity, changeListener)) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_DELETE, storeName);
                return true;

            } else if (changeListener.hasChanges()) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_UPDATE, storeName);
                return true;
            }

            return false;
        }
    }
}
