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
 * <p> Plain scenario:
 * <pre>
 *     Transaction tx = persistence.createTransaction();
 *     try {
 *         // transactional code here
 *         tx.commit();
 *     } finally {
 *         tx.end();
 *     }
 * </pre>
 *
 * <p> Action-like scenario:
 * <pre>
 *     persistence.createTransaction().execute(new Transaction.Runnable() {
 *         public void run(EntityManager em) {
 *             // transactional code here
 *         }
 *     });
 * </pre>
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Transaction extends AutoCloseable {

    /**
     * Interface for transactional code.
     * Implementors should be passed to {@link Transaction#execute(com.haulmont.cuba.core.Transaction.Callable)} method.
     * @param <T>   result type
     */
    interface Callable<T> {
        /**
         * Gets called within a transaction.
         * @param em    current EntityManager instance
         * @return      result object that in turn returns from {@link Transaction#execute(com.haulmont.cuba.core.Transaction.Callable)}.
         */
        T call(EntityManager em);
    }

    /**
     * Simplified version of {@link Callable} that is not intended to return a result from transactional code.
     */
    abstract class Runnable implements Callable<Object> {
        @Override
        public final Object call(EntityManager em) {
            run(em);
            return null;
        }

        /**
         * Gets called within a transaction.
         * @param em    current EntityManager instance
         */
        public abstract void run(EntityManager em);
    }

    /**
     * Executes the action specified by the given single method object within a transaction.
	 * <p>Allows for returning a result object created within the transaction.
     * A RuntimeException thrown by the callback is treated as a fatal exception that enforces a rollback.
     * @param callable  transactional code in the form of {@link Callable} or {@link Runnable} implementation
     * @param <T>       result type
     * @return          result object if returned by {@link Callable}
     */
    <T> T execute(Callable<T> callable);

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
}
