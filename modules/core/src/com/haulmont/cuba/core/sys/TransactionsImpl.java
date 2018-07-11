/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TransactionParams;
import com.haulmont.cuba.core.Transactions;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Stores;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.inject.Named;

@Component(Transactions.NAME)
public class TransactionsImpl implements Transactions {

    @Inject
    @Named("transactionManager")
    protected PlatformTransactionManager transactionManager;

    @Inject
    protected PersistenceImpl persistence;

    @Inject
    protected BeanLocator beanLocator;

    @Override
    public Transaction create(TransactionParams params) {
        return new TransactionImpl(transactionManager, persistence, false, params, Stores.MAIN);
    }

    @Override
    public Transaction create(String storeName, TransactionParams params) {
        return new TransactionImpl(getTransactionManager(storeName), persistence, false, params, storeName);
    }

    @Override
    public Transaction create() {
        return new TransactionImpl(transactionManager, persistence, false, null, Stores.MAIN);
    }

    @Override
    public Transaction create(String storeName) {
        return new TransactionImpl(getTransactionManager(storeName), persistence, false, null, storeName);
    }

    @Override
    public Transaction get() {
        return new TransactionImpl(transactionManager, persistence, true, null, Stores.MAIN);
    }

    @Override
    public Transaction get(String storeName) {
        return new TransactionImpl(getTransactionManager(storeName), persistence, true, null, storeName);
    }

    protected PlatformTransactionManager getTransactionManager(String store) {
        PlatformTransactionManager tm;
        if (Stores.isMain(store))
            tm = this.transactionManager;
        else
            tm = beanLocator.get("transactionManager_" + store, PlatformTransactionManager.class);
        return tm;
    }
}
