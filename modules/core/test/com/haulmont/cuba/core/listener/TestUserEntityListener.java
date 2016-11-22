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
import java.util.List;

public class TestUserEntityListener implements
        BeforeInsertEntityListener<User>,
        BeforeUpdateEntityListener<User>,
        BeforeDeleteEntityListener<User>,
        AfterInsertEntityListener<User>,
        AfterUpdateEntityListener<User>,
        AfterDeleteEntityListener<User> {

    public static final List<String> events = new ArrayList<>();

    @Override
    public void onBeforeInsert(User entity, EntityManager entityManager) {
        events.add("onBeforeInsert: " + entity.getId());
    }

    @Override
    public void onBeforeUpdate(User entity, EntityManager entityManager) {
        events.add("onBeforeUpdate: " + entity.getId());
    }

    @Override
    public void onBeforeDelete(User entity, EntityManager entityManager) {
        events.add("onBeforeDelete: " + entity.getId());
    }

    @Override
    public void onAfterInsert(User entity, Connection connection) {
        events.add("onAfterInsert: " + entity.getId());
    }

    @Override
    public void onAfterUpdate(User entity, Connection connection) {
        events.add("onAfterUpdate: " + entity.getId());
    }

    @Override
    public void onAfterDelete(User entity, Connection connection) {
        events.add("onAfterDelete: " + entity.getId());
    }
}
