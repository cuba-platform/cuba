/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.core.global;

import javax.annotation.CheckReturnValue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Bean that contain {@link com.haulmont.cuba.core.global.CommitContext} and set of actions to be performed before/after commit.
 */
public interface TransactionalAction {

    String NAME = "cuba_TransactionalAction";

    /**
     * Specifies the action to be performed right after the successful commit.
     * <p>Entities in the EntitySet can be modified but these modifications don't change entities in a database.</p>
     * <p>Method returns the same instance of {@link com.haulmont.cuba.core.global.TransactionalAction} so
     * it can be used for method chaining.</p>
     *
     * @param consumer  implementation of {@link java.util.function.Consumer} interface
     * @return          the same instance to which the method was applied
     */
    @CheckReturnValue
    TransactionalAction onSuccess(Consumer<EntitySet> consumer);

    /**
     * Specifies the action to be performed right after the unsuccessful commit.
     * <p>If this action is specified (not null) and an exception occurs during {@code commit}
     * then this exception won't be re-thrown during {@code perform()} execution.
     * In this case {@code null} value will be returned by {@code perform()} method</p>
     * <p>Method returns the same instance of {@link com.haulmont.cuba.core.global.TransactionalAction} so
     * it can be used for method chaining.</p>
     *
     * @param consumer  implementation of {@link java.util.function.BiConsumer} interface.
     *                  The first parameter - {@link CommitContext} that was used for unsuccessful commit.
     *                  The second parameter - {@link Throwable} that was thrown during the commit
     * @return          the same instance to which the method was applied
     */
    @CheckReturnValue
    TransactionalAction onFail(BiConsumer<CommitContext, Throwable> consumer);

    /**
     * Specifies the action to be performed after the commit (regardless of whether it is successful or not).
     * <p>All changes of the {@link CommitContext} won't be saved in a database.</p>
     * <p>Method returns the same instance of {@link com.haulmont.cuba.core.global.TransactionalAction} so
     * it can be used for method chaining.</p>
     *
     * @param consumer  implementation of {@link java.util.function.Consumer} interface
     * @return          the same instance to which the method was applied
     */
    @CheckReturnValue
    TransactionalAction afterCompletion(Consumer<CommitContext> consumer);

    /**
     * Specifies the action to retrieve the {@link CommitContext} with all required changes (created, updated, deleted entities).
     * <p>Method returns the same instance of {@link com.haulmont.cuba.core.global.TransactionalAction} so
     * it can be used for method chaining.</p>
     *
     * @param supplier  implementation of {@link java.util.function.Supplier} interface
     * @return          the same instance to which the method was applied
     */
    @CheckReturnValue
    TransactionalAction withCommitContext(Supplier<CommitContext> supplier);

    /**
     * Specifies the {@link CommitContext} with all required changes (created, updated, deleted entities).
     * <p>If this method is used with {@code commitContext != null} then action
     * from {@code withCommitContext(Supplier&lt;CommitContext&gt; supplier)} will be ignored</p>
     * <p>Method returns the same instance of {@link com.haulmont.cuba.core.global.TransactionalAction} so
     * it can be used for method chaining.</p>
     *
     * @param commitContext contains all required changes (created, updated and deleted entities)
     * @return              the same instance to which the method was applied
     */
    @CheckReturnValue
    TransactionalAction withCommitContext(CommitContext commitContext);

    /**
     * Terminal operation. This method must be invoked to perform the commit and all specified additional actions.
     * <pre>Actions are invoked in the following order:
     *      1) {@code withCommitContext} action to retrieve the commitContext;
     *      2) {@code beforeCommit} action;
     *      3.1) if the commit was successful then {@code onSuccess} action;
     *      3.2) if the commit was unsuccessful then {@code onFail} action;
     *      4) {@code afterCompletion} action.</pre>
     <p>If {@code onFail} action is specified (not null) and an exception occurs during {@code commit}
     * then this exception won't be re-thrown by {@code perform()} method.
     * In this case {@code null} value will be returned as a result</p>
     *
     * @return {@link EntitySet} with all committed entities. This EntitySet might be modified by {@code onSuccess} action
     */
    EntitySet perform();

    /**
     * Specifies the {@code joinTransaction} parameter.
     * <p>Method returns the same instance of {@link com.haulmont.cuba.core.global.TransactionalAction} so
     * it can be used for method chaining.</p>
     *
     * @param joinTransaction   defines should {@link com.haulmont.cuba.core.global.CommitContext} be joined to existing transaction or not.
     *                          Default value = false
     * @return                  the same instance to which the method was applied
     */
    @CheckReturnValue
    TransactionalAction setJoinTransaction(boolean joinTransaction);

    /**
     * @return commitContext that was specified by {@code withCommitContext(CommitContext commitContext)} method
     */
    CommitContext getCommitContext();

    /**
     * @return joinTransaction parameter
     */
    boolean isJoinTransaction();
}
