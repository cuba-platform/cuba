/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.core.sys.listener.EntityListenerType;
import org.apache.openjpa.event.AbstractTransactionListener;
import org.apache.openjpa.event.TransactionEvent;
import org.apache.openjpa.persistence.OpenJPAEntityManagerSPI;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_TransactionListener")
public class EntityTransactionListener extends AbstractTransactionListener {

    @Inject
    private Persistence persistence;

    @Inject
    private EntityListenerManager manager;

    @Override
    public void beforeCommit(TransactionEvent event) {
        for (Object obj : ((OpenJPAEntityManagerSPI) persistence.getEntityManager().getDelegate()).getManagedObjects()) {
            if (!(obj instanceof BaseEntity))
                continue;

            BaseEntity entity = (BaseEntity) obj;
            manager.fireListener(entity, EntityListenerType.BEFORE_DETACH);
        }
    }
}
