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

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntitySet;
import com.haulmont.cuba.core.global.TransactionalAction;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component(TransactionalAction.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TransactionalActionBean implements TransactionalAction {

    @Inject
    protected DataManager dataManager;

    protected CommitContext commitContext;
    protected Supplier<CommitContext> getCommitContextAction;
    protected Consumer<EntitySet> onSuccessAction;
    protected BiConsumer<CommitContext, Throwable> onFailAction;
    protected Consumer<CommitContext> afterCommitAction;
    protected boolean joinTransaction = false;

    @Override
    public TransactionalAction onSuccess(Consumer<EntitySet> consumer) {
        this.onSuccessAction = consumer;
        return this;
    }

    @Override
    public TransactionalAction onFail(BiConsumer<CommitContext, Throwable> consumer) {
        this.onFailAction = consumer;
        return this;
    }

    @Override
    public TransactionalAction afterCompletion(Consumer<CommitContext> consumer) {
        this.afterCommitAction = consumer;
        return this;
    }

    @Override
    public TransactionalAction withCommitContext(Supplier<CommitContext> supplier) {
        this.getCommitContextAction = supplier;
        return this;
    }

    @Override
    public TransactionalAction withCommitContext(CommitContext commitContext) {
        this.commitContext = commitContext;
        return this;
    }

    @Override
    public EntitySet perform() {
        if (commitContext == null && getCommitContextAction != null) {
            this.commitContext = getCommitContextAction.get();
        }

        if (commitContext == null) {
            throw new IllegalStateException("CommitContext can't be null.");
        }
        commitContext.setJoinTransaction(isJoinTransaction());

        EntitySet resultSet;
        try {
            resultSet = dataManager.commit(commitContext);
            if (onSuccessAction != null) {
                onSuccessAction.accept(resultSet);
            }
        }
        catch (Throwable e) {
            if (onFailAction == null) {
                throw e;
            }
            resultSet = null;
            onFailAction.accept(commitContext, e);
        }
        finally {
            if (afterCommitAction != null) {
                afterCommitAction.accept(commitContext);
            }
        }

        return resultSet;
    }

    @Override
    public CommitContext getCommitContext() {
        return commitContext;
    }

    @Override
    public boolean isJoinTransaction() {
        return joinTransaction || (commitContext != null && commitContext.isJoinTransaction());
    }

    @Override
    public TransactionalAction setJoinTransaction(boolean joinTransaction) {
        this.joinTransaction = joinTransaction;
        return this;
    }
}
