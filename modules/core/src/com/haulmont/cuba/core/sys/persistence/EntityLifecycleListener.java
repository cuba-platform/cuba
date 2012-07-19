/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:14:37
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.FtsSender;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.FtsChangeType;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FtsConfig;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.UserSessionSource;
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

@ManagedBean("cuba_EntityLifecycleListener")
public class EntityLifecycleListener extends AbstractLifecycleListener
{
    private static Log log = LogFactory.getLog(EntityLifecycleListener.class);

    @Inject
    private Persistence persistence;

    @Inject
    private EntityListenerManager manager;

    @Inject
    private EntityLogAPI entityLog;

    @Inject
    private UserSessionSource userSessionSource;

    public void beforePersist(LifecycleEvent event) {
        if (!(event.getSource() instanceof BaseEntity))
            return;

        BaseEntity entity = (BaseEntity) event.getSource();
        __beforePersist(entity);
    }

    public void beforeStore(LifecycleEvent event) {
        if (!(event.getSource() instanceof BaseEntity))
            return;

        BaseEntity entity = (BaseEntity) event.getSource();

        if (((PersistenceCapable) entity).pcIsNew()) {
            entityLog.registerCreate(entity, true);
            manager.fireListener(entity, EntityListenerType.BEFORE_INSERT);
            enqueueForFts(entity, FtsChangeType.INSERT);
        } else {
            if (entity instanceof Updatable) {
                __beforeUpdate((Updatable) event.getSource());
                if ((entity instanceof SoftDelete) && justDeleted((SoftDelete) entity)) {
                    entityLog.registerDelete(entity, true);
                    processDeletePolicy(entity);
                    manager.fireListener(entity, EntityListenerType.BEFORE_DELETE);
                    enqueueForFts(entity, FtsChangeType.DELETE);
                } else {
                    entityLog.registerModify(entity, true);
                    manager.fireListener(entity, EntityListenerType.BEFORE_UPDATE);
                    enqueueForFts(entity, FtsChangeType.UPDATE);
                }
            } else {
                entityLog.registerModify(entity, true);
                manager.fireListener(entity, EntityListenerType.BEFORE_UPDATE);
                enqueueForFts(entity, FtsChangeType.UPDATE);
            }
        }
    }

    @Override
    public void afterStore(LifecycleEvent event) {
        if (!(event.getSource() instanceof BaseEntity))
            return;

        BaseEntity entity = (BaseEntity) event.getSource();

        if (((PersistenceCapable) entity).pcIsNew()) {
            manager.fireListener(entity, EntityListenerType.AFTER_INSERT);
        } else {
            if (entity instanceof Updatable) {
                __beforeUpdate((Updatable) event.getSource());
                if ((entity instanceof SoftDelete) && justDeleted((SoftDelete) entity)) {
                    manager.fireListener(entity, EntityListenerType.AFTER_DELETE);
                } else {
                    manager.fireListener(entity, EntityListenerType.AFTER_UPDATE);
                }
            } else {
                manager.fireListener(entity, EntityListenerType.AFTER_UPDATE);
            }
        }
    }

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

    private boolean justDeleted(SoftDelete dd) {
        if (!dd.isDeleted()) {
            return false;
        }
        else {
            return persistence.getDirtyFields((BaseEntity) dd).contains("deleteTs");
        }
    }

    private void __beforePersist(BaseEntity entity) {
        entity.setCreatedBy(userSessionSource.getUserSession().getUser().getLogin());
        Date ts = TimeProvider.currentTimestamp();
        entity.setCreateTs(ts);

        if (entity instanceof Updatable) {
            ((Updatable) entity).setUpdateTs(ts);
        }
    }

    private void __beforeUpdate(Updatable entity) {
        entity.setUpdatedBy(userSessionSource.getUserSession().getUser().getLogin());
        entity.setUpdateTs(TimeProvider.currentTimestamp());
    }

    private void processDeletePolicy(BaseEntity entity) {
        DeletePolicyHelper helper = new DeletePolicyHelper(entity);
        helper.process();
    }

    private void enqueueForFts(BaseEntity entity, FtsChangeType changeType) {
        if (!ConfigProvider.getConfig(FtsConfig.class).getEnabled())
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
