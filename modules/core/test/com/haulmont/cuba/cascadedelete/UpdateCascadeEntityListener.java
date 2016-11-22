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

package com.haulmont.cuba.cascadedelete;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.cuba.testmodel.cascadedelete.CascadeEntity;

import java.util.ArrayList;
import java.util.List;

public class UpdateCascadeEntityListener implements BeforeUpdateEntityListener<CascadeEntity> {
    protected Persistence persistence = AppBeans.get(Persistence.class);
    public static final List<String> updatedEvents = new ArrayList<>();

    @Override
    public void onBeforeUpdate(CascadeEntity entity, EntityManager entityManager) {
        updatedEvents.add(entity.getId().toString());
        if (entity.getName().contains("third")) {
            entity.getFather().setName("second#1");
        }
    }
}