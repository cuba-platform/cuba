/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.app.FtsSender;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FtsConfig;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.core.sys.listener.EntityListenerType;
import com.haulmont.cuba.security.app.EntityLogAPI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.persistence.descriptors.changetracking.ChangeTracker;
import org.eclipse.persistence.internal.descriptors.changetracking.AttributeChangeListener;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.internal.sessions.ObjectChangeSet;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.ResourceHolderSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(PersistenceImplSupport.NAME)
public class PersistenceImplSupport {

    public static final String NAME = "cuba_PersistenceImplSupport";

    public static final String RESOURCE_HOLDER_KEY = ContainerResourceHolder.class.getName();

    @Inject
    protected EntityListenerManager entityListenerManager;

    @Inject
    protected EntityLogAPI entityLog;

    @Inject
    protected FtsConfig ftsConfig;

    protected volatile FtsSender ftsSender;

    private static Log log = LogFactory.getLog(PersistenceImplSupport.class.getName());

    public void registerInstance(Entity entity, EntityManager entityManager) {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new RuntimeException("No transaction");

        UnitOfWork unitOfWork = entityManager.getDelegate().unwrap(UnitOfWork.class);
        getInstanceContainerResourceHolder().registerInstanceForUnitOfWork(entity, unitOfWork);

        if (entity instanceof BaseGenericIdEntity) {
            ((BaseGenericIdEntity) entity).__detached(false);
        }
    }

    public void registerInstance(Object object, AbstractSession session) {
        // Can be called outside of a transaction when fetching lazy attributes
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            return;

        if (!(session instanceof UnitOfWork))
            throw new RuntimeException("Session is not a UnitOfWork: " + session);

        getInstanceContainerResourceHolder().registerInstanceForUnitOfWork(object, (UnitOfWork) session);
    }

    public Collection<Object> getInstances(EntityManager entityManager) {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new RuntimeException("No transaction");

        UnitOfWork unitOfWork = entityManager.getDelegate().unwrap(UnitOfWork.class);
        return getInstanceContainerResourceHolder().getInstances(unitOfWork);
    }

    protected ContainerResourceHolder getInstanceContainerResourceHolder() {
        ContainerResourceHolder holder =
                (ContainerResourceHolder) TransactionSynchronizationManager.getResource(RESOURCE_HOLDER_KEY);
        if (holder != null)
            return holder;

        holder = new ContainerResourceHolder();
        TransactionSynchronizationManager.bindResource(RESOURCE_HOLDER_KEY, holder);
        holder.setSynchronizedWithTransaction(true);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new ContainerResourceSynchronization(holder, RESOURCE_HOLDER_KEY));
        }
        return holder;
    }

    public void fireEntityListeners() {
        traverseEntities(getInstanceContainerResourceHolder(), new OnFlushEntityVisitor());
    }

    protected boolean isDeleted(BaseGenericIdEntity entity, AttributeChangeListener changeListener) {
        if ((entity instanceof SoftDelete)) {
            ObjectChangeSet changeSet = changeListener.getObjectChangeSet();
            return ((SoftDelete) entity).isDeleted()
                    && changeSet != null
                    && changeSet.getAttributesToChanges().containsKey("deleteTs");

        } else
            return entity.__removed();
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

        protected void registerInstanceForUnitOfWork(Object instance, UnitOfWork unitOfWork) {
            if (log.isTraceEnabled())
                log.trace("ContainerResourceHolder.registerInstanceForUnitOfWork: instance = " +
                        instance + ", UnitOfWork = " + unitOfWork);

            if (instance instanceof BaseGenericIdEntity) {
                ((BaseGenericIdEntity) instance).__managed(true);
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
            extends ResourceHolderSynchronization<ContainerResourceHolder, String> {

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

            traverseEntities(container, new OnCommitEntityVisitor());

            Collection<Object> instances = container.getAllInstances();
            for (Object instance : instances) {
                if (instance instanceof BaseEntity) {
                    entityListenerManager.fireListener((BaseEntity) instance, EntityListenerType.BEFORE_DETACH);
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
                    ((BaseGenericIdEntity) instance).__new(false);
                    ((BaseGenericIdEntity) instance).__managed(false);
                    ((BaseGenericIdEntity) instance).__detached(true);
                }
            }
            super.afterCompletion(status);
        }
    }

    protected class OnCommitEntityVisitor implements EntityVisitor {
        @Override
        public boolean visit(BaseGenericIdEntity entity) {
            if (entity.__new()) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_INSERT);
                entityLog.registerCreate(entity, true);
                enqueueForFts(entity, FtsChangeType.INSERT);
                return true;
            }

            AttributeChangeListener changeListener =
                    (AttributeChangeListener) ((ChangeTracker) entity)._persistence_getPropertyChangeListener();
            if (changeListener == null)
                return false;

            if (isDeleted(entity, changeListener)) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_DELETE);
                entityLog.registerDelete(entity, true);
                if ((entity instanceof SoftDelete))
                    processDeletePolicy(entity);
                enqueueForFts(entity, FtsChangeType.DELETE);
                return true;

            } else if (changeListener.hasChanges()) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_UPDATE);
                entityLog.registerModify(entity, true);
                enqueueForFts(entity, FtsChangeType.UPDATE);
                return true;
            }

            return false;
        }

        protected void enqueueForFts(BaseEntity entity, FtsChangeType changeType) {
            if (!ftsConfig.getEnabled())
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

        protected void processDeletePolicy(BaseEntity entity) {
            DeletePolicyProcessor processor = AppBeans.get(DeletePolicyProcessor.NAME); // prototype
            processor.setEntity(entity);
            processor.process();
        }
    }

    protected class OnFlushEntityVisitor implements EntityVisitor {
        @Override
        public boolean visit(BaseGenericIdEntity entity) {
            if (entity.__new()) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_INSERT);
                return true;
            }

            AttributeChangeListener changeListener =
                    (AttributeChangeListener) ((ChangeTracker) entity)._persistence_getPropertyChangeListener();
            if (changeListener == null)
                return false;

            if (isDeleted(entity, changeListener)) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_DELETE);
                return true;

            } else if (changeListener.hasChanges()) {
                entityListenerManager.fireListener(entity, EntityListenerType.BEFORE_UPDATE);
                return true;
            }

            return false;
        }
    }
}
