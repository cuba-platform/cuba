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

package com.haulmont.cuba.web.testsupport.ui;

import com.haulmont.cuba.client.sys.cache.CachingStrategy;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestCachingStrategy implements CachingStrategy {

    protected Object data;

    @Override
    public Object getObject() {
        return data;
    }

    @Override
    public Object loadObject() {
        return data;
    }

    @Override
    public ReadWriteLock lock() {
        return new ReentrantReadWriteLock();
    }

    @Override
    public boolean needToReload() {
        return false;
    }

    public void setData(Object data) {
        this.data = data;
    }
}