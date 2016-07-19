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
package com.haulmont.cuba.core;

/**
 * Programmatic transaction control interface. Supports the following usage scenarios.
 *
 * <p> Try-with-resources scenario:
 * <pre>
 *     try (Transaction tx = persistence.createTransaction()) {
 *         // transactional code here
 *         tx.commit();
 *     }
 * </pre>
 *
 * <p> Lambda scenario:
 * <pre>
 *     persistence.createTransaction().execute((EntityManager em) -> {
 *         // transactional code here
 *     });
 * </pre>
 *
 * @see Persistence#runInTransaction(Runnable)
 * @see Persistence#callInTransaction(Callable)
 */
public interface Transaction extends AutoCloseable {

    /**
     * Interface for transactional code.
     * @param <T>   result type
     */
    interface Callable<T> {
        /**
         * Gets called within a transaction.
         * @param em current EntityManager instance
         * @return result object that in turn returns from {@link Transaction#execute(com.haulmont.cuba.core.Transaction.Callable)}.
         */
        T call(EntityManager em);
    }

    /**
     * Interface for transactional code that is not intended to return a result.
     */
    interface Runnable {
        /**
         * Gets called within a transaction.
         * @param em current EntityManager instance
         */
        void run(EntityManager em);
    }

    /**
     * Executes the action specified by the given single method object within a transaction in the main data store.
     * @see #execute(String, Callable)
     */
    <T> T execute(Callable<T> callable);

    /**
     * Executes the action specified by the given single method object within a transaction.
     * <p>Returns a result object created within the transaction.
     * <p>A {@code RuntimeException} thrown in the transactional code enforces a rollback.
     * @param storeName data store name
     * @param callable  transactional code in the form of {@link Callable}
     * @param <T>       result type
     * @return          result object
     */
    <T> T execute(String storeName, Callable<T> callable);

    /**
     * Executes the action specified by the given single method object within a transaction in the main data store.
     * @see #execute(String, Runnable)
     */
    void execute(Runnable runnable);

    /**
     * Executes the action specified by the given single method object within a transaction.
     * <p>A {@code RuntimeException} thrown in the transactional code enforces a rollback.
     * @param storeName data store name
     * @param runnable  transactional code in the form of {@link Runnable}
     */
    void execute(String storeName, Runnable runnable);

    /**
     * Commit current transaction.
     */
    void commit();

    /**
     * Commit current transaction and immediately start a new one.
     */
    void commitRetaining();

    /**
     * This method has to be invoked in the following construct:
     * <pre>
     *     Transaction tx = ...
     *     try {
     *         ...
     *         tx.commit();
     *     } finally {
     *         tx.end();
     *     }
     * </pre>
     * In case of successful commit this method does nothing. Otherwise it rollbacks the current transaction.
     */
    void end();

    @Override
    void close();
}
