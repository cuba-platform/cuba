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
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.core.sys.listener.EntityListenerType;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.entity.DeleteDeferred;
import com.haulmont.cuba.core.global.TimeProvider;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.event.AbstractLifecycleListener;
import org.apache.openjpa.event.LifecycleEvent;

import java.util.Date;

public class EntityLifecycleListener extends AbstractLifecycleListener
{
    public void beforePersist(LifecycleEvent event) {
        if ((event.getSource() instanceof BaseEntity)) {
            __beforePersist((BaseEntity) event.getSource());
        }
    }

    public void beforeStore(LifecycleEvent event) {
        PersistenceCapable pc = (PersistenceCapable) event.getSource();
        if (pc.pcIsNew()) {
            EntityListenerManager.getInstance().fireListener(
                    ((BaseEntity) event.getSource()), EntityListenerType.BEFORE_INSERT);
        }
        else if (!pc.pcIsNew() && (pc instanceof Updatable)) {
            __beforeUpdate((Updatable) event.getSource());
            if ((pc instanceof DeleteDeferred) && justDeleted((DeleteDeferred) pc)) {
                processDeletePolicy((BaseEntity) event.getSource());
                EntityListenerManager.getInstance().fireListener(
                        ((BaseEntity) event.getSource()), EntityListenerType.BEFORE_DELETE);
            }
            else {
                EntityListenerManager.getInstance().fireListener(
                        ((BaseEntity) event.getSource()), EntityListenerType.BEFORE_UPDATE);
            }
        }
    }

    private boolean justDeleted(DeleteDeferred dd) {
        if (!dd.isDeleted()) {
            return false;
        }
        else {
            return PersistenceProvider.getDirtyFields((BaseEntity) dd).contains("deleteTs");
        }
    }

    public void afterStore(LifecycleEvent event) {
        PersistenceCapable pc = (PersistenceCapable) event.getSource();
        if (!pc.pcIsNew() && (event.getSource() instanceof Updatable)) {
//            System.out.println("afterStore: " + pc);
        }
    }

    private void __beforePersist(BaseEntity entity) {
        entity.setCreatedBy(SecurityProvider.currentUserSession().getSubjectId());
        Date ts = TimeProvider.currentTimestamp();
        entity.setCreateTs(ts);

        if (entity instanceof Updatable) {
            ((Updatable) entity).setUpdateTs(TimeProvider.currentTimestamp());
        }
    }

    private void __beforeUpdate(Updatable entity) {
        entity.setUpdatedBy(SecurityProvider.currentUserSession().getSubjectId());
        entity.setUpdateTs(TimeProvider.currentTimestamp());
    }

    private void processDeletePolicy(BaseEntity entity) {
        DeletePolicyHelper helper = new DeletePolicyHelper(entity);
        helper.process();
    }
}
