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

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.*;

import javax.annotation.Nullable;

/**
 * Similar to {@link DataManager} but joins an existing transaction.
 * <p>
 * Use this bean in the middleware logic to perform some operations in one transaction, for example:
 * <pre>
 *     {@literal @}Inject
 *      private TransactionalDataManager txDataManager;
 *
 *     {@literal @}Transactional
 *      private void transfer(Id&lt;Account, UUID&gt; acc1Id, Id&lt;Account, UUID&gt; acc2Id, Long amount) {
 *          Account acc1 = txDataManager.load(acc1Id).one();
 *          Account acc2 = txDataManager.load(acc2Id).one();
 *          acc1.setBalance(acc1.getBalance() - amount);
 *          acc2.setBalance(acc2.getBalance() + amount);
 *          txDataManager.save(acc1);
 *          txDataManager.save(acc2);
 *      }
 * </pre>
 */
public interface TransactionalDataManager {

    String NAME = "cuba_TransactionalDataManager";

    <E extends Entity<K>, K> FluentLoader<E, K> load(Class<E> entityClass);

    <E extends Entity<K>, K> FluentLoader.ById<E, K> load(Id<E, K> entityId);

    FluentValuesLoader loadValues(String queryString);

    <T> FluentValueLoader<T> loadValue(String queryString, Class<T> valueClass);

    EntitySet save(Entity... entities);

    <E extends Entity> E save(E entity);

    <E extends Entity> E save(E entity, @Nullable View view);

    <E extends Entity> E save(E entity, @Nullable String viewName);

    void remove(Entity entity);
}
