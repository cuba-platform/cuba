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

import com.haulmont.cuba.core.app.NumberIdWorker;
import com.haulmont.cuba.core.global.NumberIdSource;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

@Component(NumberIdSource.NAME)
public class NumberIdSourceImpl implements NumberIdSource {

    @Inject
    protected NumberIdWorker worker;

    @Inject
    protected NumberIdCache cache;

    @Override
    public Long createLongId(String entityName) {
        return cache.createLongId(entityName, worker);
    }

    @Override
    public Integer createIntegerId(String entityName) {
        long nextLong = createLongId(entityName);
        int nextInt = (int) nextLong;
        if (nextInt != nextLong)
            throw new IllegalStateException("Error creating a new Integer ID for entity " + entityName
                    + ": sequence overflow");
        return nextInt;
    }
}