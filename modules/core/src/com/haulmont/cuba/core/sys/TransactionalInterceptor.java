/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Persistence;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author Konstantin Krivopustov
 */
public class TransactionalInterceptor {

    private Persistence persistence;

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    private Object aroundInvoke(ProceedingJoinPoint ctx) throws Throwable {
        if (persistence.isInTransaction())
            TransactionSynchronizationManager.registerSynchronization(
                    ((PersistenceImpl) persistence).createSynchronization());

        return ctx.proceed();
    }
}
