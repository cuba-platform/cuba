/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
     * Executes the action specified by the given single method object within a transaction.
	 * <p>Returns a result object created within the transaction.
     * <p>A {@code RuntimeException} thrown in the transactional code enforces a rollback.
     * @param callable  transactional code in the form of {@link Callable}
     * @param <T>       result type
     * @return          result object
     */
    <T> T execute(Callable<T> callable);

    /**
     * Executes the action specified by the given single method object within a transaction.
     * <p>A {@code RuntimeException} thrown in the transactional code enforces a rollback.
     * @param runnable  transactional code in the form of {@link Runnable}
     */
    void execute(Runnable runnable);

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
