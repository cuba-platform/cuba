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
 */

package com.haulmont.cuba.core.sys;

import java.util.concurrent.Callable;

/**
 * Wrapper class that propagates {@link SecurityContext} to the target Callable.
 */
public class SecurityContextAwareCallable<V> implements Callable<V> {

    private Callable<V> target;
    private SecurityContext securityContext;

    public SecurityContextAwareCallable(Callable<V> target) {
        this.target = target;
        this.securityContext = AppContext.getSecurityContext();
    }

    @Override
    public V call() throws Exception {
        AppContext.setSecurityContext(securityContext);
        try {
            return target.call();
        } finally {
            AppContext.setSecurityContext(null);
        }
    }
}