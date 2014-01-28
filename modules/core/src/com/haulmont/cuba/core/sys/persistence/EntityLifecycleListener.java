/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.FtsSender;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.FtsChangeType;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.core.sys.listener.EntityListenerType;
import com.haulmont.cuba.security.app.EntityLogAPI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.event.AbstractLifecycleListener;
import org.apache.openjpa.event.LifecycleEvent;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_EntityLifecycleListener")
public class EntityLifecycleListener extends AbstractLifecycleListener {

    protected Log log = LogFactory.getLog(getClass());

    @Inject
    protected Persistence persistence;

    @Inject
    protected EntityListenerManager manager;

    @Inject
    protected EntityLogAPI entityLog;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected TimeSource timeSource;

    private FtsConfig ftsConfig;

    @Inject
    public void setConfiguration(Configuration configuration) {
        ftsConfig = configuration.getConfig(FtsConfig.class);
    }

    @Override
    public void beforePersist(LifecycleEvent event) {
        if (!(event.getSource() instanceof BaseEntity))
            return;

        BaseEntity entity = (BaseEntity) event.getSource();
        __beforePersist(entity);
    }

    @Override
    public void beforeStore(LifecycleEvent event) {
        if (!(event.getSource() instanceof BaseEntity))
            return;

        BaseEntity entity = (BaseEntity) event.getSource();

        if (((PersistenceCapable) entity).pcIsNew()) {
            entityLog.registerCreate(entity, true);
            manager.fireListener(entity, EntityListenerType.BEFORE_INSERT);
            enqueueForFts(entity, FtsChangeType.INSERT);

        } else if ((entity instanceof SoftDelete) && justDeleted((SoftDelete) entity)) {
            entityLog.registerDelete(entity, true);
            processDeletePolicy(entity);
            manager.fireListener(entity, EntityListenerType.BEFORE_DELETE);
            enqueueForFts(entity, FtsChangeType.DELETE);

        } else {
            if (entity instanceof Updatable) {
                __beforeUpdate((Updatable) event.getSource());
            }
            entityLog.registerModify(entity, true);
            manager.fireListener(entity, EntityListenerType.BEFORE_UPDATE);
            enqueueForFts(entity, FtsChangeType.UPDATE);
        }
    }

    @Override
    public void afterStore(LifecycleEvent event) {
        if (!(event.getSource() instanceof BaseEntity))
            return;

        BaseEntity entity = (BaseEntity) event.getSource();

        if (((PersistenceCapable) entity).pcIsNew()) {
            manager.fireListener(entity, EntityListenerType.AFTER_INSERT);

        } else if ((entity instanceof SoftDelete) && justDeleted((SoftDelete) entity)) {
            manager.fireListener(entity, EntityListenerType.AFTER_DELETE);

        } else {
            if (entity instanceof Updatable) {
                __beforeUpdate((Updatable) event.getSource());
            }
            manager.fireListener(entity, EntityListenerType.AFTER_UPDATE);
        }
    }

    @Override
    public void beforeDelete(LifecycleEvent event) {
        if (!(event.getSource() instanceof BaseEntity))
            return;

        BaseEntity entity = (BaseEntity) event.getSource();
        entityLog.registerDelete(entity, true);
        manager.fireListener(entity, EntityListenerType.BEFORE_DELETE);
        enqueueForFts(entity, FtsChangeType.DELETE);
    }

    @Override
    public void afterDelete(LifecycleEvent event) {
        if (!(event.getSource() instanceof BaseEntity))
            return;

        BaseEntity entity = (BaseEntity) event.getSource();
        manager.fireListener(entity, EntityListenerType.AFTER_DELETE);
    }

    protected boolean justDeleted(SoftDelete entity) {
        return entity.isDeleted() && persistence.getTools().getDirtyFields((BaseEntity) entity).contains("deleteTs");
    }

    protected void __beforePersist(BaseEntity entity) {
        entity.setCreatedBy(userSessionSource.getUserSession().getUser().getLogin());
        Date ts = timeSource.currentTimestamp();
        entity.setCreateTs(ts);

        if (entity instanceof Updatable) {
            ((Updatable) entity).setUpdateTs(ts);
        }
    }

    protected void __beforeUpdate(Updatable entity) {
        entity.setUpdatedBy(userSessionSource.getUserSession().getUser().getLogin());
        entity.setUpdateTs(timeSource.currentTimestamp());
    }

    protected void processDeletePolicy(BaseEntity entity) {
        DeletePolicyProcessor processor = AppBeans.get(DeletePolicyProcessor.NAME);
        processor.setEntity(entity);
        processor.process();
    }

    protected void enqueueForFts(BaseEntity entity, FtsChangeType changeType) {
        if (!ftsConfig.getEnabled())
            return;
        try {
            if (AppContext.getApplicationContext().containsBean(FtsSender.NAME)) {
                FtsSender sender = (FtsSender) AppContext.getApplicationContext().getBean(FtsSender.NAME);
                sender.enqueue(entity, changeType);
            } else {
                log.error("Error enqueueing changes for FTS: " + FtsSender.NAME + " bean not deployed");
            }
        } catch (Exception e) {
            log.error("Error enqueueing changes for FTS", e);
        }
    }
}
