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

package com.haulmont.cuba.core.sys.aop;

import com.google.common.base.Strings;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CascadePersistExceptionAspect {

    @Pointcut("execution(public * com.haulmont.cuba.core.app.RdbmsStore.commit(..))")
    public void rdbmsCommitPointcut() {
    }

    @AfterThrowing(pointcut = "rdbmsCommitPointcut()", throwing = "ex")
    public void changeCascadePersistMessage(IllegalStateException ex) {
        IllegalStateException exception = ex;
        if (!Strings.isNullOrEmpty(ex.getMessage())
                && ex.getMessage().contains("cascade PERSIST")) {
            exception = new IllegalStateException("An attempt to save an entity with reference to some not persisted entity. " +
                    "All newly created entities must be saved in the same transaction. " +
                    "Put all these objects to the CommitContext before commit.");
            exception.addSuppressed(ex);
        }
        throw exception;
    }
}