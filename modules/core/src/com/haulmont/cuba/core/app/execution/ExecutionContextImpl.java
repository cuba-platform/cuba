/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.app.execution;

import com.haulmont.cuba.core.global.UuidProvider;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExecutionContextImpl implements ExecutionContext {
    protected UUID id;
    protected String key;
    protected String group;
    protected Date startTime;
    protected Thread thread;
    protected volatile State state;
    protected List<CancelableResource> resources = new CopyOnWriteArrayList<>();

    public ExecutionContextImpl(Thread thread, String key, String group, Date startTime) {
        this.id = UuidProvider.createUuid();
        this.thread = thread;
        this.key = key;
        this.group = group;
        this.startTime = startTime;
        this.state = State.ACTIVE;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public Date getStartTime() {
        return startTime;
    }

    @Override
    public boolean isCanceled() {
        return state == State.CANCELED;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void addResource(CancelableResource resource) {
        resources.add(resource);
    }

    @Override
    public void removeResource(CancelableResource resource) {
        resources.remove(resource);
    }

    public Thread getThread() {
        return thread;
    }

    public List<CancelableResource> getResources() {
        return new ArrayList<>(resources);
    }

    public void clearResources() {
        resources.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionContextImpl context = (ExecutionContextImpl) o;
        return Objects.equals(id, context.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
