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
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TransactionParams;
import com.haulmont.cuba.core.global.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Nullable;

public class TransactionImpl implements Transaction {

    private final PlatformTransactionManager tm;
    private final PersistenceImpl persistence;
    private final String storeName;
    private final DefaultTransactionDefinition td;
    private TransactionStatus ts;
    private boolean committed;

    private static final Logger log = LoggerFactory.getLogger(TransactionImpl.class);

    public TransactionImpl(PlatformTransactionManager transactionManager, PersistenceImpl persistence, boolean join,
                           @Nullable TransactionParams params, String storeName) {
        this.tm = transactionManager;
        this.persistence = persistence;
        this.storeName = storeName;

        log.trace("Creating transaction: store='{}' join={} params={}", storeName, join, params);

        td = new DefaultTransactionDefinition();
        if (join)
            td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        else
            td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        if (params != null) {
            if (params.getTimeout() != 0)
                td.setTimeout(params.getTimeout());
            if (params.isReadOnly())
                td.setReadOnly(true);
        }

        ts = tm.getTransaction(td);

        persistence.registerSynchronizations(storeName);
    }

    @Override
    public <T> T execute(Callable<T> callable) {
        return execute(Stores.MAIN, callable);
    }

    @Override
    public <T> T execute(String storeName, Callable<T> callable) {
        try {
            T result = callable.call(persistence.getEntityManager(storeName));
            commit();
            return result;
        } finally {
            end();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        execute(Stores.MAIN, runnable);
    }

    @Override
    public void execute(String storeName, Runnable runnable) {
        try {
            runnable.run(persistence.getEntityManager(storeName));
            commit();
        } finally {
            end();
        }
    }

    @Override
    public void commit() {
        if (committed)
            return;

        tm.commit(ts);
        committed = true;
    }

    @Override
    public void commitRetaining() {
        if (committed)
            return;

        tm.commit(ts);

        ts = tm.getTransaction(td);
        persistence.registerSynchronizations(storeName);
    }

    @Override
    public void end() {
        if (committed)
            return;

        if (!ts.isCompleted())
            tm.rollback(ts);
    }

    @Override
    public void close() {
        end();
    }
}