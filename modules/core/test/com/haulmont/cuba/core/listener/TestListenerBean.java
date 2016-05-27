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

package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.Server;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Component("cuba_TestListenerBean")
public class TestListenerBean implements
        AfterInsertEntityListener<Server>,
        AfterUpdateEntityListener<Server>,
        AfterDeleteEntityListener<Server> {

    @Inject
    private Persistence persistence;

    public final List<String> events = new ArrayList<>();

    @Override
    public void onAfterDelete(Server entity) {
        if (persistence == null)
            throw new IllegalStateException("Injected value is null");
        events.add("onAfterDelete: " + entity.getId());
    }

    @Override
    public void onAfterInsert(Server entity) {
        if (persistence == null)
            throw new IllegalStateException("Injected value is null");
        events.add("onAfterInsert: " + entity.getId());
    }

    @Override
    public void onAfterUpdate(Server entity) {
        if (persistence == null)
            throw new IllegalStateException("Injected value is null");
        events.add("onAfterUpdate: " + entity.getId());
    }
}