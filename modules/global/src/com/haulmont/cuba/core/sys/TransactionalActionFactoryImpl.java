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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.*;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component(TransactionalActionFactory.NAME)
public class TransactionalActionFactoryImpl implements TransactionalActionFactory {

    @Inject
    protected BeanLocator beanLocator;

    @Override
    public TransactionalAction getTransactionalAction(Supplier<CommitContext> supplier) {
        return getTransactionalAction(supplier, false);
    }

    @Override
    public TransactionalAction getTransactionalAction(CommitContext commitContext) {
        return beanLocator.get(TransactionalAction.NAME, TransactionalAction.class)
                .withCommitContext(commitContext);
    }

    @Override
    public TransactionalAction getTransactionalAction(Supplier<CommitContext> supplier, boolean joinTransaction) {
        return beanLocator.get(TransactionalAction.NAME, TransactionalAction.class)
                .withCommitContext(supplier).setJoinTransaction(joinTransaction);
    }

    @Override
    public TransactionalAction getTransactionalAction(Supplier<CommitContext> supplier,
                                                      Consumer<EntitySet> onSuccessAction,
                                                      BiConsumer<CommitContext, Throwable> onFailAction,
                                                      Consumer<CommitContext> afterCommitAction,
                                                      boolean joinTransaction) {
        return beanLocator.get(TransactionalAction.NAME, TransactionalAction.class)
                .withCommitContext(supplier)
                .onSuccess(onSuccessAction)
                .onFail(onFailAction)
                .afterCompletion(afterCommitAction)
                .setJoinTransaction(joinTransaction);
    }

    @Override
    public TransactionalAction getTransactionalAction() {
        return beanLocator.get(TransactionalAction.NAME, TransactionalAction.class);
    }
}
