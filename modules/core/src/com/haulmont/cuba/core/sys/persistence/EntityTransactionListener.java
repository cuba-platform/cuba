/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.MiddlewareStatisticsAccumulator;
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
@ManagedBean("cuba_EntityTransactionListener")
public class EntityTransactionListener extends AbstractTransactionListener {

    @Inject
    private Persistence persistence;

    @Inject
    private EntityListenerManager manager;

    @Inject
    private MiddlewareStatisticsAccumulator statisticsCounter;

    @Override
    public void beforeCommit(TransactionEvent event) {
        for (Object obj : ((OpenJPAEntityManagerSPI) persistence.getEntityManager().getDelegate()).getManagedObjects()) {
            if (!(obj instanceof BaseEntity))
                continue;

            BaseEntity entity = (BaseEntity) obj;
            manager.fireListener(entity, EntityListenerType.BEFORE_DETACH);
            entity.setDetached(true);
        }
    }

    @Override
    public void afterBegin(TransactionEvent event) {
        statisticsCounter.incStartedTransactionsCount();
    }

    @Override
    public void afterCommit(TransactionEvent event) {
        statisticsCounter.incCommittedTransactionsCount();
    }

    @Override
    public void afterRollback(TransactionEvent event) {
        statisticsCounter.incRolledBackTransactionsCount();
    }
}
