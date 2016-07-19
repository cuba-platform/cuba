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

import com.google.common.base.Strings;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.Stores;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

public class TransactionalInterceptor {

    private Persistence persistence;

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    private Object aroundInvoke(ProceedingJoinPoint ctx) throws Throwable {
        Method method = ((MethodSignature) ctx.getSignature()).getMethod();
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, ctx.getTarget().getClass());
        Transactional transactional = specificMethod.getAnnotation(Transactional.class);
        if (transactional == null)
            throw new IllegalStateException("Cannot determine data store of the current transaction");

        String storeName = Strings.isNullOrEmpty(transactional.value()) ? Stores.MAIN : transactional.value();

        TransactionSynchronizationManager.registerSynchronization(
                ((PersistenceImpl) persistence).createSynchronization(storeName));

        return ctx.proceed();
    }
}