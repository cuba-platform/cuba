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

import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.core.sys.listener.EntityListenerType;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.entity.DeleteDeferred;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.security.app.EntityLog;
import com.haulmont.cuba.security.app.EntityLogMBean;
import com.haulmont.cuba.security.app.EntityLogAPI;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.event.AbstractLifecycleListener;
import org.apache.openjpa.event.LifecycleEvent;

import java.util.Date;

public class EntityLifecycleListener extends AbstractLifecycleListener
{
    private EntityLogAPI entityLog;

    private EntityLogAPI getEntityLog() {
        if (entityLog == null) {
            entityLog = Locator.lookupMBean(EntityLogMBean.class, EntityLogMBean.OBJECT_NAME).getAPI();
        }
        return entityLog;
    }

    public void beforePersist(LifecycleEvent event) {
        if ((event.getSource() instanceof BaseEntity)) {
            __beforePersist((BaseEntity) event.getSource());
        }
    }

    public void beforeStore(LifecycleEvent event) {
        PersistenceCapable pc = (PersistenceCapable) event.getSource();
        if (pc.pcIsNew()) {
            getEntityLog().registerCreate((BaseEntity) event.getSource(), true);
            EntityListenerManager.getInstance().fireListener(
                    ((BaseEntity) event.getSource()), EntityListenerType.BEFORE_INSERT);
        } else {
            if (pc instanceof Updatable) {
                __beforeUpdate((Updatable) event.getSource());
                if ((pc instanceof DeleteDeferred) && justDeleted((DeleteDeferred) pc)) {
                    getEntityLog().registerDelete((BaseEntity) event.getSource(), true);
                    processDeletePolicy((BaseEntity) event.getSource());
                    EntityListenerManager.getInstance().fireListener(
                            ((BaseEntity) event.getSource()), EntityListenerType.BEFORE_DELETE);
                } else {
                    getEntityLog().registerModify((BaseEntity) event.getSource(), true);
                    EntityListenerManager.getInstance().fireListener(
                            ((BaseEntity) event.getSource()), EntityListenerType.BEFORE_UPDATE);
                }
            } else {
                getEntityLog().registerModify((BaseEntity) event.getSource(), true);
                EntityListenerManager.getInstance().fireListener(
                        ((BaseEntity) event.getSource()), EntityListenerType.BEFORE_UPDATE);
            }
        }
    }

    public void beforeDelete(LifecycleEvent event) {
        getEntityLog().registerDelete((BaseEntity) event.getSource(), true);
        EntityListenerManager.getInstance().fireListener(
                ((BaseEntity) event.getSource()), EntityListenerType.BEFORE_DELETE);
    }

    private boolean justDeleted(DeleteDeferred dd) {
        if (!dd.isDeleted()) {
            return false;
        }
        else {
            return PersistenceProvider.getDirtyFields((BaseEntity) dd).contains("deleteTs");
        }
    }

    private void __beforePersist(BaseEntity entity) {
        entity.setCreatedBy(SecurityProvider.currentUserSession().getLogin());
        Date ts = TimeProvider.currentTimestamp();
        entity.setCreateTs(ts);

        if (entity instanceof Updatable) {
            ((Updatable) entity).setUpdateTs(TimeProvider.currentTimestamp());
        }
    }

    private void __beforeUpdate(Updatable entity) {
        entity.setUpdatedBy(SecurityProvider.currentUserSession().getLogin());
        entity.setUpdateTs(TimeProvider.currentTimestamp());
    }

    private void processDeletePolicy(BaseEntity entity) {
        DeletePolicyHelper helper = new DeletePolicyHelper(entity);
        helper.process();
    }
}
