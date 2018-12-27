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

package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.security.entity.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TestUserEntityListener implements
        BeforeInsertEntityListener<User>,
        BeforeUpdateEntityListener<User>,
        BeforeDeleteEntityListener<User>,
        AfterInsertEntityListener<User>,
        AfterUpdateEntityListener<User>,
        AfterDeleteEntityListener<User> {

    public static final List<String> events = new ArrayList<>();

    public static Map<String, Consumer<User>> consumers = new HashMap<>();

    @Override
    public void onBeforeInsert(User entity, EntityManager entityManager) {
        events.add("onBeforeInsert: " + entity.getId());
        if (consumers.get("BeforeInsert") != null) {
            consumers.get("BeforeInsert").accept(entity);
        }
    }

    @Override
    public void onBeforeUpdate(User entity, EntityManager entityManager) {
        events.add("onBeforeUpdate: " + entity.getId());
        if (consumers.get("BeforeUpdate") != null) {
            consumers.get("BeforeUpdate").accept(entity);
        }
    }

    @Override
    public void onBeforeDelete(User entity, EntityManager entityManager) {
        events.add("onBeforeDelete: " + entity.getId());
        if (consumers.get("BeforeDelete") != null) {
            consumers.get("BeforeDelete").accept(entity);
        }
    }

    @Override
    public void onAfterInsert(User entity, Connection connection) {
        events.add("onAfterInsert: " + entity.getId());
        if (consumers.get("AfterInsert") != null) {
            consumers.get("AfterInsert").accept(entity);
        }
    }

    @Override
    public void onAfterUpdate(User entity, Connection connection) {
        events.add("onAfterUpdate: " + entity.getId());
        if (consumers.get("AfterUpdate") != null) {
            consumers.get("AfterUpdate").accept(entity);
        }
    }

    @Override
    public void onAfterDelete(User entity, Connection connection) {
        events.add("onAfterDelete: " + entity.getId());
        if (consumers.get("AfterDelete") != null) {
            consumers.get("AfterDelete").accept(entity);
        }
    }
}
