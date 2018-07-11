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

/**
 * Factory for creating {@link Transaction}s.
 */
public interface Transactions {

    String NAME = "cuba_Transactions";

    /**
     * Creates a new transaction in the main data store.
     *
     * @see #create(String, TransactionParams)
     */
    Transaction create(TransactionParams params);

    /**
     * Creates a new transaction.<br>
     * If there is an active transaction, it will be suspended.
     *
     * @param storeName data store name
     * @param params    new transaction parameters
     * @return new transaction
     */
    Transaction create(String storeName, TransactionParams params);

    /**
     * Creates a new transaction in the main data store.<br>
     *
     * @see #create(String)
     */
    Transaction create();

    /**
     * Creates a new transaction.<br>
     * If there is an active transaction, it will be suspended.
     *
     * @param storeName data store name
     * @return object to control the new transaction
     */
    Transaction create(String storeName);

    /**
     * Creates a new transaction in the main data store if there is no one at the moment.
     *
     * @see #get(String)
     */
    Transaction get();

    /**
     * Creates a new transaction if there is no one at the moment.
     * <p>If a transaction exists, joins the current transaction. In this case:
     * <ul>
     *     <li>Subsequent invocation of {@link Transaction#commit()} does not affect current transaction.</li>
     *     <li>If {@link Transaction#end()} is called without previous {@link Transaction#commit()}, current
     *     transaction is marked as rollback-only, so any attempt to commit the surrounding {@link Transaction} will
     *     throw an exception.</li>
     * </ul>
     *
     * @param storeName data store name
     * @return object to control the transaction
     */
    Transaction get(String storeName);
}
