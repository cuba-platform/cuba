/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:14:37
 *
 * $Id$
 */
package com.haulmont.cuba.core.persistence;

import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.core.TimeProvider;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Updatable;
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
        if (!pc.pcIsNew() && (event.getSource() instanceof Updatable)) {
            __beforeUpdate((Updatable) event.getSource());
        }
    }

    private void __beforePersist(BaseEntity entity) {
        entity.setCreatedBy(SecurityProvider.currentUserLogin());
        Date ts = TimeProvider.currentTimestamp();
        entity.setCreateTs(ts);

        if (entity instanceof Updatable) {
            ((Updatable) entity).setUpdateTs(TimeProvider.currentTimestamp());
        }
    }

    private void __beforeUpdate(Updatable entity) {
        String user = SecurityProvider.currentUserLogin();
        entity.setUpdatedBy(user);
        entity.setUpdateTs(TimeProvider.currentTimestamp());
    }
}
